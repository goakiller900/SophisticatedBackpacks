package net.p3pp3rf1y.sophisticatedbackpacks.backpack;

import net.fabricmc.fabric.api.loot.v3.LootTableEvents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.p3pp3rf1y.sophisticatedbackpacks.Config;
import net.p3pp3rf1y.sophisticatedbackpacks.SophisticatedBackpacks;
import net.p3pp3rf1y.sophisticatedbackpacks.data.SBInjectLootSubProvider;

import java.util.Map;

/** Replaces the removed global-loot-modifier path with Fabric's 26.2 loot hook. */
public final class BackpackLootEvents {
	private static final Map<ResourceKey<LootTable>, ResourceKey<LootTable>> INJECTED_TABLES = Map.of(
			BuiltInLootTables.SIMPLE_DUNGEON, SBInjectLootSubProvider.SIMPLE_DUNGEON,
			BuiltInLootTables.ABANDONED_MINESHAFT, SBInjectLootSubProvider.ABANDONED_MINESHAFT,
			BuiltInLootTables.DESERT_PYRAMID, SBInjectLootSubProvider.DESERT_PYRAMID,
			BuiltInLootTables.WOODLAND_MANSION, SBInjectLootSubProvider.WOODLAND_MANSION,
			BuiltInLootTables.SHIPWRECK_TREASURE, SBInjectLootSubProvider.SHIPWRECK_TREASURE,
			BuiltInLootTables.BASTION_TREASURE, SBInjectLootSubProvider.BASTION_TREASURE,
			BuiltInLootTables.END_CITY_TREASURE, SBInjectLootSubProvider.END_CITY_TREASURE,
			BuiltInLootTables.NETHER_BRIDGE, SBInjectLootSubProvider.NETHER_BRIDGE
	);

	private BackpackLootEvents() {
	}

	public static void register() {
		LootTableEvents.MODIFY_DROPS.register((table, context, drops) -> table.unwrapKey().ifPresent(key -> {
			replaceBackpackBlockDrop(key, context.getOptionalParameter(LootContextParams.BLOCK_ENTITY), drops);
			if (Boolean.TRUE.equals(Config.COMMON.chestLootEnabled.get())) {
				appendInjectedLoot(key, context, drops);
			}
		}));
	}

	private static void replaceBackpackBlockDrop(ResourceKey<LootTable> table, BlockEntity blockEntity, java.util.List<ItemStack> drops) {
		if (!(blockEntity instanceof BackpackBlockEntity backpackBlockEntity) || !isBackpackBlockTable(table)) {
			return;
		}
		drops.replaceAll(ignored -> backpackBlockEntity.getBackpackWrapper().getBackpack().copy());
	}

	private static boolean isBackpackBlockTable(ResourceKey<LootTable> table) {
		Identifier id = table.identifier();
		return id.getNamespace().equals(SophisticatedBackpacks.MOD_ID) && id.getPath().startsWith("blocks/") && id.getPath().endsWith("backpack");
	}

	private static void appendInjectedLoot(ResourceKey<LootTable> table, net.minecraft.world.level.storage.loot.LootContext context, java.util.List<ItemStack> drops) {
		ResourceKey<LootTable> injected = INJECTED_TABLES.get(table);
		if (injected != null) {
			context.getResolver().get(injected).ifPresent(lootTable -> lootTable.value().getRandomItems(context, drops::add));
		}
	}
}
