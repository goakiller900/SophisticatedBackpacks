package net.p3pp3rf1y.sophisticatedbackpacks.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.BackpackBlock;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.BackpackBlockEntity;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.wrapper.IBackpackWrapper;
import net.p3pp3rf1y.sophisticatedcore.renderdata.RenderInfo;

/** Renders the complete stack-aware backpack model for a placed backpack. */
public class BackpackBlockEntityRenderer implements BlockEntityRenderer<BackpackBlockEntity, BackpackBlockEntityRenderer.State> {
	public BackpackBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
		BackpackModelManager.initModels(context);
	}

	@Override
	public State createRenderState() {
		return new State();
	}

	@Override
	public void extractRenderState(BackpackBlockEntity blockEntity, State state, float partialTick, Vec3 cameraPos, ModelFeatureRenderer.CrumblingOverlay crumblingOverlay) {
		BlockEntityRenderer.super.extractRenderState(blockEntity, state, partialTick, cameraPos, crumblingOverlay);
		BlockState blockState = blockEntity.getBlockState();
		state.facing = blockState.getValue(BackpackBlock.FACING);
		IBackpackWrapper wrapper = blockEntity.getBackpackWrapper();
		state.backpack = wrapper.getBackpack().copy();
		state.renderInfo = wrapper.getRenderInfo();
		state.mainColor = wrapper.getMainColor();
		state.accentColor = wrapper.getAccentColor();
		state.displayItem.clear();
		state.renderInfo.getItemDisplayRenderInfo().getDisplayItem().ifPresent(displayItem ->
			Minecraft.getInstance().getItemModelResolver().updateForTopItem(state.displayItem, displayItem.getItem(), ItemDisplayContext.FIXED, blockEntity.getLevel(), null, 0));
	}

	@Override
	public void submit(State state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState cameraRenderState) {
		if (state.backpack.isEmpty() || state.renderInfo == null) {
			return;
		}
		IBackpackModel model = BackpackModelManager.getBackpackModel(state.backpack.getItem());
		poseStack.pushPose();
		poseStack.translate(0.5, 0, 0.5);
		poseStack.mulPose(Axis.YN.rotationDegrees(state.facing.toYRot()));
		poseStack.pushPose();
		/* The entity-style ModelPart root is at y=24/16 with inverted X/Y
		 * compared to the former block-model geometry.  This is the exact
		 * conversion back to block coordinates; the old 0.6/-2.5 transform
		 * applied only to the legacy fluid overlay. */
		poseStack.mulPose(Axis.YP.rotationDegrees(180));
		poseStack.mulPose(Axis.ZP.rotationDegrees(180));
		poseStack.translate(0, -1.5, 0);
		model.submit(poseStack, collector, state.lightCoords, state.mainColor, state.accentColor, state.backpack.getItem(), state.renderInfo);
		poseStack.popPose();
		if (!state.displayItem.isEmpty()) {
			poseStack.pushPose();
			poseStack.translate(0, 0.6, 0.25);
			poseStack.scale(0.5F, 0.5F, 0.5F);
			poseStack.mulPose(Axis.XN.rotationDegrees(180));
			state.renderInfo.getItemDisplayRenderInfo().getDisplayItem().ifPresent(display -> poseStack.mulPose(Axis.ZP.rotationDegrees(180F + display.getRotation())));
			state.displayItem.submit(poseStack, collector, state.lightCoords, 0, 0);
			poseStack.popPose();
		}
		poseStack.popPose();
	}

	public static class State extends BlockEntityRenderState {
		private Direction facing = Direction.NORTH;
		private ItemStack backpack = ItemStack.EMPTY;
		private RenderInfo renderInfo;
		private int mainColor;
		private int accentColor;
		private final ItemStackRenderState displayItem = new ItemStackRenderState();
	}
}
