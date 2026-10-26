package com.windwoif.equilibrium.content.chemicalStack;

import com.windwoif.equilibrium.AllItems;
import com.windwoif.equilibrium.content.recipe.chemical.Chemical;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Item carrying a snapshot of a solid phase, stored as amounts (mmol) of chemicals in the stack tag.
 */
public class ChemicalStackItem extends Item {
    private static final String TAG_CONTENTS = "Contents";
    private static final String TAG_CHEMICAL = "Chemical";
    private static final String TAG_AMOUNT = "Amount";
    private static final String MIXTURE_KEY = "item.equilibrium.chemical_stack.mixture";

    public ChemicalStackItem(Properties properties) {
        super(properties);
    }

    public static ItemStack create(Chemical chemical, long amount) {
        return create(Map.of(chemical, amount));
    }

    public static ItemStack create(Map<Chemical, Long> contents) {
        ItemStack stack = new ItemStack(AllItems.CHEMICAL_STACK.get());
        setContents(stack, contents);
        return stack;
    }

    public static void setContents(ItemStack stack, Map<Chemical, Long> contents) {
        ListTag list = new ListTag();
        contents.forEach((chemical, amount) -> {
            if (amount <= 0) return;
            CompoundTag entry = new CompoundTag();
            entry.put(TAG_CHEMICAL, chemical.serializeNBT());
            entry.putLong(TAG_AMOUNT, amount);
            list.add(entry);
        });
        stack.getOrCreateTag().put(TAG_CONTENTS, list);
    }

    public Map<Chemical, Long> getContents(ItemStack stack) {
        CompoundTag tag = stack.getTag();
        if (tag == null || !tag.contains(TAG_CONTENTS, Tag.TAG_LIST)) return Map.of();
        ListTag list = tag.getList(TAG_CONTENTS, Tag.TAG_COMPOUND);
        Map<Chemical, Long> contents = new LinkedHashMap<>();
        for (int i = 0; i < list.size(); i++) {
            CompoundTag entry = list.getCompound(i);
            long amount = entry.getLong(TAG_AMOUNT);
            if (amount <= 0) continue;
            contents.merge(Chemical.fromNBT(entry.getCompound(TAG_CHEMICAL)), amount, Long::sum);
        }
        return contents;
    }

    /**
     * @return the average color of the carried chemicals, as RGB
     */
    public static int getColor(ItemStack stack) {
        if (!(stack.getItem() instanceof ChemicalStackItem item)) return 0xFFFFFF;
        Map<Chemical, Long> contents = item.getContents(stack);
        double total = contents.values().stream().mapToDouble(Long::doubleValue).sum();
        if (total <= 0) return 0xFFFFFF;

        double red = 0;
        double green = 0;
        double blue = 0;
        for (Map.Entry<Chemical, Long> entry : contents.entrySet()) {
            double fraction = entry.getValue() / total;
            int argb = entry.getKey().ARGB();
            red += ((argb >> 16) & 0xFF) * fraction;
            green += ((argb >> 8) & 0xFF) * fraction;
            blue += (argb & 0xFF) * fraction;
        }
        return ((int) Math.round(red) << 16) | ((int) Math.round(green) << 8) | (int) Math.round(blue);
    }

    @Override
    public Component getName(ItemStack stack) {
        Map<Chemical, Long> contents = getContents(stack);
        if (contents.size() == 1) {
            return Component.literal(contents.keySet().iterator().next().name());
        }
        if (contents.size() > 1) {
            return Component.translatableWithFallback(MIXTURE_KEY, "Chemical Mixture");
        }
        return super.getName(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        getContents(stack).forEach((chemical, amount) -> tooltip.add(
                Component.literal(String.format("%s  %.3f mol", chemical.name(), amount / 1000.0))
                        .withStyle(ChatFormatting.GRAY)));
        super.appendHoverText(stack, level, tooltip, flag);
    }
}
