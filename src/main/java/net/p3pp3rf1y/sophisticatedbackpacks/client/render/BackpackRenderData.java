package net.p3pp3rf1y.sophisticatedbackpacks.client.render;

import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.BackpackItem;
import net.p3pp3rf1y.sophisticatedbackpacks.util.PlayerInventoryProvider;
import net.p3pp3rf1y.sophisticatedcore.renderdata.RenderInfo;

import javax.annotation.Nullable;

/** Immutable backpack snapshot collected with the vanilla entity render state. */
public record BackpackRenderData(ItemStack stack, RenderInfo renderInfo, int mainColor, int accentColor, boolean wearsArmor, boolean baby, boolean crouching, Identifier entityType, ItemStackRenderState displayItem) {
	public static final RenderStateDataKey<BackpackRenderData> RENDER_STATE_KEY = RenderStateDataKey.create(() -> "sophisticatedbackpacks:backpack");

	@Nullable
	public static BackpackRenderData capture(LivingEntity entity) {
		ItemStack stack;
		boolean wearsArmor;
		if (entity instanceof AbstractClientPlayer player) {
			/*
			 * The chest slot is the native 26.2 equip path.  Resolve it before the
			 * optional-inventory provider: integrations are allowed to add rendered
			 * handlers and may return no render info for a slot they own.  The old
			 * indirect lookup could therefore leave a backpack in CHEST with no
			 * BackpackRenderData, while the item model was still visible in the
			 * player's equipment/inventory view.
			 */
			ItemStack chestStack = player.getItemBySlot(EquipmentSlot.CHEST);
			boolean backpackIsInArmorSlot;
			if (chestStack.getItem() instanceof BackpackItem) {
				stack = chestStack;
				backpackIsInArmorSlot = true;
			} else {
				var rendered = PlayerInventoryProvider.get().getBackpackFromRendered(player);
				if (rendered.isEmpty()) {
					return null;
				}
				stack = rendered.get().getBackpack();
				backpackIsInArmorSlot = rendered.get().isArmorSlot();
			}
			EquipmentSlot renderSlot = BackpackModelManager.getBackpackModel(stack.getItem()).getRenderEquipmentSlot();
			// Match the old Fabric provider contract: this flag describes armor
			// underneath the backpack, not the backpack's own equipment slot.
			wearsArmor = (renderSlot != EquipmentSlot.CHEST || !backpackIsInArmorSlot) && !player.getItemBySlot(renderSlot).isEmpty();
		} else {
			stack = entity.getItemBySlot(EquipmentSlot.CHEST);
			if (!(stack.getItem() instanceof BackpackItem)) {
				return null;
			}
			wearsArmor = false;
		}
		var wrapper = net.p3pp3rf1y.sophisticatedbackpacks.backpack.wrapper.BackpackWrapper.fromStack(stack);
		return new BackpackRenderData(stack.copy(), wrapper.getRenderInfo(), wrapper.getMainColor(), wrapper.getAccentColor(), wearsArmor, entity.isBaby(), entity.isCrouching(), EntityType.getKey(entity.getType()), new ItemStackRenderState());
	}
}
