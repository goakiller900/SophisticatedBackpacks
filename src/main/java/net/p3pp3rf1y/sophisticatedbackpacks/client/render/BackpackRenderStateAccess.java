package net.p3pp3rf1y.sophisticatedbackpacks.client.render;

import javax.annotation.Nullable;

/** Implemented on vanilla living-entity render states by the client mixin. */
public interface BackpackRenderStateAccess {
	@Nullable BackpackRenderData sophisticatedbackpacks$getBackpackRenderData();

	void sophisticatedbackpacks$setBackpackRenderData(@Nullable BackpackRenderData data);
}
