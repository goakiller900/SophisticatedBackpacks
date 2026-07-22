package net.p3pp3rf1y.sophisticatedbackpacks.upgrades.smithing;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.CyclingSlotBackground;
import net.minecraft.client.gui.screens.inventory.SmithingScreen;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.entity.state.ArmorStandRenderState;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.inventory.Slot;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SmithingTemplateItem;
import net.minecraft.world.item.equipment.Equippable;
import net.p3pp3rf1y.sophisticatedbackpacks.client.gui.SBPTranslationHelper;
import net.p3pp3rf1y.sophisticatedcore.client.gui.StorageScreenBase;
import net.p3pp3rf1y.sophisticatedcore.client.gui.UpgradeSettingsTab;
import net.p3pp3rf1y.sophisticatedcore.client.gui.utils.*;

import java.util.List;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class SmithingUpgradeTab extends UpgradeSettingsTab<SmithingUpgradeContainer> {

	public static final TextureBlitData ARROW = new TextureBlitData(GuiHelper.GUI_CONTROLS, Dimension.SQUARE_256, new UV(56, 221), new Dimension(14, 15));
	public static final TextureBlitData RED_CROSS = new TextureBlitData(GuiHelper.GUI_CONTROLS, Dimension.SQUARE_256, new UV(113, 216), new Dimension(15, 15));
	private final CyclingSlotBackground templateIcon;
	private final CyclingSlotBackground baseIcon;
	private final CyclingSlotBackground additionalIcon;
	private static final Vector3f ARMOR_STAND_TRANSLATION = new Vector3f(0.0F, 1.0F, 0.0F);
	private static final Quaternionf ARMOR_STAND_ANGLE = new Quaternionf().rotationXYZ((float) Math.toRadians(25.0), 0.0F, (float) Math.PI);
	private static final List<Identifier> EMPTY_SLOT_SMITHING_TEMPLATES = List.of(
			Identifier.withDefaultNamespace("container/slot/smithing_template_armor_trim"),
			Identifier.withDefaultNamespace("container/slot/smithing_template_netherite_upgrade"));
	private static final Component ERROR_TOOLTIP = Component.translatable("container.upgrade.error_tooltip");
	private static final Component MISSING_TEMPLATE_TOOLTIP = Component.translatable("container.upgrade.missing_template_tooltip");
	private final ArmorStandRenderState armorStandPreview;

	public SmithingUpgradeTab(SmithingUpgradeContainer upgradeContainer, Position position, StorageScreenBase<?> screen) {
		super(upgradeContainer, position, screen, SBPTranslationHelper.INSTANCE.translUpgrade("smithing"), SBPTranslationHelper.INSTANCE.translUpgradeTooltip("smithing"));
		openTabDimension = new Dimension(103, 100);

		armorStandPreview = new ArmorStandRenderState();
		armorStandPreview.entityType = EntityTypes.ARMOR_STAND;
		armorStandPreview.showBasePlate = false;
		armorStandPreview.showArms = true;
		armorStandPreview.bodyRot = 210.0F;
		armorStandPreview.xRot = 25.0F;
		updateArmorStandPreview();

		templateIcon = new CyclingSlotBackground(getContainer().getTemplateSlot().index);
		baseIcon = new CyclingSlotBackground(getContainer().getBaseSlot().index);
		additionalIcon = new CyclingSlotBackground(getContainer().getAdditionalSlot().index);

		getContainer().setOnResultChangedHandler(this::updateArmorStandPreview);
	}

	private void updateArmorStandPreview() {
		ItemStack stack = getContainer().getResultSlot().getItem();
		armorStandPreview.leftHandItemStack = ItemStack.EMPTY;
		armorStandPreview.leftHandItemState.clear();
		armorStandPreview.headEquipment = ItemStack.EMPTY;
		armorStandPreview.headItem.clear();
		armorStandPreview.chestEquipment = ItemStack.EMPTY;
		armorStandPreview.legsEquipment = ItemStack.EMPTY;
		armorStandPreview.feetEquipment = ItemStack.EMPTY;

		if (!stack.isEmpty()) {
			ItemStack previewStack = stack.copy();
			Equippable equippable = stack.get(DataComponents.EQUIPPABLE);
			EquipmentSlot slot = equippable == null ? null : equippable.slot();
			if (slot == EquipmentSlot.HEAD && !HumanoidArmorLayer.shouldRender(stack, EquipmentSlot.HEAD)) {
				minecraft.getItemModelResolver().updateForTopItem(armorStandPreview.headItem, previewStack, ItemDisplayContext.HEAD, minecraft.level, null, 0);
			} else if (slot == EquipmentSlot.HEAD) {
				armorStandPreview.headEquipment = previewStack;
			} else if (slot == EquipmentSlot.CHEST) {
				armorStandPreview.chestEquipment = previewStack;
			} else if (slot == EquipmentSlot.LEGS) {
				armorStandPreview.legsEquipment = previewStack;
			} else if (slot == EquipmentSlot.FEET) {
				armorStandPreview.feetEquipment = previewStack;
			} else {
				armorStandPreview.leftHandItemStack = previewStack;
				minecraft.getItemModelResolver().updateForTopItem(armorStandPreview.leftHandItemState, previewStack, ItemDisplayContext.THIRD_PERSON_LEFT_HAND, minecraft.level, null, 0);
			}
		}
	}

	@Override
	protected void extractBg(GuiGraphicsExtractor guiGraphics, Minecraft minecraft, int mouseX, int mouseY) {
		super.extractBg(guiGraphics, minecraft, mouseX, mouseY);

		if (getContainer().isOpen()) {
			renderSlotBg(guiGraphics, getContainer().getTemplateSlot());
			renderSlotBg(guiGraphics, getContainer().getBaseSlot());
			renderSlotBg(guiGraphics, getContainer().getAdditionalSlot());
			renderSlotBg(guiGraphics, getContainer().getResultSlot());

			templateIcon.extractRenderState(screen.getMenu(), guiGraphics, 0, screen.getLeftX(), screen.getTopY());
			baseIcon.extractRenderState(screen.getMenu(), guiGraphics, 0, screen.getLeftX(), screen.getTopY());
			additionalIcon.extractRenderState(screen.getMenu(), guiGraphics, 0, screen.getLeftX(), screen.getTopY());
		}
	}

	private void renderSlotBg(GuiGraphicsExtractor guiGraphics, Slot slot) {
		GuiHelper.renderSlotsBackground(guiGraphics, slot.x + screen.sophisticatedCore_getGuiLeft() - 1, slot.y + screen.sophisticatedCore_getGuiTop() - 1, 1, 1);
	}

	@Override
	public void extractTooltip(Screen screen, GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY) {
		super.extractTooltip(screen, guiGraphics, mouseX, mouseY);
		renderOnboardingTooltips(guiGraphics, mouseX, mouseY);
	}

	@Override
	protected void extractWidget(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
		super.extractWidget(guiGraphics, mouseX, mouseY, partialTicks);

		if (!isOpen) {
			return;
		}

		Slot resultSlot = getContainer().getResultSlot();
		int inputSlotsY = resultSlot.y + screen.sophisticatedCore_getGuiTop();
		int additionalSlotX = getContainer().getAdditionalSlot().x + screen.sophisticatedCore_getGuiLeft();
		int resultSlotX = resultSlot.x + screen.sophisticatedCore_getGuiLeft();

		int arrowX = getArrowX(additionalSlotX, resultSlotX);
		int arrowY = getArrowY(inputSlotsY);
		GuiHelper.blit(guiGraphics, arrowX, arrowY, ARROW);

		if (hasRecipeError()) {
			GuiHelper.blit(guiGraphics, arrowX, arrowY, RED_CROSS);
		}

		int centerX = getX() + getWidth() / 2;
		int bottomY = getTopY() + getHeight() - 10;
		guiGraphics.entity(armorStandPreview, 25.0F, ARMOR_STAND_TRANSLATION, ARMOR_STAND_ANGLE, null,
				centerX - 22, bottomY - 58, centerX + 22, bottomY + 2);
	}

	private int getArrowY(int inputSlotsY) {
		return inputSlotsY + 1;
	}

	private int getArrowX(int additionalSlotX, int resultSlotX) {
		return additionalSlotX + 18 + (resultSlotX - (additionalSlotX + 18)) / 2 - ARROW.getWidth() / 2 - 1;
	}

	@Override
	public void tick() {
		super.tick();
		this.templateIcon.tick(EMPTY_SLOT_SMITHING_TEMPLATES);
		ItemStack templateItem = getContainer().getTemplateSlot().getItem();
		if (templateItem.getItem() instanceof SmithingTemplateItem smithingTemplate) {
			this.baseIcon.tick(smithingTemplate.getBaseSlotEmptyIcons());
			this.additionalIcon.tick(smithingTemplate.getAdditionalSlotEmptyIcons());
		} else {
			baseIcon.tick(List.of());
			additionalIcon.tick(List.of());
		}
	}

	@Override
	protected void moveSlotsToTab() {
		Slot templateSlot = getContainer().getTemplateSlot();
		templateSlot.x = x - screen.sophisticatedCore_getGuiLeft() + 4;
		templateSlot.y = y - screen.sophisticatedCore_getGuiTop() + 1 + 24;

		Slot baseSlot = getContainer().getBaseSlot();
		baseSlot.x = templateSlot.x + 18;
		baseSlot.y = y - screen.sophisticatedCore_getGuiTop() + 1 + 24;

		Slot additionalSlot = getContainer().getAdditionalSlot();
		additionalSlot.x = baseSlot.x + 18;
		additionalSlot.y = y - screen.sophisticatedCore_getGuiTop() + 1 + 24;

		Slot resultSlot = getContainer().getResultSlot();
		resultSlot.x = x - screen.sophisticatedCore_getGuiLeft() + getWidth() - 2 - 3 - 18;
		resultSlot.y = y - screen.sophisticatedCore_getGuiTop() + 1 + 24;
	}

	private boolean isHoveringRedCross(int mouseX, int mouseY) {
		Slot additionalSlot = getContainer().getAdditionalSlot();
		int arrowX = getArrowX(additionalSlot.x + screen.sophisticatedCore_getGuiLeft(), getContainer().getResultSlot().x + screen.sophisticatedCore_getGuiLeft());
		int arrowY = getArrowY(additionalSlot.y + screen.sophisticatedCore_getGuiTop());
		return mouseX >= arrowX && mouseX < arrowX + RED_CROSS.getWidth() && mouseY >= arrowY && mouseY < arrowY + RED_CROSS.getHeight();
	}

	private boolean isHoveringEmptySlot(Slot slot, int mouseX, int mouseY) {
		return mouseX >= slot.x + screen.sophisticatedCore_getGuiLeft() && mouseX < slot.x + screen.sophisticatedCore_getGuiLeft() + 16 && mouseY >= slot.y + screen.sophisticatedCore_getGuiTop() && mouseY < slot.y + screen.sophisticatedCore_getGuiTop() + 16 && slot.getItem().isEmpty();
	}

	private void renderOnboardingTooltips(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY) {
		if (this.hasRecipeError() && isHoveringRedCross(mouseX, mouseY)) {
			Component tooltip = ERROR_TOOLTIP;
			renderOnboardingTooltip(guiGraphics, mouseX, mouseY, tooltip);
		} else {
			if (isHoveringEmptySlot(getContainer().getTemplateSlot(), mouseX, mouseY)) {
				renderOnboardingTooltip(guiGraphics, mouseX, mouseY, MISSING_TEMPLATE_TOOLTIP);
			} else if (getContainer().getTemplateSlot().getItem().getItem() instanceof SmithingTemplateItem smithingTemplate) {

				if (isHoveringEmptySlot(getContainer().getBaseSlot(), mouseX, mouseY)) {
					renderOnboardingTooltip(guiGraphics, mouseX, mouseY, smithingTemplate.getBaseSlotDescription());
				} else if (isHoveringEmptySlot(getContainer().getAdditionalSlot(), mouseX, mouseY)) {
					renderOnboardingTooltip(guiGraphics, mouseX, mouseY, smithingTemplate.getAdditionSlotDescription());
				}
			}
		}
	}

	private void renderOnboardingTooltip(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, Component tooltip) {
		guiGraphics.setTooltipForNextFrame(font, font.split(tooltip, 115), mouseX, mouseY, null);
	}

	private boolean hasRecipeError() {
		return getContainer().getTemplateSlot().hasItem() && getContainer().getBaseSlot().hasItem() && getContainer().getAdditionalSlot().hasItem() && !getContainer().getResultSlot().hasItem();
	}

}
