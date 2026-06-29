package com.windwoif.balance;

import com.simibubi.create.foundation.data.AssetLookup;
import com.tterrag.registrate.util.entry.ItemEntry;
import com.windwoif.balance.content.hammer.HammerItem;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.Tags;

import static com.windwoif.balance.Balance.REGISTRATE;

public class AllItems {
    public static void init() {}
    public static final ItemEntry<HammerItem> HAMMER = REGISTRATE
            .item("hammer", (properties) -> new HammerItem(properties.stacksTo(1)))
            .model(AssetLookup.itemModelWithPartials())
            .recipe((ctx, prov) -> {
                ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ctx.get())
                        .pattern("SSS")
                        .pattern(" R ")
                        .pattern(" R ")
                        .define('S', ItemTags.LOGS)
                        .define('R', Tags.Items.RODS_WOODEN)
                        .unlockedBy("has_log", InventoryChangeTrigger.TriggerInstance.hasItems(
                                ItemPredicate.Builder.item().of(ItemTags.LOGS).build()
                        ))
                        .save(prov);
            })
            .register();
}
