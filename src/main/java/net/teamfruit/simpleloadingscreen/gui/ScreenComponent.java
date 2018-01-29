package net.teamfruit.simpleloadingscreen.gui;

import java.io.File;
import java.util.Collections;
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
	private ScreenComponent source;
	private final LoadingScreen loadingScreen;
	private final String id;
	private ModuleContainer module;
	private final Map<String, Object> blackboard = Maps.newHashMap();
	private final List<IRenderer> renderers = Lists.newArrayList();
	private final List<IPropertyMapper> mappers = Lists.newArrayList();

	private ScreenComponent(final LoadingScreen loadingScreen, final String id, final ModuleContainer module, final ScreenComponent source) {
		this.loadingScreen = loadingScreen;
		this.id = id;
		this.module = module;
		this.source = source;
	}

	public static ScreenComponent createComponent(final LoadingScreen loadingScreen, final String id, final ModuleContainer module) {
		return new ScreenComponent(loadingScreen, id, module, null);
	}

	public static ScreenComponent createUncompletedComponent(final LoadingScreen loadingScreen, final String id) {
		return new ScreenComponent(loadingScreen, id, null, null);
	}

	public boolean complete(final ModuleContainer module, final ScreenComponent source) {
		if (this.module==null) {
			this.module = module;
			this.source = source;
			return true;
		}
		return false;
	}

	@Override
	public String getID() {
		return this.id;
	}

	@Override
	public IModule getModule() {
		if (this.module==null)
			throw new IllegalStateException("This component has not been completed.");
		return this.module.getModule();
	}

	@Override
	public IModule getAuthorModule() {
		final ScreenComponent parent = getSource();
		if (parent!=null)
			return parent.getAuthorModule();
		return getModule();
	}

	@Override
	public File getWorkspace() {
		final File workspace = new File(this.loadingScreen.directories.configDir, this.module.getModule().getName());
		final File componentWorkspace = new File(workspace, getID());
		componentWorkspace.mkdirs();
		return componentWorkspace;
	}

	@Override
	public ScreenComponent getSource() {
		if (this.source!=null&&this.source.module==null)
			throw new IllegalStateException("This component tree has not been completed yet.");
		return this.source;
	}

	@Override
	public Map<String, Object> getCurrentBlackboard() {
		return this.blackboard;
	}

	@Override
	public List<IRenderer> getCurrentRenderers() {
		return this.renderers;
	}

	@Override
	public List<IPropertyMapper> getCurrentPropertyMappers() {
		return this.mappers;
	}

	@Override
	public Map<String, Object> getBlackboard() {
		final Map<String, Object> output = Maps.newHashMap();
		getBlackboard(output);
		return Collections.unmodifiableMap(output);
	}

	@Override
	public List<IRenderer> getRenderers() {
		final List<IRenderer> output = Lists.newArrayList();
		getRenderers(output);
		return Collections.unmodifiableList(output);
	}

	@Override
	public List<IPropertyMapper> getPropertyMappers() {
		final List<IPropertyMapper> output = Lists.newArrayList();
		getPropertyMappers(output);
		return Collections.unmodifiableList(output);
	}

	private void getBlackboard(final Map<String, Object> output) {
		final ScreenComponent source = getSource();
		if (source!=null)
			source.getBlackboard(output);
		output.putAll(getCurrentBlackboard());
	}

	private void getRenderers(final List<IRenderer> output) {
		final ScreenComponent source = getSource();
		if (source!=null)
			source.getRenderers(output);
		output.addAll(getCurrentRenderers());
	}

	private void getPropertyMappers(final List<IPropertyMapper> output) {
		final ScreenComponent source = getSource();
		if (source!=null)
			source.getPropertyMappers(output);
		output.addAll(getCurrentPropertyMappers());
	}

	public ScreenComponent copy(final String newid, final ModuleContainer newmodule) {
		return new ScreenComponent(this.loadingScreen, newid, newmodule, this);
	}
}
