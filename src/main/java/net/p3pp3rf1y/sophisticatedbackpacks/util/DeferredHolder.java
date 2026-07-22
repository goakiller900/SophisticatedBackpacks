package net.p3pp3rf1y.sophisticatedbackpacks.util;

import net.p3pp3rf1y.sophisticatedcore.util.RegistrySupplier;

import java.util.function.Supplier;

/**
 * Backpacks' typed registry handle, backed by Core's 26.2 registry supplier.
 * Keeping the value type preserves the public item declarations while using
 * the new registration lifecycle.
 */
public final class DeferredHolder<T, I extends T> implements Supplier<I> {
	private final RegistrySupplier<I> delegate;

	DeferredHolder(RegistrySupplier<I> delegate) {
		this.delegate = delegate;
	}

	@Override
	public I get() {
		return delegate.get();
	}

	public net.minecraft.resources.Identifier getId() {
		return delegate.getKey().identifier();
	}
}
