package net.teamfruit.simpleloadingscreen.api;

import java.io.File;
import java.util.Properties;
import java.util.Set;

public interface IConfig {

	IConfigProperty<Integer> hexProperty(final String name, final int def);

	IConfigProperty<Integer> intProperty(final String name, final int def);

	IConfigProperty<Boolean> booleanProperty(final String name, final boolean def);

	IConfigProperty<String> stringProperty(final String name, final String def);

	Properties getProperties();

	File getLocation();

	void fillDefaults();

	void save();

	void load();

	Set<IConfigProperty<?>> getConfigs();

}