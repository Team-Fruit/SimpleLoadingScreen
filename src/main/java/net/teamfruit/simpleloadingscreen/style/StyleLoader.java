package net.teamfruit.simpleloadingscreen.style;

import java.io.File;
import java.util.Map;

import com.google.common.collect.Maps;

import net.teamfruit.simpleloadingscreen.splash.LoadingScreen;

public class StyleLoader {
	private final StyleObjectLoader styleObjectLoader = new StyleObjectLoader();
	private final File themeDir;

	private final Map<String, StyleObjectModel> themes = Maps.newHashMap();

	public StyleLoader(final LoadingScreen loadingScreen, final File themeDir) {
		this.themeDir = themeDir;
	}

	public StyleObjectModel getStyle(final String id, final File style) {
		final StyleObjectModel cached = this.themes.get(id);
		if (cached!=null)
			return cached;
		final StyleObjectModel model = this.styleObjectLoader.read(style, false);
		this.themes.put(id, model);
		fillBase(model);
		return model;
	}

	public StyleObjectModel getThemeStyle(final String id) {
		return getStyle("themes/"+id, new File(this.themeDir, id));
	}

	private void fillBase(final StyleObjectModel target) {
		for (final StyleObjectModel child : target.getChild())
			fillBase(child);
		final String propBase = target.getProperty().get(StyleObjectModel.PropertyExtends);
		if (propBase!=null) {
			final StyleObjectModel source = getThemeStyle(propBase);
			if (source!=null)
				target.importSource(source);
		}
	}
}
