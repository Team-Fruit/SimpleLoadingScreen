package net.teamfruit.simpleloadingscreen.resources;

import net.minecraft.client.Minecraft;
import net.teamfruit.simpleloadingscreen.api.IBlackboard;
import net.teamfruit.simpleloadingscreen.api.IConfig;
import net.teamfruit.simpleloadingscreen.api.IConfigMapper;

public class AreaConfigMapper implements IConfigMapper {

	public static final AreaConfigMapper instance = new AreaConfigMapper();

	private AreaConfigMapper() {
	}

	@Override
	public void map(final IConfig source, final IBlackboard dest) {
		final Minecraft mc = Minecraft.getMinecraft();
		dest.setValue("posX", source.intProperty("posX", 0));
		dest.setValue("posY", source.intProperty("posY", 0));
		dest.setValue("width", source.intProperty("width", mc.displayWidth));
		dest.setValue("height", source.intProperty("height", mc.displayHeight));
	}

}
