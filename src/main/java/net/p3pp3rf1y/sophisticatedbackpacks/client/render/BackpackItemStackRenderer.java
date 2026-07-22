package net.p3pp3rf1y.sophisticatedbackpacks.client.render;

/**
 * Kept as the public client-side integration point used by add-ons.  Backpack
 * rendering itself is now supplied by {@link BackpackDynamicModel}, because
 * Minecraft 26.2 replaced Fabric's built-in item renderer callback with item
 * model special renderers.
 */
public final class BackpackItemStackRenderer {
	private BackpackItemStackRenderer() {
	}
}
