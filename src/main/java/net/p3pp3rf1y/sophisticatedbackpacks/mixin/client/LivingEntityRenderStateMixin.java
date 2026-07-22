package net.p3pp3rf1y.sophisticatedbackpacks.mixin.client;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.p3pp3rf1y.sophisticatedbackpacks.client.render.BackpackRenderData;
import net.p3pp3rf1y.sophisticatedbackpacks.client.render.BackpackRenderStateAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import javax.annotation.Nullable;

@Mixin(LivingEntityRenderState.class)
public class LivingEntityRenderStateMixin implements BackpackRenderStateAccess {
	@Unique
	@Nullable
	private BackpackRenderData sophisticatedbackpacks$backpackRenderData;

	@Override
	@Nullable
	public BackpackRenderData sophisticatedbackpacks$getBackpackRenderData() {
		return sophisticatedbackpacks$backpackRenderData;
	}

	@Override
	public void sophisticatedbackpacks$setBackpackRenderData(@Nullable BackpackRenderData data) {
		sophisticatedbackpacks$backpackRenderData = data;
	}
}
