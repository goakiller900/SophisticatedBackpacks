package net.p3pp3rf1y.sophisticatedbackpacks.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;

/**
 * Worn backpack feature using the 26.2 submit-node pipeline.  The associated
 * mixin snapshots the source entity into its render state before this layer is
 * submitted, so no live entity is accessed from rendering.
 */
public class BackpackLayerRenderer<S extends LivingEntityRenderState, M extends EntityModel<? super S>> extends RenderLayer<S, M> {
	private static final float BABY_BODY_SCALE = 0.5F;
	private static final float BABY_BODY_Y_OFFSET = 1.5F;

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
			// Match the upstream layer's body-space transform. Players do not use
			// the non-player baby-model adjustment.
			if (data.baby() && !data.entityType().equals(EntityType.getKey(EntityTypes.PLAYER))) {
				poseStack.scale(BABY_BODY_SCALE, BABY_BODY_SCALE, BABY_BODY_SCALE);
				poseStack.translate(0.0F, BABY_BODY_Y_OFFSET, 0.0F);
			}
			humanoidModel.body.translateAndRotate(poseStack);
		}
		poseStack.mulPose(Axis.YP.rotationDegrees(180));
		poseStack.mulPose(Axis.ZP.rotationDegrees(180));
		float zOffset = data.wearsArmor() ? -0.35F : -0.3F;
		poseStack.translate(0, -0.25F, zOffset);
	}
}
