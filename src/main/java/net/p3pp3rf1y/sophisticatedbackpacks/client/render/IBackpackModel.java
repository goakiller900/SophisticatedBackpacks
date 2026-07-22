package net.p3pp3rf1y.sophisticatedbackpacks.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.p3pp3rf1y.sophisticatedcore.fluid.FluidStack;
import net.p3pp3rf1y.sophisticatedcore.renderdata.RenderInfo;

public interface IBackpackModel {
	void submit(PoseStack poseStack, SubmitNodeCollector collector, int packedLight, int clothColor, int borderColor, Item backpackItem, RenderInfo renderInfo);

	void submitBatteryCharge(PoseStack matrixStack, SubmitNodeCollector collector, int packedLight, float chargeRatio);

	void submitFluid(PoseStack matrixStack, SubmitNodeCollector collector, int packedLight, FluidStack fluid, float fill, boolean left);

	EquipmentSlot getRenderEquipmentSlot();

	void translateRotateAndScale(EntityModel<?> parentModel, LivingEntity livingEntity, PoseStack matrixStack, boolean wearsArmor);
}
