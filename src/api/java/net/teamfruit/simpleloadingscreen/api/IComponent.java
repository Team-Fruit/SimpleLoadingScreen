package net.teamfruit.simpleloadingscreen.api;

import java.io.File;
import java.util.List;

public interface IComponent {
	String getID();

	IModule getModule();

	File getWorkspace();

	void registerRenderer(IRenderer renderer);

	List<IRenderer> getRenderers();

	IConfig getConfig(File configFile);

	IConfig config(File configFile);

	IModule getAuthorModule();

	IBlackboard getBlackboard();

	IBlackboard getGlobalBlackboard();
}
