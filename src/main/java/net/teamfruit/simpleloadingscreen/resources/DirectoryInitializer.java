package net.teamfruit.simpleloadingscreen.resources;

import java.io.File;

public class DirectoryInitializer {
	public final File loadingScreenDir;
	public final File configDir;
	public final File resourceDir;
	public final File moduleDir;
	public final File themeDir;
	public final File jsonScreen;

	public DirectoryInitializer(final File loadingScreenDir) {
		this.loadingScreenDir = loadingScreenDir;
		this.configDir = new File(this.loadingScreenDir, "config");
		this.resourceDir = new File(this.loadingScreenDir, "resources");
		this.moduleDir = new File(this.loadingScreenDir, "modules");
		this.themeDir = new File(this.loadingScreenDir, "themes");
		this.jsonScreen = new File(loadingScreenDir, "screen.json");
	}

	public void init() {
		this.loadingScreenDir.mkdirs();
		if (isInitialized()) {
			this.configDir.mkdirs();
			this.resourceDir.mkdirs();
			this.moduleDir.mkdirs();
			this.themeDir.mkdirs();
		}
	}

	public boolean isInitialized() {
		return this.loadingScreenDir.isDirectory()&&this.loadingScreenDir.listFiles().length>0;
	}
}
