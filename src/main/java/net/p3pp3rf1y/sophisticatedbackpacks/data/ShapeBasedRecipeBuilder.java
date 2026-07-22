package net.p3pp3rf1y.sophisticatedbackpacks.data;

import com.google.common.base.Preconditions;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.fabricmc.fabric.impl.datagen.FabricDataGenHelper;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.ItemLike;
import net.p3pp3rf1y.sophisticatedcore.crafting.HoldingRecipeOutput;
import net.p3pp3rf1y.sophisticatedcore.crafting.ItemEnabledCondition;
import net.p3pp3rf1y.sophisticatedcore.crafting.SCShapedRecipeBuilder;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

/** Builds a shaped recipe that may be wrapped by a non-ShapedRecipe recipe implementation. */
public class ShapeBasedRecipeBuilder extends SCShapedRecipeBuilder {
	private final Function<ShapedRecipe, ? extends Recipe<?>> factory;
	private final HolderGetter<Item> items;

	private ShapeBasedRecipeBuilder(HolderGetter<Item> items, ItemStackTemplate result, Function<ShapedRecipe, ? extends Recipe<?>> factory) {
		super(RecipeCategory.MISC, result);
		this.items = items;
		this.factory = factory;
	}

	@Override
	public ShapeBasedRecipeBuilder define(Character symbol, TagKey<Item> tag) {
		super.define(symbol, net.minecraft.world.item.crafting.Ingredient.of(items.getOrThrow(tag)));
		return this;
	}

	public static Factory factory(HolderGetter<Item> items) {
		return new Factory(items);
	}

	public static final class Factory {
		private final HolderGetter<Item> items;

		private Factory(HolderGetter<Item> items) {
			this.items = items;
		}

		public ShapeBasedRecipeBuilder shaped(ItemStack result) {
			return new ShapeBasedRecipeBuilder(items, ItemStackTemplate.fromNonEmptyStack(result), recipe -> recipe);
		}

		public ShapeBasedRecipeBuilder shaped(ItemLike result) {
			return new ShapeBasedRecipeBuilder(items, new ItemStackTemplate(result.asItem()), recipe -> recipe);
		}

		public ShapeBasedRecipeBuilder shaped(ItemLike result, Function<ShapedRecipe, ? extends Recipe<?>> factory) {
			return new ShapeBasedRecipeBuilder(items, new ItemStackTemplate(result.asItem()), factory);
		}

		public ShapeBasedRecipeBuilder shaped(ItemStack result, Function<ShapedRecipe, ? extends Recipe<?>> factory) {
			return new ShapeBasedRecipeBuilder(items, ItemStackTemplate.fromNonEmptyStack(result), factory);
		}
	}

	@Override
	public void save(RecipeOutput recipeOutput) {
		save(recipeOutput, defaultId());
	}

	@Override
	public void save(RecipeOutput recipeOutput, ResourceKey<Recipe<?>> id) {
		HoldingRecipeOutput holdingRecipeOutput = new HoldingRecipeOutput(recipeOutput.advancement());
		super.save(holdingRecipeOutput, id);

		if (!(holdingRecipeOutput.getRecipe() instanceof ShapedRecipe compose)) {
			return;
		}

		withConditions(recipeOutput, new ItemEnabledCondition(getResult())).accept(id, factory.apply(compose), holdingRecipeOutput.getAdvancementHolder());
	}

	private RecipeOutput withConditions(RecipeOutput exporter, ResourceCondition... conditions) {
		Preconditions.checkArgument(conditions.length > 0, "Must add at least one condition.");
		return new RecipeOutput() {
			@Override
			public void accept(ResourceKey<Recipe<?>> location, Recipe<?> recipe, @Nullable AdvancementHolder advancement) {
				FabricDataGenHelper.addConditions(recipe, conditions);
				exporter.accept(location, recipe, advancement);
			}

			@Override
			public Advancement.Builder advancement() {
				return exporter.advancement();
			}

			@Override
			public void includeRootAdvancement() {
				exporter.includeRootAdvancement();
			}
		};
	}
}
