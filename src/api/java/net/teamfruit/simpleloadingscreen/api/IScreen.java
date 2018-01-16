package net.teamfruit.simpleloadingscreen.api;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import net.teamfruit.simpleloadingscreen.api.position.Area;
import net.teamfruit.simpleloadingscreen.api.position.RelativeArea;

public interface IScreen {
	IResourceLocation location(String domain, String path);

	IResourceLocation location(String domainpath);

	ITexture texture(IResourceLocation location);

	InputStream open(IResourceLocation location) throws IOException;

	IFontRenderer getFontRenderer();

	List<IProgressBar> getProgressBars();

	IForgeSplashProperties getForgeSplashProperties();

	IComponent getComponent();

	Area getDisplayArea();

	RelativeArea getRelativeArea();

	Area getArea();
}
