package net.teamfruit.simpleloadingscreen.gui;

import java.io.File;
import java.util.List;

import com.google.common.collect.Lists;

import net.teamfruit.simpleloadingscreen.api.IComponent;
import net.teamfruit.simpleloadingscreen.api.IConfig;
import net.teamfruit.simpleloadingscreen.api.IConfigMapper;
import net.teamfruit.simpleloadingscreen.api.IModule;
import net.teamfruit.simpleloadingscreen.api.IRenderer;
import net.teamfruit.simpleloadingscreen.modules.ModuleContainer;
import net.teamfruit.simpleloadingscreen.resources.ScreenConfig;
import net.teamfruit.simpleloadingscreen.splash.LoadingScreen;

public class ScreenComponent implements IComponent {
	private final LoadingScreen loadingScreen;
	private final String id;
	private final ModuleContainer module;
	private final ModuleContainer authormodule;
	private final List<IRenderer> renderers;
	private final List<IConfigMapper> mappers;

	private ScreenComponent(final LoadingScreen loadingScreen, final String id, final ModuleContainer module, final ModuleContainer authormodule, final List<IRenderer> renderers, final List<IConfigMapper> mappers) {
		this.loadingScreen = loadingScreen;
		this.id = id;
		this.module = module;
		this.authormodule = authormodule;
		this.renderers = renderers;
		this.mappers = mappers;
	}

	public ScreenComponent(final LoadingScreen loadingScreen, final String id, final ModuleContainer module) {
		this(loadingScreen, id, module, module, Lists.newArrayList(), Lists.newArrayList());
	}

	@Override
	public String getID() {
		return this.id;
	}

	@Override
	public IModule getModule() {
		return this.module.getModule();
	}

	@Override
	public IModule getAuthorModule() {
		return this.authormodule.getModule();
	}

	@Override
	public File getWorkspace() {
		final File workspace = new File(this.loadingScreen.loadingScreenModuleConfigDir, this.module.getModule().getName());
		final File componentWorkspace = new File(workspace, getID());
		componentWorkspace.mkdirs();
		return componentWorkspace;
	}

	private IConfig config;

	@Override
	public IConfig getConfig() {
		if (this.config==null)
			this.config = loadConfig(new File(getWorkspace(), "config.properties"));
		return this.config;
	}

	@Override
	public IConfig loadConfig(final File configFile) {
		final ScreenConfig config = new ScreenConfig(configFile);
		config.load();
		return config;
	}

	@Override
	public ScreenBlackboard getBlackboard() {
		return new ScreenBlackboard(this.loadingScreen.blackboard, this.module.getModule().getID(), this.id);
	}

	@Override
	public ScreenBlackboard getGlobalBlackboard() {
		return this.loadingScreen.globalBlackboard;
	}

	@Override
	public List<IRenderer> getRenderers() {
		return this.renderers;
	}

	@Override
	public List<IConfigMapper> getConfigMappers() {
		return this.mappers;
	}

	public ScreenComponent copy(final String newid, final ModuleContainer newmodule) {
		final ScreenComponent component = new ScreenComponent(this.loadingScreen, newid, newmodule, this.authormodule, Lists.newArrayList(this.renderers), Lists.newArrayList(this.mappers));
		ScreenBlackboard.copy(getBlackboard(), component.getBlackboard());
		return component;
	}
}
