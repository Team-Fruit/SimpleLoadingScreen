package net.teamfruit.simpleloadingscreen.api;

import java.io.File;
import java.util.List;
import java.util.Map;

import net.teamfruit.simpleloadingscreen.api.renderer.IRenderer;

public interface IComponent {
	String getID();

	IModule getModule();

	File getWorkspace();

	List<IRenderer> getRenderers();

	IModule getAuthorModule();

	Map<String, Object> getBlackboard();

	List<IPropertyMapper> getPropertyMappers();
}
