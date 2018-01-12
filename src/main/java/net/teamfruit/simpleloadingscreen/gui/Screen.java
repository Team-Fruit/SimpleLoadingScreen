package net.teamfruit.simpleloadingscreen.gui;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.teamfruit.simpleloadingscreen.api.IComponent;
import net.teamfruit.simpleloadingscreen.api.IFontRenderer;
import net.teamfruit.simpleloadingscreen.api.IForgeSplashProperties;
import net.teamfruit.simpleloadingscreen.api.IProgressBar;
import net.teamfruit.simpleloadingscreen.api.IResourceLocation;
import net.teamfruit.simpleloadingscreen.api.IScreen;
import net.teamfruit.simpleloadingscreen.api.ITexture;
import net.teamfruit.simpleloadingscreen.api.position.Area;
import net.teamfruit.simpleloadingscreen.api.position.RelativeArea;
import net.teamfruit.simpleloadingscreen.resources.ScreenResourceLocation;
import net.teamfruit.simpleloadingscreen.splash.LoadingScreen;

public class Screen implements IScreen {
	private final LoadingScreen loadingScreen;
	private final int width;
	private final int height;
	private final IFontRenderer fontRenderer;
	private final IComponent component;

	public Screen(final LoadingScreen loadingScreen, final IComponent component) {
		this.loadingScreen = loadingScreen;
		final Minecraft mc = Minecraft.getMinecraft();
		this.width = mc.displayWidth;
		this.height = mc.displayHeight;
		this.fontRenderer = loadingScreen.fontRenderer;
		this.component = component;
	}

	@Override
	public int getWidth() {
		return this.width;
	}

	@Override
	public int getHeight() {
		return this.height;
	}

	@Override
	public IFontRenderer getFontRenderer() {
		return this.fontRenderer;
	}

	@Override
	public Area getDisplayArea() {
		return Area.size(0, 0, this.width, this.height);
	}

	@Override
	public RelativeArea getArea() {
		final Object obj = getComponent().getBlackboard().getValue("area");
		if (obj instanceof RelativeArea)
			return (RelativeArea) obj;
		return new RelativeArea();
	}

	@Override
	public List<IProgressBar> getProgressBars() {
		return Collections.unmodifiableList(this.loadingScreen.progressManager.getProgressBars());
	}

	@Override
	public IComponent getComponent() {
		return this.component;
	}

	@Override
	public IForgeSplashProperties getForgeSplashProperties() {
		return this.loadingScreen.config;
	}

	@Override
	public IResourceLocation location(final String domain, final String path) {
		return new ScreenResourceLocation(new ResourceLocation(domain, path));
	}

	@Override
	public IResourceLocation location(final String domainpath) {
		return new ScreenResourceLocation(new ResourceLocation(domainpath));
	}

	@Override
	public ITexture texture(final IResourceLocation location) {
		return new ScreenTexture(this.loadingScreen.resourceLoader, ScreenResourceLocation.getLocation(location));
	}

	@Override
	public InputStream open(final IResourceLocation location) throws IOException {
		return this.loadingScreen.resourceLoader.open(ScreenResourceLocation.getLocation(location));
	}
}
