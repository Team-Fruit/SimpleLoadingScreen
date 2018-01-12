package net.teamfruit.simpleloadingscreen.gui;

import java.io.File;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

import net.teamfruit.simpleloadingscreen.api.IBlackboard;
import net.teamfruit.simpleloadingscreen.api.IComponent;
import net.teamfruit.simpleloadingscreen.api.IConfig;
import net.teamfruit.simpleloadingscreen.api.IConfigMapper;
import net.teamfruit.simpleloadingscreen.api.IManager;
import net.teamfruit.simpleloadingscreen.api.IModule;
import net.teamfruit.simpleloadingscreen.api.position.Area;
import net.teamfruit.simpleloadingscreen.modules.ModuleContainer;
import net.teamfruit.simpleloadingscreen.resources.AreaConfigMapper;
import net.teamfruit.simpleloadingscreen.resources.ScreenConfig;
import net.teamfruit.simpleloadingscreen.splash.LoadingScreen;

public class ScreenManager implements IManager {
	private final LoadingScreen loadingScreen;
	private final ModuleContainer module;
	private final List<IComponent> components = Lists.newArrayList();
	private final List<IConfigMapper> mappers = Lists.newArrayList(AreaConfigMapper.instance);

	public ScreenManager(final LoadingScreen loadingScreen, final ModuleContainer module) {
		this.loadingScreen = loadingScreen;
		this.module = module;
	}

	@Override
	public IComponent createComponent(final String id) {
		for (final ScreenComponent component : this.loadingScreen.components)
			if (StringUtils.equalsIgnoreCase(this.module.getModule().getID(), component.getModule().getID())&&StringUtils.equalsIgnoreCase(id, component.getID()))
				throw new IllegalArgumentException("id '+id+' has been already created by this module.");
		final ScreenComponent component = new ScreenComponent(this.loadingScreen, id, this.module);
		this.loadingScreen.components.add(component);
		return component;
	}

	@Override
	public IComponent createComponent(String sourceId, final String id) {
		for (final ScreenComponent component : this.loadingScreen.components)
			if (StringUtils.equalsIgnoreCase(this.module.getModule().getID(), component.getModule().getID())&&StringUtils.equalsIgnoreCase(id, component.getID()))
				throw new IllegalArgumentException("id '+id+' has been already created by this module.");
		String moduleid = null;
		{
			final String sep = ":";
			final int col = sourceId.indexOf(sep);
			if (col!=-1) {
				moduleid = sourceId.substring(0, col);
				sourceId = sourceId.substring(col+sep.length());
			}
		}
		for (final ScreenComponent component : this.loadingScreen.components)
			if (StringUtils.equalsIgnoreCase(moduleid!=null ? moduleid : this.module.getModule().getID(), component.getModule().getID())&&StringUtils.equalsIgnoreCase(sourceId, component.getID()))
				return component.copy(id, this.module);
		for (final ScreenComponent component : this.loadingScreen.components)
			if (StringUtils.equalsIgnoreCase(sourceId, component.getID()))
				return component.copy(id, this.module);
		return null;
	}

	@Override
	public List<IComponent> getRenderingComponents() {
		return this.components;
	}

	@Override
	public void setArea(final Area area) {
		getBlackboard().setValue("area", area);
	}

	@Override
	public IModule getModule(final String id) {
		return this.module.getModule();
	}

	@Override
	public IBlackboard getBlackboard() {
		return new ScreenBlackboard(this.loadingScreen.blackboard, this.module.getModule().getID());
	}

	@Override
	public IBlackboard getGlobalBlackboard() {
		return this.loadingScreen.globalBlackboard;
	}

	@Override
	public File getWorkspace() {
		final File workspace = new File(this.loadingScreen.loadingScreenModuleConfigDir, this.module.getModule().getName());
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

	@Override
	public List<IConfigMapper> getConfigMappers() {
		return this.mappers;
	}
}
