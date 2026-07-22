package net.p3pp3rf1y.sophisticatedbackpacks;

import fuzs.forgeconfigapiport.fabric.api.v5.ConfigRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.neoforged.fml.config.ModConfig;
import net.p3pp3rf1y.sophisticatedbackpacks.command.SBPCommand;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.BackpackLootEvents;
import net.p3pp3rf1y.sophisticatedbackpacks.common.CommonEventHandler;
import net.p3pp3rf1y.sophisticatedbackpacks.init.ModItems;
import net.p3pp3rf1y.sophisticatedbackpacks.registry.RegistryLoader;
import net.p3pp3rf1y.sophisticatedcore.compat.CompatRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SophisticatedBackpacks implements ModInitializer {
	public static final String MOD_ID = "sophisticatedbackpacks";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private final RegistryLoader registryLoader = new RegistryLoader();
	public final CommonEventHandler commonEventHandler = new CommonEventHandler();

	@Override
	public void onInitialize() {
		ConfigRegistry.INSTANCE.register(MOD_ID, ModConfig.Type.SERVER, Config.SERVER_SPEC);
		ConfigRegistry.INSTANCE.register(MOD_ID, ModConfig.Type.COMMON, Config.COMMON_SPEC);
		commonEventHandler.registerHandlers();
		setup();
		BackpackLootEvents.register();
		Config.SERVER.initListeners();
		SBPCommand.init();
		ResourceManagerHelper.get(PackType.SERVER_DATA).registerReloadListener(registryLoader);

		CompatRegistry.getRegistry(MOD_ID).setupCompats();
	}

	private static void setup() {
		ModItems.registerDispenseBehavior();
		ModItems.registerCauldronInteractions();
	}

	public static Identifier getRL(String regName) {
		return Identifier.fromNamespaceAndPath(MOD_ID, regName);
	}

	public static String getRegistryName(String regName) {
		return MOD_ID + ":" + regName;
	}
}
