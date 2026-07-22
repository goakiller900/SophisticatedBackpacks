package net.p3pp3rf1y.sophisticatedbackpacks.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.p3pp3rf1y.sophisticatedbackpacks.backpack.BackpackItem;
import net.p3pp3rf1y.sophisticatedbackpacks.client.KeybindHandler;
import net.p3pp3rf1y.sophisticatedbackpacks.common.gui.BackpackContainer;
import net.p3pp3rf1y.sophisticatedbackpacks.network.BackpackOpenPayload;
import net.p3pp3rf1y.sophisticatedcore.client.gui.StorageScreenBase;
import net.p3pp3rf1y.sophisticatedcore.network.PacketDistributor;

public class BackpackScreen extends StorageScreenBase<BackpackContainer> {
	public static BackpackScreen constructScreen(BackpackContainer screenContainer, Inventory inv, Component title) {
		return new BackpackScreen(screenContainer, inv, title);
	}

	public BackpackScreen(BackpackContainer screenContainer, Inventory inv, Component titleIn) {
		super(screenContainer, inv, titleIn);
	}

	@Override
	public boolean keyPressed(KeyEvent event) {
		/*if (getFocused() != null) {
			return super.keyPressed(keyCode, scanCode, modifiers);
		}*/
		if (event.key() == 256 || KeybindHandler.BACKPACK_OPEN_KEYBIND.matches(event)) {
			if (getMenu().isFirstLevelStorage() && (event.key() == 256 || mouseNotOverBackpack())) {
				if (getMenu().getBackpackContext().wasOpenFromInventory()) {
					this.minecraft.player.closeContainer();
					this.minecraft.setScreenAndShow(new InventoryScreen(this.minecraft.player));
				} else {
					onClose();
				}
				return true;
			} else if (!getMenu().isFirstLevelStorage()) {
				PacketDistributor.sendToServer(new BackpackOpenPayload());
				return true;
			}
		}
		return super.keyPressed(event);
	}

	private boolean mouseNotOverBackpack() {
		Slot selectedSlot = sophisticatedCore_getSlotUnderMouse();
		return selectedSlot == null || !(selectedSlot.getItem().getItem() instanceof BackpackItem);
	}

	@Override
	protected String getStorageSettingsTabTooltip() {
		return SBPTranslationHelper.INSTANCE.translGui("settings.tooltip");
	}

	@Override
	public void extractRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
		super.extractRenderState(guiGraphics, mouseX, mouseY, partialTicks);
		if (getMenu().getNumberOfStorageInventorySlots() == 0 && Minecraft.getInstance().player != null) {
			Minecraft.getInstance().player.closeContainer();
		}
	}
}
