package net.teamfruit.simpleloadingscreen.basemodule;

import net.teamfruit.simpleloadingscreen.api.IComponent;
import net.teamfruit.simpleloadingscreen.api.IManager;
import net.teamfruit.simpleloadingscreen.api.IModule;

public class BaseModule implements IModule {

	@Override
	public boolean enable(final IManager manager) {
		final IComponent forgeLogoComponent = manager.createComponent("forge_logo");
		forgeLogoComponent.getCurrentRenderers().add(new ForgeLogoRenderer());
		final IComponent barComponent = manager.createComponent("forge_bar");
		barComponent.getCurrentRenderers().add(new ProgressBarRenderer());
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
