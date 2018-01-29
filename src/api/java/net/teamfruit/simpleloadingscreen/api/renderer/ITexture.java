package net.teamfruit.simpleloadingscreen.api.renderer;

import net.minecraft.util.ResourceLocation;
import net.teamfruit.simpleloadingscreen.api.position.Area;

public interface ITexture {

	void texCoord(final int frame, final float u, final float v);

	float getV(final int frame, final float v);

	float getU(final int frame, final float u);

	void delete();

	void bind();

	int getSize();

	int getFrames();

	int getHeight();

	int getWidth();

	int getName();

	ResourceLocation getLocation();

	Area getTexArea(int frame);

}
