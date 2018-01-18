package net.teamfruit.simpleloadingscreen.api;

import java.io.File;
import java.util.List;
import java.util.Map;

import net.teamfruit.simpleloadingscreen.api.renderer.IRenderer;

public interface IComponent {
	String getID();

	IModule getModule();

	File getWorkspace();

	IModule getAuthorModule();

	IComponent getSource();

	List<IRenderer> getCurrentRenderers();

	Map<String, Object> getCurrentBlackboard();

	List<IPropertyMapper> getCurrentPropertyMappers();

	List<IRenderer> getRenderers();

	Map<String, Object> getBlackboard();

	List<IPropertyMapper> getPropertyMappers();
}
