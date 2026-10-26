package com.windwoif.equilibrium.content.reactors.reactorEntity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.windwoif.equilibrium.client.CubeRenderer;
import com.windwoif.equilibrium.client.ModRenderTypes;
import com.windwoif.equilibrium.content.recipe.chemical.Chemical;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.phys.AABB;

import java.util.List;

import static com.windwoif.equilibrium.Equilibrium.MODID;

public class ReactorRenderer extends EntityRenderer<ReactorEntity> {
	private final TextureAtlasSprite liquidSprite;
	private final TextureAtlasSprite solidSprite;
	private final TextureAtlasSprite gasSprite;
	private final TextureAtlasSprite moltenSalt;
	private final TextureAtlasSprite moltenMetal;
	private final TextureAtlasSprite flameSprite;

	public ReactorRenderer(EntityRendererProvider.Context context) {
		super(context);
		var atlas = Minecraft.getInstance().getModelManager().getAtlas(InventoryMenu.BLOCK_ATLAS);
		this.liquidSprite = atlas.getSprite(ResourceLocation.parse(MODID + ":block/liquid"));
		this.solidSprite = atlas.getSprite(ResourceLocation.parse(MODID + ":block/solid"));
		this.gasSprite = atlas.getSprite(ResourceLocation.parse(MODID + ":block/gas"));
		this.moltenSalt = atlas.getSprite(ResourceLocation.parse(MODID + ":block/molten_salt"));
		this.moltenMetal = atlas.getSprite(ResourceLocation.parse(MODID + ":block/molten_metal"));
		this.flameSprite = atlas.getSprite(ResourceLocation.parse(MODID + ":block/flame"));
	}

	@Override
	public void render(ReactorEntity entity, float entityYaw, float partialTick,
					   PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
		List<PhaseData> phases = entity.getPhaseData();
		if (phases.isEmpty()) return;

		AABB bb = entity.getBoundingBox();
		float w = (float) bb.getXsize();
		float h = (float) bb.getYsize();
		float d = (float) bb.getZsize();

		poseStack.pushPose();
		poseStack.translate(bb.minX - entity.getX() + w/2,
				bb.minY - entity.getY() + h/2,
				bb.minZ - entity.getZ() + d/2);

		VertexConsumer consumer = bufferSource.getBuffer(ModRenderTypes.TRANSLUCENT_NO_CULL);
		double totalVolume = phases.stream().mapToDouble(PhaseData::volume).sum();
		double accumulatedVolume = 0.0;

		for (PhaseData phase : phases) {
			double phaseVolume = phase.volume();
			if (phaseVolume <= 0.001) continue;

			double bottomY = -h/2 + (accumulatedVolume / totalVolume) * h;
			double topY    = -h/2 + ((accumulatedVolume + phaseVolume) / totalVolume) * h;
			float yMin = (float) bottomY;
			float yMax = (float) topY;

			TextureAtlasSprite sprite = switch (phase.state()) {
				case SOLID -> solidSprite;
				case GAS -> gasSprite;
				case MOLTEN_SALT -> moltenSalt;
				case MOLTEN_METAL ->  moltenMetal;
				default -> liquidSprite;
			};

			int light = switch (phase.state()) {
				case MOLTEN_SALT, MOLTEN_METAL -> 0xF000F0;
				default -> packedLight;
			};

			int color = phase.color();

			float burnStrength = entity.getBurnStrength();
			int origAlpha = (color >> 24) & 0xFF;

			if (phase.state() == Chemical.State.GAS && burnStrength > origAlpha * 196) {

				light = 0xF000F0;
				sprite = flameSprite;
				int alpha = (int)(burnStrength * 255);
				color = (color & 0x00FFFFFF) | (alpha << 24);
			}
			if (color >> 24 == 0) continue;

			CubeRenderer.renderCube(poseStack, consumer, sprite,
					-w/2, yMin, -d/2,
					w/2, yMax,  d/2,
					color, light);

			accumulatedVolume += phaseVolume;
		}

		poseStack.popPose();
		super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
	}

	@Override
	public ResourceLocation getTextureLocation(ReactorEntity entity) {
		return null;
	}
}