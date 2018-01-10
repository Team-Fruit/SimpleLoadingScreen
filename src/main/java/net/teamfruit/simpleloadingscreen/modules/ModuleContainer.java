package net.teamfruit.simpleloadingscreen.modules;

import java.util.List;

import com.google.common.collect.Lists;

import net.teamfruit.simpleloadingscreen.api.IComponent;
import net.teamfruit.simpleloadingscreen.api.IModule;

public class ModuleContainer {
	private final IModule module;
	private final List<IComponent> components = Lists.newArrayList();

	public ModuleContainer(final IModule module) {
		this.module = module;
	}

	public IModule getModule() {
		return this.module;
	}

	public List<IComponent> getComponents() {
		return this.components;
	}

	public void registerComponent(final IComponent component) {
		this.components.add(component);
	}
}
