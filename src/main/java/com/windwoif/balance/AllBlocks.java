package com.windwoif.balance;

import com.simibubi.create.AllCreativeModeTabs;
import com.simibubi.create.foundation.data.BlockStateGen;
import com.simibubi.create.foundation.data.ModelGen;
import com.simibubi.create.foundation.data.TagGen;
import com.tterrag.registrate.providers.RegistrateRecipeProvider;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.windwoif.balance.content.pump.PumpBlock;
import com.windwoif.balance.content.valve.ValveBlock;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.Direction;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.client.model.generators.ModelFile;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.windwoif.balance.AllItems.HAMMER;
import static net.minecraft.resources.ResourceLocation.fromNamespaceAndPath;


@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AllBlocks {
    public static final BlockEntry<ValveBlock> VALVE_BLOCK = Balance.REGISTRATE
            .block("valve", ValveBlock::new)
            .initialProperties(() -> Blocks.DEEPSLATE)
            .blockstate((ctx, prov) -> {
                ModelFile off = prov.models().getExistingFile(fromNamespaceAndPath("balance", "block/valve/off"));
                ModelFile on = prov.models().getExistingFile(fromNamespaceAndPath("balance", "block/valve/on"));
                prov.getVariantBuilder(ctx.getEntry())
                        .forAllStatesExcept(state -> {
                            Direction dir = state.getValue(BlockStateProperties.FACING);
                            boolean powered = state.getValue(BlockStateProperties.POWERED);
                            ModelFile model = powered ? on : off;
                            int xRot = 0;
                            int yRot = 0;
                            if (!(dir == Direction.UP || dir == Direction.DOWN)) {
                                xRot = 90;
                                yRot = (int) dir.toYRot();
                            }
                            return ConfiguredModel.builder()
                                    .modelFile(model)
                                    .rotationX(xRot)
                                    .rotationY(yRot)
                                    .build();
                        });
            })
            .transform(TagGen.pickaxeOnly())
            .tag(BlockTags.MINEABLE_WITH_PICKAXE)
            .item()
            .tab(CreativeModeTabs.FUNCTIONAL_BLOCKS)
            .transform(ModelGen.customItemModel())
            .recipe((ctx, provider) -> ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ctx.get())
                    .requires(Items.POLISHED_DEEPSLATE)
                    .requires(Items.REDSTONE)
                    .unlockedBy("has_hammer", RegistrateRecipeProvider.has(HAMMER))
                    .save(provider, Balance.modLoc("valve")))
            .register();

    public static final BlockEntry<PumpBlock> PUMP = Balance.REGISTRATE
            .block("pump", PumpBlock::new)
            .initialProperties(() -> Blocks.DEEPSLATE)
            .properties(BlockBehaviour.Properties::noOcclusion)
            .blockstate(BlockStateGen.directionalBlockProvider(true))
            .transform(TagGen.pickaxeOnly())
            .tag(BlockTags.MINEABLE_WITH_PICKAXE)
            .item()
            .tab(AllCreativeModeTabs.BASE_CREATIVE_TAB.getKey())
            .transform(ModelGen.customItemModel())
            .recipe((ctx, provider) -> ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ctx.get())
                    .requires(VALVE_BLOCK)
                    .requires(com.simibubi.create.AllBlocks.COGWHEEL.asItem())
                    .unlockedBy("has_hammer", RegistrateRecipeProvider.has(HAMMER))
                    .save(provider, Balance.modLoc("pump")))
            .register();


    public static void init() {}
}
