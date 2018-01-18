package net.teamfruit.simpleloadingscreen.basemodule;

import net.teamfruit.simpleloadingscreen.api.IManager;
import net.teamfruit.simpleloadingscreen.api.IModule;

public class BaseModule implements IModule {

	@Override
	public boolean enable(final IManager manager) {
		manager.createComponent("fill").getCurrentRenderers().add(new FillRenderer());
		manager.createComponent("forge_logo").getCurrentRenderers().add(new ForgeLogoRenderer());
		manager.createComponent("forge_bar").getCurrentRenderers().add(new ProgressBarRenderer());
		return true;
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
