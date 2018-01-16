package net.teamfruit.simpleloadingscreen.api;

import java.io.File;
import java.util.List;

public interface IComponent {
	String getID();

	IModule getModule();

	File getWorkspace();

	List<IRenderer> getRenderers();

	IModule getAuthorModule();

	IBlackboard getBlackboard();

	IBlackboard getGlobalBlackboard();

	List<IConfigMapper> getConfigMappers();
}
