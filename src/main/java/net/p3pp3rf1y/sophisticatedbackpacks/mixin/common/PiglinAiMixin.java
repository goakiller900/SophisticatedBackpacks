package net.p3pp3rf1y.sophisticatedbackpacks.mixin.common;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.piglin.PiglinAi;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.p3pp3rf1y.sophisticatedbackpacks.compat.CompatModIds;
import net.p3pp3rf1y.sophisticatedbackpacks.util.PlayerInventoryProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Arrays;

@Mixin(PiglinAi.class)
public class PiglinAiMixin {
	@Inject(method = "isWearingSafeArmor", at = @At(value = "HEAD"), cancellable = true)
	private static void sophisticatedBackpacks$isWearingSafeArmor(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
		for (ItemStack itemStack : sophisticatedBackpacks_getArmorSlots(entity)) {
			if (itemStack.makesPiglinsNeutral(entity)) {
				cir.setReturnValue(true);
				return;
			}
		}
	}

	@Unique
	private static Iterable<ItemStack> sophisticatedBackpacks_getArmorSlots(LivingEntity entity) {
		List<ItemStack> armorSlots = Arrays.stream(EquipmentSlot.values()).filter(EquipmentSlot::isArmor).map(entity::getItemBySlot).toList();
		if (entity instanceof Player player) {
			List<ItemStack> trinkets = Lists.newArrayList();
			PlayerInventoryProvider.get().runOnBackpacks(player, CompatModIds.TRINKETS, (backpack, inventoryHandlerName, identifier, slot) -> trinkets.add(backpack));
			return Iterables.concat(armorSlots, trinkets);
		}

		return armorSlots;
	}
}
