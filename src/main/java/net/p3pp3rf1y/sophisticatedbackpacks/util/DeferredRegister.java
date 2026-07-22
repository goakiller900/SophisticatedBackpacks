package net.p3pp3rf1y.sophisticatedbackpacks.util;

import net.minecraft.core.Registry;
import net.p3pp3rf1y.sophisticatedcore.util.RegistrySupplier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/** Adapts Backpacks' typed registrations to Core's 26.2 registry helper. */
public final class DeferredRegister<T> {
	private final net.p3pp3rf1y.sophisticatedcore.util.DeferredRegister<T> delegate;
	private final List<DeferredHolder<T, ? extends T>> entries = new ArrayList<>();

	private DeferredRegister(Registry<T> registry, String namespace) {
		delegate = net.p3pp3rf1y.sophisticatedcore.util.DeferredRegister.create(registry, namespace);
	}

	public static <T> DeferredRegister<T> create(Registry<T> registry, String namespace) {
		return new DeferredRegister<>(registry, namespace);
	}

	public <I extends T> DeferredHolder<T, I> register(String name, Supplier<? extends I> factory) {
		RegistrySupplier<I> entry = delegate.register(name, factory);
		DeferredHolder<T, I> holder = new DeferredHolder<>(entry);
		entries.add(holder);
		return holder;
	}

	public List<DeferredHolder<T, ? extends T>> getEntries() {
		return List.copyOf(entries);
	}

	public void register() {
		delegate.register();
	}
}
