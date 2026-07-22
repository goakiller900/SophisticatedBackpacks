package net.p3pp3rf1y.sophisticatedbackpacks.client.render;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.p3pp3rf1y.sophisticatedcore.init.ModParticles;
import net.p3pp3rf1y.sophisticatedcore.renderdata.IUpgradeRenderData;
import net.p3pp3rf1y.sophisticatedcore.upgrades.cooking.CookingUpgradeRenderData;
import net.p3pp3rf1y.sophisticatedcore.upgrades.jukebox.JukeboxUpgradeRenderData;
import org.joml.Vector3f;

import java.util.function.UnaryOperator;

/**
 * The old Core upgrade-renderer registry was removed in 26.2.  These effects
 * are the two Core-defined render-data behaviours and are kept local to the
 * backpack presentation layer rather than reviving the removed registry API.
 */
public final class BackpackUpgradeEffects {
	private BackpackUpgradeEffects() {
	}

	public static void render(Level level, RandomSource random, UnaryOperator<Vector3f> position, IUpgradeRenderData data) {
		if (data instanceof CookingUpgradeRenderData cooking && cooking.isBurning()) {
			renderCooking(level, position);
		} else if (data instanceof JukeboxUpgradeRenderData jukebox && jukebox.isPlaying() && random.nextInt(2) == 0) {
			renderJukebox(level, random, position);
		}
	}

	private static void renderCooking(Level level, UnaryOperator<Vector3f> position) {
		if (level.getRandom().nextDouble() < 0.1D) {
			Vector3f center = position.apply(new Vector3f());
			level.playLocalSound(center.x(), center.y(), center.z(), SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1.0F, 1.0F, false);
		}
		Vector3f particle = position.apply(new Vector3f(level.getRandom().nextFloat() * 0.6F - 0.3F, level.getRandom().nextFloat() * 6.0F / 16.0F, 0.02F));
		level.addParticle(ParticleTypes.SMOKE, particle.x(), particle.y(), particle.z(), 0.0D, 0.0D, 0.0D);
		level.addParticle(ParticleTypes.FLAME, particle.x(), particle.y(), particle.z(), 0.0D, 0.0D, 0.0D);
	}

	private static void renderJukebox(Level level, RandomSource random, UnaryOperator<Vector3f> position) {
		Vector3f particle = position.apply(new Vector3f(level.getRandom().nextFloat() * 0.6F - 0.3F, 0.5F + level.getRandom().nextFloat() * 6.0F / 16.0F,
				level.getRandom().nextFloat() * 0.6F - 0.1F));
		level.addParticle(ModParticles.JUKEBOX_NOTE.get(), particle.x(), particle.y(), particle.z(), random.nextFloat(), 0.0D, 0.0D);
	}
}
