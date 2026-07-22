package net.p3pp3rf1y.sophisticatedbackpacks.client.init;


public class ModBlockColors {
	private ModBlockColors() {}

	public static void registerBlockColorHandlers() {
		// Block tint sources are immutable state data in 26.2 and cannot inspect a
		// block entity.  The backpack block-entity renderer therefore submits the
		// complete colored model from its extracted render state.
	}
}
