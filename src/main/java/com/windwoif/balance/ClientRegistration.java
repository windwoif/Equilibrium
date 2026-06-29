package com.windwoif.balance;

import com.windwoif.balance.content.reactors.reactorEntity.ReactorEntity;
import com.windwoif.balance.content.reactors.reactorEntity.ReactorRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static com.windwoif.balance.AllEntityTypes.REACTOR_ENTITY;

@Mod.EventBusSubscriber(modid = Balance.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientRegistration {
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.<ReactorEntity>registerEntityRenderer(REACTOR_ENTITY.get(), ReactorRenderer::new);
    }
}
