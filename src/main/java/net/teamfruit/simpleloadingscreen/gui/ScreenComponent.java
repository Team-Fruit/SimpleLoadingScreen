package net.teamfruit.simpleloadingscreen.gui;

import java.io.File;
import java.util.List;

import com.google.common.collect.Lists;

import net.teamfruit.simpleloadingscreen.api.IComponent;
import net.teamfruit.simpleloadingscreen.api.IConfig;
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
	private final List<IRenderer> renderers = Lists.newArrayList();

	private ScreenComponent(final LoadingScreen loadingScreen, final String id, final ModuleContainer module, final ModuleContainer authormodule) {
		this.loadingScreen = loadingScreen;
		this.id = id;
		this.module = module;
		this.authormodule = authormodule;
	}

	public ScreenComponent(final LoadingScreen loadingScreen, final String id, final ModuleContainer module) {
		this(loadingScreen, id, module, module);
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

	@Override
	public IConfig getConfig(final File configFile) {
		return config(new File(getWorkspace(), "config.properties"));
	}

	@Override
	public IConfig config(final File configFile) {
		return new ScreenConfig(configFile);
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
	public void registerRenderer(final IRenderer renderer) {
		this.renderers.add(renderer);
	}

	@Override
	public List<IRenderer> getRenderers() {
		return this.renderers;
	}

	public ScreenComponent copy(final String newid, final ModuleContainer newmodule) {
		final ScreenComponent component = new ScreenComponent(this.loadingScreen, newid, newmodule, this.authormodule);
		ScreenBlackboard.copy(getBlackboard(), component.getBlackboard());
		return component;
	}
}
