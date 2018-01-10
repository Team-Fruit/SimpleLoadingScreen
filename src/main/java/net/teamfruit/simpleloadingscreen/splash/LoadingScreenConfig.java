package net.teamfruit.simpleloadingscreen.splash;

import net.teamfruit.simpleloadingscreen.api.IConfig;
import net.teamfruit.simpleloadingscreen.api.IConfigProperty;
import net.teamfruit.simpleloadingscreen.api.IForgeSplashProperties;

public class LoadingScreenConfig implements IForgeSplashProperties {
	private final IConfigProperty<Boolean> enabled;
	private final IConfigProperty<Boolean> rotate;
	private final IConfigProperty<Integer> logoOffset;
	private final IConfigProperty<Integer> backgroundColor;
	private final IConfigProperty<Integer> fontColor;
	private final IConfigProperty<Integer> barBorderColor;
	private final IConfigProperty<Integer> barColor;
	private final IConfigProperty<Integer> barBackgroundColor;
	private final IConfigProperty<String> resourcePackPath;
	private final IConfigProperty<String> fontLoc;
	private final IConfigProperty<String> logoLoc;
	private final IConfigProperty<String> forgeLoc;

	public LoadingScreenConfig(final IConfig config) {
		this.enabled = config.booleanProperty("enabled", true);
		this.rotate = config.booleanProperty("rotate", false);
		this.logoOffset = config.intProperty("logoOffset", 0);
		this.backgroundColor = config.hexProperty("background", 0xFFFFFF);
		this.fontColor = config.hexProperty("font", 0x000000);
		this.barBorderColor = config.hexProperty("barBorder", 0xC0C0C0);
		this.barColor = config.hexProperty("bar", 0xCB3D35);
		this.barBackgroundColor = config.hexProperty("barBackground", 0xFFFFFF);
		this.resourcePackPath = config.stringProperty("resourcePackPath", "resources");
		this.fontLoc = config.stringProperty("fontTexture", "textures/font/ascii.png");
		this.logoLoc = config.stringProperty("logoTexture", "textures/gui/title/mojang.png");
		this.forgeLoc = config.stringProperty("forgeTexture", "fml:textures/gui/forge.gif");
	}

	@Override
	public IConfigProperty<Boolean> getEnabled() {
		return this.enabled;
	}

	@Override
	public IConfigProperty<Boolean> getRotate() {
		return this.rotate;
	}

	@Override
	public IConfigProperty<Integer> getLogoOffset() {
		return this.logoOffset;
	}

	@Override
	public IConfigProperty<Integer> getBackgroundColor() {
		return this.backgroundColor;
	}

	@Override
	public IConfigProperty<Integer> getFontColor() {
		return this.fontColor;
	}

	@Override
	public IConfigProperty<Integer> getBarBorderColor() {
		return this.barBorderColor;
	}

	@Override
	public IConfigProperty<Integer> getBarColor() {
		return this.barColor;
	}

	@Override
	public IConfigProperty<Integer> getBarBackgroundColor() {
		return this.barBackgroundColor;
	}

	@Override
	public IConfigProperty<String> getResourcePackPath() {
		return this.resourcePackPath;
	}

	@Override
	public IConfigProperty<String> getFontLoc() {
		return this.fontLoc;
	}

	@Override
	public IConfigProperty<String> getLogoLoc() {
		return this.logoLoc;
	}

	@Override
	public IConfigProperty<String> getForgeLoc() {
		return this.forgeLoc;
	}
}
