package net.teamfruit.simpleloadingscreen.api;

import java.io.File;
import java.util.List;

public interface IManager {
	IComponent createComponent(String id);

	IComponent createComponent(String sourceId, String id);

	IModule getModule(String id);

	IConfig loadConfig(File configFile);

	File getWorkspace();

	IConfig getConfig();

	List<IComponent> getRenderingComponents();
}
