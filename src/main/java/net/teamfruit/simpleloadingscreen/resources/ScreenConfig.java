package net.teamfruit.simpleloadingscreen.resources;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;

import org.apache.logging.log4j.Level;

import com.google.common.collect.Sets;

import cpw.mods.fml.common.FMLLog;
import net.teamfruit.simpleloadingscreen.api.IConfig;
import net.teamfruit.simpleloadingscreen.api.IConfigProperty;

public class ScreenConfig implements IConfig {
	private final File configFile;
	private final Properties properties;
	private final Set<ConfigProperty<?>> configProperties = Sets.newHashSet();
	private boolean changeToSave;

	public ScreenConfig(final File configFile, final Properties properties) {
		this.configFile = configFile;
		this.properties = properties;
	}

	public ScreenConfig(final File configFile) {
		this(configFile, new Properties());
	}

	@Override
	public void load() {
		try (FileReader r = new FileReader(this.configFile)) {
			this.properties.load(r);
		} catch (final IOException e) {
			FMLLog.info("Could not load "+this.configFile.getName()+", will create a default one");
		}
	}

	@Override
	public void save() {
		try (FileWriter w = new FileWriter(this.configFile);) {
			this.properties.store(w, "Splash screen properties");
		} catch (final IOException e) {
			FMLLog.log(Level.ERROR, e, "Could not save the "+this.configFile.getName()+" file");
		}
	}

	@Override
	public void fillDefaults() {
		for (final IConfigProperty<?> cfgProp : this.configProperties)
			cfgProp.fillDefault();
	}

	private <T> IConfigProperty<T> register(final ConfigProperty<T> property) {
		this.configProperties.add(property);
		return property;
	}

	private void onChanged() {
		if (this.changeToSave)
			save();
	}

	@Override
	public void setEnableChangeToSave(final boolean enable) {
		this.changeToSave = enable;
	}

	@Override
	public File getLocation() {
		return this.configFile;
	}

	@Override
	public Properties getProperties() {
		return this.properties;
	}

	@Override
	public IConfigProperty<String> stringProperty(final String name, final String def) {
		return register(new ConfigProperty<String>(this, name, def) {
			@Override
			public String get() {
				return getProperty(this.def);
			}

			@Override
			public void set(final String value) {
				setProperty(value);
			}
		});
	}

	@Override
	public IConfigProperty<Boolean> booleanProperty(final String name, final boolean def) {
		return register(new ConfigProperty<Boolean>(this, name, def) {
			@Override
			public Boolean get() {
				return Boolean.parseBoolean(getProperty(Boolean.toString(this.def)));
			}

			@Override
			public void set(final Boolean value) {
				setProperty(Boolean.toString(value));
			}
		});
	}

	@Override
	public IConfigProperty<Integer> intProperty(final String name, final int def) {
		return register(new ConfigProperty<Integer>(this, name, def) {
			@Override
			public Integer get() {
				return Integer.decode(getProperty(Integer.toString(this.def)));
			}

			@Override
			public void set(final Integer value) {
				setProperty(Integer.toString(value));
			}
		});
	}

	@Override
	public IConfigProperty<Integer> hexProperty(final String name, final int def) {
		return register(new ConfigProperty<Integer>(this, name, def) {
			@Override
			public Integer get() {
				return Integer.decode(getProperty("0x"+Integer.toString(this.def, 16).toUpperCase()));
			}

			@Override
			public void set(final Integer value) {
				setProperty("0x"+Integer.toString(value, 16).toUpperCase());
			}
		});
	}

	public static abstract class ConfigProperty<T> implements IConfigProperty<T> {
		protected final ScreenConfig config;
		protected final String name;
		protected final T def;

		public ConfigProperty(final ScreenConfig config, final String name, final T def) {
			this.config = config;
			this.name = name;
			this.def = def;
		}

		protected String getProperty(final String def) {
			return this.config.properties.getProperty(this.name, def);
		}

		protected void setProperty(final String value) {
			this.config.properties.setProperty(this.name, value);
			this.config.onChanged();
		}

		@Override
		public void fillDefault() {
			set(get());
		}

		@Override
		public abstract T get();

		@Override
		public abstract void set(T value);
	}
}
