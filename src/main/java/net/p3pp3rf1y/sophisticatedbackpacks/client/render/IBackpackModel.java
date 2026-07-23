package net.p3pp3rf1y.sophisticatedbackpacks.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.p3pp3rf1y.sophisticatedcore.fluid.FluidStack;
import net.p3pp3rf1y.sophisticatedcore.renderdata.RenderInfo;

public interface IBackpackModel {
	void submit(PoseStack poseStack, SubmitNodeCollector collector, int packedLight, int clothColor, int borderColor, Item backpackItem, RenderInfo renderInfo);

	/** Native special-item submission hook. Older providers retain their normal submission behavior. */
	default void submitSpecial(PoseStack poseStack, SubmitNodeCollector collector, int packedLight, int overlay, boolean foil, int outlineColor,
			int clothColor, int borderColor, Item backpackItem, RenderInfo renderInfo) {
		submit(poseStack, collector, packedLight, clothColor, borderColor, backpackItem, renderInfo);
	}

	void submitBatteryCharge(PoseStack matrixStack, SubmitNodeCollector collector, int packedLight, float chargeRatio);

	void submitFluid(PoseStack matrixStack, SubmitNodeCollector collector, int packedLight, FluidStack fluid, float fill, boolean left);

	EquipmentSlot getRenderEquipmentSlot();

	@Deprecated(forRemoval = false)
	void translateRotateAndScale(EntityModel<?> parentModel, LivingEntity livingEntity, PoseStack matrixStack, boolean wearsArmor);

	/**
	 * Render-state-safe 26.2 replacement for the live-entity transform hook.
	 * Add-on models may override this just as they overrode the former hook;
	 * the default retains the standard backpack placement for older providers.
	 */
	default void translateRotateAndScale(EntityModel<?> parentModel, BackpackRenderData data, PoseStack poseStack) {
		if (parentModel instanceof HumanoidModel<?> humanoidModel) {
			humanoidModel.body.translateAndRotate(poseStack);
		}
		poseStack.mulPose(Axis.YP.rotationDegrees(180));
		float zOffset = data.wearsArmor() ? -0.35F : -0.3F;
		float yOffset = -0.75F;
		if (data.baby()) {
			zOffset += 0.1F;
			yOffset = 0.3F;
		}
		poseStack.translate(0, yOffset, zOffset);
		if (data.entityType().equals(EntityType.getKey(EntityTypes.PLAYER))) {
			return;
		}
		if (data.baby()) {
			poseStack.scale(0.55F, 0.55F, 0.55F);
		}
		if (data.entityType().equals(EntityType.getKey(EntityTypes.ENDERMAN))) {
			poseStack.translate(0, -0.8F, 0);
		}
	}
}
