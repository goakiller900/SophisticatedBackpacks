package net.p3pp3rf1y.sophisticatedbackpacks.mixin.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemInHandRenderer.class)
public class ItemInHandRendererMixin {
	@Shadow
	@Final
	private Minecraft minecraft;

	@Unique
	private int slotMainHand = 0;

	@Unique
	private boolean sophisticatedBackpacks_shouldCauseReequipAnimation(ItemStack from, ItemStack to, int slot) {
		boolean fromEmpty = from.isEmpty();
		boolean toEmpty = to.isEmpty();

		if (fromEmpty && toEmpty) return false;
		if (fromEmpty || toEmpty) return true;

		boolean changed = false;
		if (slot != -1) {
			changed = slot != slotMainHand;
			slotMainHand = slot;
		}
		return from.getItem().shouldCauseReequipAnimation(from, to, changed);
	}

	@Inject(method = "shouldInstantlyReplaceVisibleItem", at = @At("RETURN"), cancellable = true)
	private void sophisticatedbackpacks$skipReequipAnimation(ItemStack from, ItemStack to, CallbackInfoReturnable<Boolean> cir) {
		if (cir.getReturnValueZ()) {
			return;
		}

		LocalPlayer player = minecraft.player;
		int slot = player != null && to == player.getMainHandItem() ? player.getInventory().getSelectedSlot() : -1;
		if (!sophisticatedBackpacks_shouldCauseReequipAnimation(from, to, slot)) {
			cir.setReturnValue(true);
		}
	}

}
