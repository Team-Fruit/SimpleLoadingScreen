package net.teamfruit.simpleloadingscreen.gui;

import java.io.File;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

import net.teamfruit.simpleloadingscreen.api.IComponent;
import net.teamfruit.simpleloadingscreen.api.IConfig;
import net.teamfruit.simpleloadingscreen.api.IManager;
import net.teamfruit.simpleloadingscreen.api.IModule;
import net.teamfruit.simpleloadingscreen.modules.ModuleContainer;
import net.teamfruit.simpleloadingscreen.resources.ScreenConfig;
import net.teamfruit.simpleloadingscreen.splash.LoadingScreen;

public class ScreenManager implements IManager {
	private final LoadingScreen loadingScreen;
	private final ModuleContainer module;
	private final List<IComponent> components = Lists.newArrayList();

	public ScreenManager(final LoadingScreen loadingScreen, final ModuleContainer module) {
		this.loadingScreen = loadingScreen;
		this.module = module;
	}

	public LoadingScreen getLoadingScreen() {
		return this.loadingScreen;
	}

	@Override
	public IComponent createComponent(final String id) {
		for (final ScreenComponent component : this.loadingScreen.components)
			if (StringUtils.equalsIgnoreCase(id, component.getID()))
				if (component.complete(this.module, null))
					return component;
				else
					throw new IllegalArgumentException("id '+id+' has been already created by this module.");
		final ScreenComponent component = ScreenComponent.createComponent(this.loadingScreen, id, this.module);
		this.loadingScreen.components.add(component);
		return component;
	}

	@Override
	public IComponent createComponent(final String sourceId, final String id) {
		for (final ScreenComponent component : this.loadingScreen.components)
			if (StringUtils.equalsIgnoreCase(id, component.getID()))
				throw new IllegalArgumentException("id '+id+' has been already created.");
		ScreenComponent sourceComponent = null;
		for (final ScreenComponent component : this.loadingScreen.components)
			if (StringUtils.equalsIgnoreCase(sourceId, component.getID()))
				sourceComponent = component;
		if (sourceComponent==null) {
			sourceComponent = ScreenComponent.createUncompletedComponent(this.loadingScreen, sourceId);
			this.loadingScreen.components.add(sourceComponent);
		}
		return sourceComponent.copy(id, this.module);
	}

	@Override
	public List<IComponent> getRenderingComponents() {
		return this.components;
	}

	@Override
	public IModule getModule(final String id) {
		return this.module.getModule();
	}

	@Override
	public File getWorkspace() {
		final File workspace = new File(this.loadingScreen.directories.configDir, this.module.getModule().getName());
		workspace.mkdirs();
		return workspace;
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
}
