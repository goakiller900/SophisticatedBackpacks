package net.p3pp3rf1y.sophisticatedbackpacks.crafting;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.SmithingRecipe;
import net.minecraft.world.item.crafting.SmithingRecipeInput;
import net.minecraft.world.item.crafting.SmithingTransformRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.level.Level;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.BackpackItem;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.wrapper.BackpackWrapper;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.wrapper.IBackpackWrapper;
import net.p3pp3rf1y.sophisticatedbackpacks.init.ModItems;
import net.p3pp3rf1y.sophisticatedcore.SophisticatedCore;
import net.p3pp3rf1y.sophisticatedcore.crafting.IWrapperRecipe;
import net.p3pp3rf1y.sophisticatedcore.crafting.RecipeWrapperSerializer;

import java.util.Optional;
import java.util.List;

public class SmithingBackpackUpgradeRecipe implements SmithingRecipe, IWrapperRecipe<SmithingTransformRecipe> {
	private final SmithingTransformRecipe compose;

	public SmithingBackpackUpgradeRecipe(SmithingTransformRecipe compose) {
		this.compose = compose;
	}

	@Override
	public boolean isSpecial() {
		return true;
	}

	@Override
	public ItemStack assemble(SmithingRecipeInput inv) {
		ItemStack upgradedBackpack = compose.assemble(inv);
		if (SophisticatedCore.isLogicalServerThread()) {
			getBackpack(inv).map(ItemStack::getComponents).ifPresent(upgradedBackpack::applyComponents);
			IBackpackWrapper wrapper = BackpackWrapper.fromStack(upgradedBackpack);
			BackpackItem backpackItem = ((BackpackItem) upgradedBackpack.getItem());
			wrapper.setSlotNumbers(backpackItem.getNumberOfSlots(), backpackItem.getNumberOfUpgradeSlots());
		}
		return upgradedBackpack;
	}

	private Optional<ItemStack> getBackpack(SmithingRecipeInput inv) {
		ItemStack slotStack = inv.getItem(1);
		if (slotStack.getItem() instanceof BackpackItem) {
			return Optional.of(slotStack);
		}
		return Optional.empty();
	}

	@Override
	public RecipeSerializer<SmithingBackpackUpgradeRecipe> getSerializer() {
		return ModItems.SMITHING_BACKPACK_UPGRADE_RECIPE_SERIALIZER.get();
	}

	@Override
	public SmithingTransformRecipe getCompose() {
		return compose;
	}

	@Override
	public Optional<Ingredient> templateIngredient() {
		return compose.templateIngredient();
	}

	@Override
	public Ingredient baseIngredient() {
		return compose.baseIngredient();
	}

	@Override
	public Optional<Ingredient> additionIngredient() {
		return compose.additionIngredient();
	}

	@Override
	public boolean showNotification() {
		return compose.showNotification();
	}

	@Override
	public String group() {
		return compose.group();
	}

	@Override
	public PlacementInfo placementInfo() {
		return compose.placementInfo();
	}

	@Override
	public List<RecipeDisplay> display() {
		return compose.display();
	}

}
