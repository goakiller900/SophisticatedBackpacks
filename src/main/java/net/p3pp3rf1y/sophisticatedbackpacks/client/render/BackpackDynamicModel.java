package net.p3pp3rf1y.sophisticatedbackpacks.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.special.SpecialModelRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.wrapper.BackpackWrapper;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.wrapper.IBackpackWrapper;
import net.p3pp3rf1y.sophisticatedcore.renderdata.RenderInfo;

import java.util.function.Consumer;

/**
 * Native 26.2 item-model renderer for backpacks.  The old Porting Lib geometry
 * loader baked mutable quads per stack; special models are the supported place
 * to submit that stack-dependent geometry now.
 */
public final class BackpackDynamicModel implements SpecialModelRenderer<BackpackDynamicModel.RenderData> {
	private final BackpackModel model;

	private BackpackDynamicModel(BackpackModel model) {
		this.model = model;
	}

	@Override
	public RenderData extractArgument(ItemStack stack) {
		ItemStack stackSnapshot = stack.copy();
		IBackpackWrapper wrapper = BackpackWrapper.fromStack(stackSnapshot);
		return new RenderData(stackSnapshot, wrapper.getRenderInfo(), wrapper.getMainColor(), wrapper.getAccentColor());
	}

	@Override
	public void submit(RenderData data, PoseStack poseStack, SubmitNodeCollector collector, int light, int overlay, boolean foil, int outlineColor) {
		IBackpackModel selectedModel = BackpackModelManager.getBackpackModel(data.stack().getItem());
		selectedModel.submitSpecial(poseStack, collector, light, overlay, foil, outlineColor,
				data.mainColor(), data.accentColor(), data.stack().getItem(), data.renderInfo());
		renderDisplayedItem(poseStack, collector, light, overlay, outlineColor, data.renderInfo());
	}

	private static void renderDisplayedItem(PoseStack poseStack, SubmitNodeCollector collector, int light, int overlay, int outlineColor, RenderInfo renderInfo) {
		renderInfo.getItemDisplayRenderInfo().getDisplayItem().ifPresent(displayItem -> {
			poseStack.pushPose();
			poseStack.translate(0.5, 0.6, 0.25);
			poseStack.scale(0.5F, 0.5F, 0.5F);
			poseStack.mulPose(Axis.ZP.rotationDegrees(displayItem.getRotation()));
			ItemStackRenderState itemState = new ItemStackRenderState();
			Minecraft minecraft = Minecraft.getInstance();
			minecraft.getItemModelResolver().updateForTopItem(itemState, displayItem.getItem(), ItemDisplayContext.FIXED, minecraft.level, null, 0);
			itemState.submit(poseStack, collector, light, overlay, outlineColor);
			poseStack.popPose();
		});
	}

	@Override
	public void getExtents(Consumer<org.joml.Vector3fc> consumer) {
		model.getExtents(consumer);
	}

	public record RenderData(ItemStack stack, RenderInfo renderInfo, int mainColor, int accentColor) {}

	public record Unbaked() implements SpecialModelRenderer.Unbaked<RenderData> {
		public static final Unbaked INSTANCE = new Unbaked();
		public static final MapCodec<Unbaked> MAP_CODEC = MapCodec.unit(INSTANCE);

		@Override
		public BackpackDynamicModel bake(BakingContext context) {
			return new BackpackDynamicModel(new BackpackModel(context.entityModelSet().bakeLayer(net.p3pp3rf1y.sophisticatedbackpacks.client.ClientEventHandler.BACKPACK_LAYER)));
		}

		@Override
		public MapCodec<Unbaked> type() {
			return MAP_CODEC;
		}
	}
}
