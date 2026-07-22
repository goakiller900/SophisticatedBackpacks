package net.p3pp3rf1y.sophisticatedbackpacks.compat.jei;

import net.fabricmc.fabric.api.tag.convention.v2.ConventionalItemTags;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;
import net.p3pp3rf1y.sophisticatedbackpacks.SophisticatedBackpacks;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.wrapper.BackpackWrapper;
import net.p3pp3rf1y.sophisticatedbackpacks.init.ModItems;
import net.p3pp3rf1y.sophisticatedcore.util.ColorHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DyeRecipesMaker {
	private DyeRecipesMaker() {
	}

	public static List<RecipeHolder<CraftingRecipe>> getRecipes() {
		List<RecipeHolder<CraftingRecipe>> recipes = new ArrayList<>();
		addSingleColorRecipes(recipes);
		addMultipleColorsRecipe(recipes);

		return recipes;
	}

	private static void addMultipleColorsRecipe(List<RecipeHolder<CraftingRecipe>> recipes) {
		List<Optional<Ingredient>> ingredients = new ArrayList<>();
		ingredients.add(Optional.of(ingredient(ConventionalItemTags.YELLOW_DYES)));
		ingredients.add(Optional.of(Ingredient.of(ModItems.BACKPACK.get())));
		ingredients.add(Optional.empty());
		ingredients.add(Optional.of(ingredient(ConventionalItemTags.LIME_DYES)));
		ingredients.add(Optional.of(ingredient(ConventionalItemTags.BLUE_DYES)));
		ingredients.add(Optional.of(ingredient(ConventionalItemTags.BLACK_DYES)));

		ItemStack backpackOutput = new ItemStack(ModItems.BACKPACK.get());
		int clothColor = ColorHelper.calculateColor(BackpackWrapper.DEFAULT_MAIN_COLOR, BackpackWrapper.DEFAULT_MAIN_COLOR, List.of(
				DyeColor.YELLOW, DyeColor.LIME
		));
		int trimColor = ColorHelper.calculateColor(BackpackWrapper.DEFAULT_ACCENT_COLOR, BackpackWrapper.DEFAULT_ACCENT_COLOR, List.of(
				DyeColor.BLUE, DyeColor.BLACK
		));

		BackpackWrapper.fromStack(backpackOutput).setColors(clothColor, trimColor);

		ShapedRecipePattern pattern = new ShapedRecipePattern(3, 2, ingredients, Optional.empty());
		Identifier id = Identifier.fromNamespaceAndPath(SophisticatedBackpacks.MOD_ID, "multiple_colors");
		recipes.add(new RecipeHolder<>(ResourceKey.create(Registries.RECIPE, id), new ShapedRecipe(new Recipe.CommonInfo(false),
				new CraftingRecipe.CraftingBookInfo(CraftingBookCategory.MISC, ""), pattern, ItemStackTemplate.fromNonEmptyStack(backpackOutput))));
	}

	private static void addSingleColorRecipes(List<RecipeHolder<CraftingRecipe>> recipes) {
		for (DyeColor color : DyeColor.values()) {
			ItemStack backpackOutput = new ItemStack(ModItems.BACKPACK.get());
			BackpackWrapper.fromStack(backpackOutput).setColors(color.getTextureDiffuseColor(), color.getTextureDiffuseColor());
			List<Optional<Ingredient>> ingredients = new ArrayList<>();
			ingredients.add(Optional.of(Ingredient.of(ModItems.BACKPACK.get())));
			ingredients.add(Optional.of(ingredient(TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath("c", "dyes/" + color.getName())))));

			ShapedRecipePattern pattern = new ShapedRecipePattern(1, 2, ingredients, Optional.empty());
			Identifier id = Identifier.fromNamespaceAndPath(SophisticatedBackpacks.MOD_ID, "single_color_" + color.getSerializedName());
			recipes.add(new RecipeHolder<>(ResourceKey.create(Registries.RECIPE, id), new ShapedRecipe(new Recipe.CommonInfo(false),
					new CraftingRecipe.CraftingBookInfo(CraftingBookCategory.MISC, ""), pattern, ItemStackTemplate.fromNonEmptyStack(backpackOutput))));
		}
	}

	private static Ingredient ingredient(TagKey<net.minecraft.world.item.Item> tag) {
		return Ingredient.of(BuiltInRegistries.ITEM.get(tag).orElseThrow());
	}
}
