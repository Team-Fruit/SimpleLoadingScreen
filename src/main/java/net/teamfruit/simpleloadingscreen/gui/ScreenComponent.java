package net.teamfruit.simpleloadingscreen.gui;

import java.io.File;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.teamfruit.simpleloadingscreen.api.IComponent;
import net.teamfruit.simpleloadingscreen.api.IModule;
import net.teamfruit.simpleloadingscreen.api.IPropertyMapper;
import net.teamfruit.simpleloadingscreen.api.renderer.IRenderer;
import net.teamfruit.simpleloadingscreen.modules.ModuleContainer;
import net.teamfruit.simpleloadingscreen.splash.LoadingScreen;

public class ScreenComponent implements IComponent {
	private final LoadingScreen loadingScreen;
	private final String id;
	private final ModuleContainer module;
	private final ModuleContainer authormodule;
	private final Map<String, Object> blackboard;
	private final List<IRenderer> renderers;
	private final List<IPropertyMapper> mappers;

	private ScreenComponent(final LoadingScreen loadingScreen, final String id, final ModuleContainer module, final ModuleContainer authormodule, final Map<String, Object> blackboard, final List<IRenderer> renderers, final List<IPropertyMapper> mappers) {
		this.loadingScreen = loadingScreen;
		this.id = id;
		this.module = module;
		this.authormodule = authormodule;
		this.blackboard = blackboard;
		this.renderers = renderers;
		this.mappers = mappers;
	}

	public ScreenComponent(final LoadingScreen loadingScreen, final String id, final ModuleContainer module) {
		this(loadingScreen, id, module, module, Maps.newHashMap(), Lists.newArrayList(), Lists.newArrayList());
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
	public Map<String, Object> getBlackboard() {
		return this.blackboard;
	}

	@Override
	public List<IRenderer> getRenderers() {
		return this.renderers;
	}

	@Override
	public List<IPropertyMapper> getPropertyMappers() {
		return this.mappers;
	}

	public ScreenComponent copy(final String newid, final ModuleContainer newmodule) {
		final ScreenComponent component = new ScreenComponent(this.loadingScreen, newid, newmodule, this.authormodule, Maps.newHashMap(this.blackboard), Lists.newArrayList(this.renderers), Lists.newArrayList(this.mappers));
		return component;
	}
}
