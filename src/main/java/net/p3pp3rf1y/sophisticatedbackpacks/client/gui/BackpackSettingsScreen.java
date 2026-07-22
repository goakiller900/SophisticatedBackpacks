package net.p3pp3rf1y.sophisticatedbackpacks.client.gui;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.p3pp3rf1y.sophisticatedbackpacks.network.BackpackOpenPayload;
import net.p3pp3rf1y.sophisticatedbackpacks.settings.BackpackSettingsTabControl;
import net.p3pp3rf1y.sophisticatedcore.client.gui.SettingsScreen;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.Position;
import net.p3pp3rf1y.sophisticatedcore.common.gui.SettingsContainerMenu;
import net.p3pp3rf1y.sophisticatedcore.network.PacketDistributor;
import net.p3pp3rf1y.sophisticatedcore.settings.StorageSettingsTabControlBase;
import net.p3pp3rf1y.sophisticatedcore.settings.itemdisplay.IItemDisplaySettingsPreviewProvider;
import net.p3pp3rf1y.sophisticatedcore.settings.itemdisplay.ItemDisplaySettingsContainer;

import java.util.Optional;

public class BackpackSettingsScreen extends SettingsScreen {
	private final IItemDisplaySettingsPreviewProvider itemDisplayPreviewProvider = new IItemDisplaySettingsPreviewProvider() {
		@Override
		public Optional<net.minecraft.world.item.ItemStack> getItemDisplaySettingsPreviewStack(SettingsScreen screen,
				ItemDisplaySettingsContainer container, int selectedSlot) {
			if (selectedSlot < 0 || selectedSlot >= menu.getStorageWrapper().getInventoryHandler().getSlotCount()) {
				return Optional.empty();
			}
			return Optional.of(menu.getStorageWrapper().getInventoryHandler().getStackInSlot(selectedSlot));
		}
	};
	public BackpackSettingsScreen(SettingsContainerMenu<?> screenContainer, Inventory inv, Component titleIn) {
		super(screenContainer, inv, titleIn);
	}

	@Override
	protected StorageSettingsTabControlBase initializeTabControl() {
		return new BackpackSettingsTabControl(this, new Position(leftPos + imageWidth, topPos + 4));
	}

	public static BackpackSettingsScreen constructScreen(SettingsContainerMenu<?> settingsContainer, Inventory playerInventory, Component title) {
		return new BackpackSettingsScreen(settingsContainer, playerInventory, title);
	}

	@Override
	protected void sendStorageInventoryScreenOpenMessage() {
		PacketDistributor.sendToServer(new BackpackOpenPayload());
	}

	@Override
	public IItemDisplaySettingsPreviewProvider getItemDisplaySettingsPreviewProvider() {
		return itemDisplayPreviewProvider;
	}
}
