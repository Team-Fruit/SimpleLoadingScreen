package net.teamfruit.simpleloadingscreen.api;

import java.io.File;
import java.util.List;

import net.teamfruit.simpleloadingscreen.api.position.RelativeArea;

public interface IManager {
	IComponent createComponent(String id);

	IComponent createComponent(String sourceId, String id);

	IModule getModule(String id);

	IBlackboard getBlackboard();

	IBlackboard getGlobalBlackboard();

	IConfig loadConfig(File configFile);

	File getWorkspace();

	IConfig getConfig();

	void setArea(RelativeArea area);

	List<IComponent> getRenderingComponents();

	List<IConfigMapper> getConfigMappers();

}
