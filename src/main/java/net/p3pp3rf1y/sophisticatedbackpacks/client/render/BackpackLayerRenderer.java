package net.p3pp3rf1y.sophisticatedbackpacks.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;

/**
 * Worn backpack feature using the 26.2 submit-node pipeline.  The associated
 * mixin snapshots the source entity into its render state before this layer is
 * submitted, so no live entity is accessed from rendering.
 */
public class BackpackLayerRenderer<S extends LivingEntityRenderState, M extends EntityModel<? super S>> extends RenderLayer<S, M> {
	public BackpackLayerRenderer(RenderLayerParent<S, M> parent) {
		super(parent);
		BackpackModelManager.initModels();
	}

	@Override
	public void submit(PoseStack poseStack, SubmitNodeCollector collector, int packedLight, S state, float limbSwing, float limbSwingAmount) {
		if (!(state instanceof BackpackRenderStateAccess access)) {
			return;
		}
		BackpackRenderData data = access.sophisticatedbackpacks$getBackpackRenderData();
		if (data == null) {
			return;
		}
		IBackpackModel model = BackpackModelManager.getBackpackModel(data.stack().getItem());
		poseStack.pushPose();
		translate(getParentModel(), data, poseStack);
		model.submit(poseStack, collector, packedLight, data.mainColor(), data.accentColor(), data.stack().getItem(), data.renderInfo());
		poseStack.popPose();
	}

	private static void translate(EntityModel<?> parentModel, BackpackRenderData data, PoseStack poseStack) {
		if (parentModel instanceof HumanoidModel<?> humanoidModel) {
			humanoidModel.body.translateAndRotate(poseStack);
		}
		poseStack.mulPose(Axis.YP.rotationDegrees(180));
		float zOffset = data.wearsArmor() ? -0.35F : -0.3F;
		float yOffset = -0.75F;
		if (data.baby()) {
			zOffset += BackpackModel.CHILD_Z_OFFSET;
			yOffset = BackpackModel.CHILD_Y_OFFSET;
		}
		poseStack.translate(0, yOffset, zOffset);
		if (data.baby()) {
			poseStack.scale(BackpackModel.CHILD_SCALE, BackpackModel.CHILD_SCALE, BackpackModel.CHILD_SCALE);
		}
		if (data.entityType().equals(EntityType.getKey(EntityTypes.ENDERMAN))) {
			poseStack.translate(0, -0.8, 0);
		}
	}
}
