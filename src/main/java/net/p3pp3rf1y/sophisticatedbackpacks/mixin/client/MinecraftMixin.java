package net.p3pp3rf1y.sophisticatedbackpacks.mixin.client;

import net.minecraft.client.Minecraft;
import net.p3pp3rf1y.sophisticatedbackpacks.client.ClientEventHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
	@Inject(method = "pickBlockOrEntity", at = @At("HEAD"))
	private void sophisticatedbackpacks$handlePickBlock(CallbackInfo ci) {
		Minecraft minecraft = (Minecraft) (Object) this;
		if (minecraft.player != null && minecraft.hitResult != null) {
			ClientEventHandler.handleBlockPick(minecraft.player, minecraft.hitResult, net.minecraft.world.item.ItemStack.EMPTY);
		}
	}
}
