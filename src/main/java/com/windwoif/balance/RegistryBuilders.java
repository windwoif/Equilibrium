package com.windwoif.balance;


import com.windwoif.balance.content.recipe.chemical.Chemical;
import com.windwoif.balance.content.recipe.reaction.Reaction;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.NewRegistryEvent;
import net.minecraftforge.registries.RegistryBuilder;

import static com.windwoif.balance.Balance.MODID;

public class RegistryBuilders {
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class ModEvents {
        @SubscribeEvent
        public static void onNewRegistry(NewRegistryEvent event) {
            event.create(new RegistryBuilder<Chemical>()
                    .setName(Balance.CHEMICAL_REGISTRY_KEY.location())
                    .hasTags()
            );
            event.create(new RegistryBuilder<Reaction>()
                    .setName(Balance.REACTION_REGISTRY_KEY.location())
                    .hasTags()
            );
        }
    }
}