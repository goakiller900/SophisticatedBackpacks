package net.p3pp3rf1y.sophisticatedbackpacks.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.fabricmc.fabric.api.client.rendering.v1.FabricRenderState;

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
		BackpackRenderData data = ((FabricRenderState) state).getData(BackpackRenderData.RENDER_STATE_KEY);
		if (data == null) {
			return;
		}
		IBackpackModel model = BackpackModelManager.getBackpackModel(data.stack().getItem());
		poseStack.pushPose();
		model.translateRotateAndScale(getParentModel(), data, poseStack);
		model.submit(poseStack, collector, packedLight, data.mainColor(), data.accentColor(), data.stack().getItem(), data.renderInfo());
		submitDisplayedItem(poseStack, collector, packedLight, data);
		poseStack.popPose();
	}

	private static void submitDisplayedItem(PoseStack poseStack, SubmitNodeCollector collector, int packedLight, BackpackRenderData data) {
		if (data.displayItem().isEmpty()) {
			return;
		}
		data.renderInfo().getItemDisplayRenderInfo().getDisplayItem().ifPresent(displayItem -> {
			poseStack.pushPose();
			// These are the working Fabric worn-model coordinates.  The FIXED
			// item state was captured from the living entity by the renderer mixin.
			poseStack.translate(0, 0.9F, -0.25F);
			poseStack.scale(0.5F, 0.5F, 0.5F);
			poseStack.mulPose(Axis.ZP.rotationDegrees(180F + displayItem.getRotation()));
			data.displayItem().submit(poseStack, collector, packedLight, OverlayTexture.NO_OVERLAY, 0);
			poseStack.popPose();
		});
	}
}
