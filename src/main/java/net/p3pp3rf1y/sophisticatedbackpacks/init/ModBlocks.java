package net.p3pp3rf1y.sophisticatedbackpacks.init;

import net.p3pp3rf1y.sophisticatedbackpacks.util.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import team.reborn.energy.api.EnergyStorage;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.item.ItemStorage;
import net.p3pp3rf1y.sophisticatedbackpacks.SophisticatedBackpacks;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.BackpackBlock;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.BackpackBlockEntity;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class ModBlocks {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(BuiltInRegistries.BLOCK, SophisticatedBackpacks.MOD_ID);
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, SophisticatedBackpacks.MOD_ID);

    private ModBlocks() {
    }

	private static BlockBehaviour.Properties blockProperties(String name) {
		return BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, SophisticatedBackpacks.getRL(name)));
	}

    public static final Supplier<BackpackBlock> BACKPACK = BLOCKS.register("backpack", () -> new BackpackBlock(blockProperties("backpack"), 0.8F));
    public static final Supplier<BackpackBlock> COPPER_BACKPACK = BLOCKS.register("copper_backpack", () -> new BackpackBlock(blockProperties("copper_backpack"), 0.8F));
    public static final Supplier<BackpackBlock> IRON_BACKPACK = BLOCKS.register("iron_backpack", () -> new BackpackBlock(blockProperties("iron_backpack"), 0.8F));
    public static final Supplier<BackpackBlock> GOLD_BACKPACK = BLOCKS.register("gold_backpack", () -> new BackpackBlock(blockProperties("gold_backpack"), 0.8F));
    public static final Supplier<BackpackBlock> DIAMOND_BACKPACK = BLOCKS.register("diamond_backpack", () -> new BackpackBlock(blockProperties("diamond_backpack"), 0.8F));
    public static final Supplier<BackpackBlock> NETHERITE_BACKPACK = BLOCKS.register("netherite_backpack", () -> new BackpackBlock(blockProperties("netherite_backpack"), 1200));

	public static final List<Supplier<BackpackBlock>> BACKPACKS = List.of(BACKPACK, COPPER_BACKPACK, IRON_BACKPACK, GOLD_BACKPACK, DIAMOND_BACKPACK, NETHERITE_BACKPACK);


	@SuppressWarnings("ConstantConditions") //no datafixer type needed
    public static final Supplier<BlockEntityType<BackpackBlockEntity>> BACKPACK_TILE_TYPE = BLOCK_ENTITY_TYPES.register("backpack", () ->
			new BlockEntityType<>(BackpackBlockEntity::new, Set.of(BACKPACK.get(), COPPER_BACKPACK.get(), IRON_BACKPACK.get(), GOLD_BACKPACK.get(), DIAMOND_BACKPACK.get(), NETHERITE_BACKPACK.get())));

    public static void registerHandlers() {
        BLOCKS.register();
        BLOCK_ENTITY_TYPES.register();
		UseBlockCallback.EVENT.register(BackpackBlock::playerInteract);
		registerCapabilities();
    }

	private static void registerCapabilities() {
		ItemStorage.SIDED.registerForBlockEntity(BackpackBlockEntity::getExternalItemHandler, BACKPACK_TILE_TYPE.get());
		FluidStorage.SIDED.registerForBlockEntity(BackpackBlockEntity::getExternalFluidHandler, BACKPACK_TILE_TYPE.get());
		EnergyStorage.SIDED.registerForBlockEntity(BackpackBlockEntity::getExternalEnergyStorage, BACKPACK_TILE_TYPE.get());
	}
}
