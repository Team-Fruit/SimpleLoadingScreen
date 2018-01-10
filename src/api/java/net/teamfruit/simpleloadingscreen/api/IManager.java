package net.teamfruit.simpleloadingscreen.api;

import java.io.File;

public interface IManager {
	IComponent createComponent(String id);

	IComponent getComponent(String sourceId, String id);

	IModule getModule(String id);

	IBlackboard getBlackboard();

	IBlackboard getGlobalBlackboard();

	void registerComponent(IComponent component);

	IConfig config(File configFile);

	File getWorkspace();

	IConfig getConfig(File configFile);

	void setArea(Area area);
}
