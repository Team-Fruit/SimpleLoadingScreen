package net.teamfruit.simpleloadingscreen;

import java.io.File;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Config extends ConfigBase {
	private static @Nullable Config instance;
	private static @Nullable ICompat compat;

	public static @Nonnull Config getConfig() {
		if (instance!=null)
			return instance;
		throw new IllegalStateException("config not initialized");
	}

	public static void init(final @Nonnull File cfgFile, final @Nonnull String version, @Nonnull final ICompat icompat) {
		compat = icompat;
		instance = new Config(cfgFile, version);
	}

	private Config(final @Nonnull File configFile, final @Nonnull String version) {
		super(configFile, version);
	}
}
