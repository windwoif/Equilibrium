package com.windwoif.equilibrium;

import com.windwoif.equilibrium.content.chemicalStack.ChemicalStackItem;
import com.windwoif.equilibrium.content.recipe.chemical.Ions;
import com.windwoif.equilibrium.content.recipe.chemical.Oxides;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.Map;

public class AllCreativeModeTabs {
    private static final String MAIN_TITLE_KEY = "itemGroup.equilibrium.main";

    static {
        Equilibrium.REGISTRATE.addRawLang(MAIN_TITLE_KEY, "Equilibrium");
    }

    private static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Equilibrium.MODID);

    public static final RegistryObject<CreativeModeTab> MAIN = TABS.register("main",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable(MAIN_TITLE_KEY))
                    .icon(() -> new ItemStack(AllItems.HAMMER.get()))
                    .displayItems((parameters, output) -> {
                        output.accept(AllItems.HAMMER.get());
                        output.accept(AllBlocks.VALVE_BLOCK.asStack());
                        output.accept(AllBlocks.PUMP.asStack());
                        addChemicalStacks(output);
                    })
                    .build());

    /**
     * Adds samples of chemical stacks with a fixed composition.
     */
    private static void addChemicalStacks(CreativeModeTab.Output output) {
        output.accept(ChemicalStackItem.create(Oxides.H2O_s, 10000000));
        output.accept(ChemicalStackItem.create(Oxides.H2O_l, 10000000));
        output.accept(ChemicalStackItem.create(Map.of(Ions.Cl__aq, 100000L, Ions.Na$_aq, 100000L)));
    }

    public static void register(IEventBus modEventBus) {
        TABS.register(modEventBus);
    }
}
