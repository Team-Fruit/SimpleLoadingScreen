package net.teamfruit.simpleloadingscreen.modules;

import net.teamfruit.simpleloadingscreen.api.IModule;
import net.teamfruit.simpleloadingscreen.gui.ScreenManager;
import net.teamfruit.simpleloadingscreen.splash.LoadingScreen;

public class ModuleContainer {
	private final LoadingScreen loadingScreen;
	private final IModule module;
	private final ScreenManager manager;

	public ModuleContainer(final LoadingScreen loadingScreen, final IModule module) {
		this.loadingScreen = loadingScreen;
		this.module = module;
		this.manager = new ScreenManager(this.loadingScreen, this);
	}

	public IModule getModule() {
		return this.module;
	}

	public ScreenManager getManager() {
		return this.manager;
	}
}
