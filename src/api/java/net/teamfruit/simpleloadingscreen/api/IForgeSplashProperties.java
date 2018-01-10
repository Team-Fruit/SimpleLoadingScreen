package net.teamfruit.simpleloadingscreen.api;

public interface IForgeSplashProperties {

	IConfigProperty<String> getResourcePackPath();

	IConfigProperty<Integer> getBarBackgroundColor();

	IConfigProperty<Integer> getBarColor();

	IConfigProperty<Integer> getBarBorderColor();

	IConfigProperty<Integer> getFontColor();

	IConfigProperty<Integer> getBackgroundColor();

	IConfigProperty<Integer> getLogoOffset();

	IConfigProperty<Boolean> getRotate();

	IConfigProperty<Boolean> getEnabled();

	IConfigProperty<String> getForgeLoc();

	IConfigProperty<String> getLogoLoc();

	IConfigProperty<String> getFontLoc();

}