package net.teamfruit.simpleloadingscreen.modules;

import net.teamfruit.simpleloadingscreen.api.IBlackboard;
import net.teamfruit.simpleloadingscreen.api.IConfig;
import net.teamfruit.simpleloadingscreen.api.IConfigMapper;
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

	public boolean enable() {
		if (this.module.enable(this.manager)) {
			final IConfig config = this.manager.getConfig();
			final IBlackboard blackboard = this.manager.getBlackboard();
			for (final IConfigMapper mapper : this.manager.getConfigMappers())
				mapper.map(config, blackboard);
			if (!config.getConfigs().isEmpty()) {
				config.fillDefaults();
				config.save();
			}
			return true;
		}
		return false;
	}
}
