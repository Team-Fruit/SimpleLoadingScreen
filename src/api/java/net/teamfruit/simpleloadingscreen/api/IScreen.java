package net.teamfruit.simpleloadingscreen.api;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface IScreen {
	int getWidth();

	int getHeight();

	IResourceLocation location(String domain, String path);

	IResourceLocation location(String domainpath);

	ITexture texture(IResourceLocation location);

	InputStream open(IResourceLocation location) throws IOException;

	IFontRenderer getFontRenderer();

	List<IProgressBar> getProgressBars();

	IForgeSplashProperties getForgeSplashProperties();

	IComponent getComponent();

	Area getArea();
}
