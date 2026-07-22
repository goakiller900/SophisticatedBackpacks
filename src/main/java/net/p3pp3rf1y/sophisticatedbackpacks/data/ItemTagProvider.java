package net.p3pp3rf1y.sophisticatedbackpacks.data;

import net.fabricmc.fabric.api.datagen.v1.FabricPackOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagsProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.p3pp3rf1y.sophisticatedbackpacks.SophisticatedBackpacks;
import net.p3pp3rf1y.sophisticatedbackpacks.init.ModItems;
import net.p3pp3rf1y.sophisticatedcore.upgrades.UpgradeItemBase;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class ItemTagProvider extends FabricTagsProvider.ItemTagsProvider {
	public ItemTagProvider(FabricPackOutput output, CompletableFuture<HolderLookup.Provider> completableFuture) {
		super(output, completableFuture);
	}

	@Override
	protected void addTags(HolderLookup.Provider pProvider) {
		var upgradeTag = builder(ModItems.BACKPACK_UPGRADE_TAG);
		BuiltInRegistries.ITEM.entrySet().stream()
			.filter(entry -> entry.getKey().identifier().getNamespace().equals(SophisticatedBackpacks.MOD_ID) && entry.getValue() instanceof UpgradeItemBase)
				.forEach(entry -> {
					if (entry.getKey().identifier().getPath().contains("/")) {
						upgradeTag.addOptional(entry.getKey());
					} else {
						upgradeTag.add(entry.getKey());
					}
				});
	}
}
