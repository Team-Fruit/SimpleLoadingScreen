package net.teamfruit.simpleloadingscreen.basemodule;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import net.teamfruit.simpleloadingscreen.api.IBlackboard;
import net.teamfruit.simpleloadingscreen.api.IComponent;
import net.teamfruit.simpleloadingscreen.api.IManager;
import net.teamfruit.simpleloadingscreen.api.IModule;
import net.teamfruit.simpleloadingscreen.api.position.RelativeArea;
import net.teamfruit.simpleloadingscreen.gui.ScreenManager;
import net.teamfruit.simpleloadingscreen.resources.AreaConfigMapper;
import net.teamfruit.simpleloadingscreen.splash.LoadingScreen;
import net.teamfruit.simpleloadingscreen.style.StyleObjectModel;

public class BaseModule implements IModule {

	@Override
	public boolean enable(final IManager manager) {
		final LoadingScreen loadingScreen = ((ScreenManager) manager).getLoadingScreen();
		final StyleObjectModel model = loadingScreen.styleLoader.getStyle("main", new File(loadingScreen.loadingScreenDir, "screen.json"));
		fillBase(manager, model, null);
		return true;
	}

	private void fillBase(final IManager manager, final StyleObjectModel model, final RelativeArea parent) {
		final RelativeArea area = new RelativeArea(parent, AreaConfigMapper.instance.createArea(model.getProperty()));
		for (final StyleObjectModel child : model.getChild())
			fillBase(manager, child, area);

		String id = model.getProperty().get("id");
		if (id==null)
			id = UUID.randomUUID().toString();

		final Map<String, Object> blackboard = model.getBlackboard();
		final String source = model.getProperty().get("source");
		IComponent component = (IComponent) blackboard.get("component");
		if (component==null) {
			if (source!=null)
				component = manager.createComponent(source, id);
			if (component==null)
				component = manager.createComponent(id);
			blackboard.put("component", component);
		}
		blackboard.put("area", area);
		final IBlackboard cblackboard = component.getBlackboard();
		for (final Entry<String, String> entry : model.getProperty().entrySet())
			cblackboard.setValue(entry.getKey(), entry.getValue());
		for (final Entry<String, Object> entry : model.getBlackboard().entrySet())
			cblackboard.setValue(entry.getKey(), entry.getValue());

		manager.getRenderingComponents().add(component);
	}

	@Override
	public String getID() {
		return "basemodule";
	}

	@Override
	public String getName() {
		return "BaseModule";
	}

	@Override
	public String getAuthor() {
		return "TeamFruit";
	}

	@Override
	public String getVersion() {
		return "1.0.0";
	}

}
