package net.p3pp3rf1y.sophisticatedbackpacks.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.entity.LivingEntity;
import net.p3pp3rf1y.sophisticatedbackpacks.client.render.BackpackRenderData;
import net.p3pp3rf1y.sophisticatedbackpacks.client.render.BackpackRenderStateAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin {
	@Inject(method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V", at = @At("TAIL"))
	private void sophisticatedbackpacks$extractBackpackRenderData(LivingEntity entity, LivingEntityRenderState state, float partialTick, CallbackInfo ci) {
		BackpackRenderData data = BackpackRenderData.capture(entity);
		((BackpackRenderStateAccess) state).sophisticatedbackpacks$setBackpackRenderData(data);
		if (data == null) {
			return;
		}
		data.renderInfo().getItemDisplayRenderInfo().getDisplayItem().ifPresent(displayItem ->
			Minecraft.getInstance().getItemModelResolver().updateForLiving(data.displayItem(), displayItem.getItem(), ItemDisplayContext.FIXED, entity));
		if (!Minecraft.getInstance().isPaused() && entity.level().getRandom().nextInt(32) == 0) {
			data.renderInfo().getUpgradeRenderData().forEach((type, value) ->
				net.p3pp3rf1y.sophisticatedbackpacks.client.render.BackpackUpgradeEffects.render(entity.level(), entity.level().getRandom(),
					vector -> vector.rotate(com.mojang.math.Axis.XP.rotationDegrees(entity.isCrouching() ? 25 : 0)).add(0, 0.8F, entity.isCrouching() ? 0.9F : 0.7F).rotate(com.mojang.math.Axis.YN.rotationDegrees(entity.yBodyRot - 180)).add(entity.position().toVector3f()), value));
		}
	}
}
