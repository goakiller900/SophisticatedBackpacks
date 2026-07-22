package net.p3pp3rf1y.sophisticatedbackpacks.client.init;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ModItemColors {
	private ModItemColors() {
	}

	public static void registerItemColorHandlers() {
		// 26.2 removed Fabric's mutable item-color registry.  Backpack special
		// models submit their cloth, trim and fluid parts with the stack's colors.
	}
}
