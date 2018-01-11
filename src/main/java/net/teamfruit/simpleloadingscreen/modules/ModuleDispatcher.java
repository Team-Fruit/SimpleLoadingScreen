package net.teamfruit.simpleloadingscreen.modules;

import java.util.List;
import java.util.function.Consumer;

import com.google.common.collect.Lists;

import net.teamfruit.simpleloadingscreen.Log;
import net.teamfruit.simpleloadingscreen.api.IComponent;
import net.teamfruit.simpleloadingscreen.api.IRenderer;
import net.teamfruit.simpleloadingscreen.api.IScreen;
import net.teamfruit.simpleloadingscreen.gui.Screen;
import net.teamfruit.simpleloadingscreen.splash.LoadingScreen;

public class ModuleDispatcher {
	private final LoadingScreen loadingScreen;
	private final List<ModuleContainer> listenersRegistry = Lists.newArrayList();

	public ModuleDispatcher(final LoadingScreen loadingScreen) {
		this.loadingScreen = loadingScreen;
	}

	public void register(final ModuleContainer module) {
		this.listenersRegistry.add(module);
		Log.log.trace("Registered module {}", module.getClass().getName());
	}

	public void unregister(final ModuleContainer module) {
		this.listenersRegistry.remove(module);
		Log.log.trace("Unregistered module {}", module.getClass().getName());
	}

	public List<ModuleContainer> getRegistry() {
		return this.listenersRegistry;
	}

	/**
	 * Dispatches an event.
	 *
	 * @param event The event.
	 */
	public void dispatchInit() {
		for (final ModuleContainer moduleContainer : this.listenersRegistry)
			for (final IComponent component : moduleContainer.getManager().getRenderingComponents()) {
				final IScreen screen = new Screen(this.loadingScreen, component);
				for (final IRenderer listener : component.getRenderers())
					listener.init(screen);
			}
	}

	/**
	 * Dispatches an event.
	 *
	 * @param event The event.
	 */
	public void dispatchDraw(final Consumer<IComponent> before, final Consumer<IComponent> after) {
		for (final ModuleContainer moduleContainer : this.listenersRegistry)
			for (final IComponent component : moduleContainer.getManager().getRenderingComponents()) {
				final IScreen screen = new Screen(this.loadingScreen, component);
				before.accept(component);
				for (final IRenderer listener : component.getRenderers())
					listener.draw(screen);
				after.accept(component);
			}
	}

	/**
	 * Dispatches an event.
	 *
	 * @param event The event.
	 */
	public void dispatchFinish() {
		for (final ModuleContainer moduleContainer : this.listenersRegistry)
			for (final IComponent component : moduleContainer.getManager().getRenderingComponents()) {
				final IScreen screen = new Screen(this.loadingScreen, component);
				for (final IRenderer listener : component.getRenderers())
					listener.finish(screen);
			}
	}
}
