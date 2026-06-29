package com.windwoif.balance;

import com.mojang.logging.LogUtils;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.windwoif.balance.content.reactors.connection.ReactorConnectionManager;
import com.windwoif.balance.content.recipe.chemical.Chemical;
import com.windwoif.balance.content.recipe.chemical.Chemicals;
import com.windwoif.balance.content.recipe.material.Material;
import com.windwoif.balance.content.recipe.material.Materials;
import com.windwoif.balance.content.recipe.reaction.Reaction;
import com.windwoif.balance.content.recipe.reaction.Reactions;
import com.windwoif.balance.network.BalanceNetwork;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.slf4j.Logger;


// The value here should match an entry in the META-INF/mods.toml file
@Mod(Balance.MODID)
public class Balance
{
    public static final String MODID = "balance";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final ResourceKey<Registry<Chemical>> CHEMICAL_REGISTRY_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(MODID, "chemicals"));
    public static final ResourceKey<Registry<Reaction>> REACTION_REGISTRY_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(MODID, "reactions"));
    public static final ResourceKey<Registry<Material>> MATERIAL_REGISTRY_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(MODID, "materials"));

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Balance.MODID);
    public static final DeferredRegister<Chemical> CHEMICALS = DeferredRegister.create(CHEMICAL_REGISTRY_KEY, MODID);
    public static final DeferredRegister<Reaction> REACTIONS = DeferredRegister.create(REACTION_REGISTRY_KEY, MODID);
    public static final DeferredRegister<Material> MATERIALS = DeferredRegister.create(MATERIAL_REGISTRY_KEY, MODID);

    public static ResourceLocation modLoc(String path){
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MODID);

    private static void initializeRegistries() {
        AllBlocks.init();
        AllItems.init();
        AllEntityTypes.init();
        AllBlockEntityTypes.init();
        Chemicals.init();
        Reactions.init();
        Materials.init();
    }
    public Balance(FMLJavaModLoadingContext context) {
        initializeRegistries();
        IEventBus modEventBus = context.getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        REGISTRATE.registerEventListeners(modEventBus);

        ENTITY_TYPES.register(modEventBus);

        CHEMICALS.register(modEventBus);
        REACTIONS.register(modEventBus);
        MATERIALS.register(modEventBus);
        MinecraftForge.EVENT_BUS.register(this);

        context.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
        BalanceNetwork.registerPackets();
//        com.windwoif.balance.dimension.DimensionAtmosphere.init();
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
//        // Some common setup code
//        LOGGER.info("HELLO FROM COMMON SETUP");
//
//        if (Config.logDirtBlock)
//            LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));
//
//        LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {
//        // Do something when the server starts
//        LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            // Some client setup code
//            LOGGER.info("HELLO FROM CLIENT SETUP");
//            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }

    @Mod.EventBusSubscriber(modid = Balance.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class ServerEvents {
        @SubscribeEvent
        public static void onServerTick(TickEvent.ServerTickEvent event) {
            if (event.phase == TickEvent.Phase.END) {
                MinecraftServer server = ServerLifecycleHooks.getCurrentServer();
                if (server == null) {
                    return;
                }
                for (ServerLevel level : server.getAllLevels()) {
                    ReactorConnectionManager manager = ReactorConnectionManager.get(level);
                    if (manager != null) {
                        manager.tick();
                    }
                }
            }
        }
    }

}
