package net.teamfruit.simpleloadingscreen.basemodule;

import org.apache.commons.lang3.math.NumberUtils;

import net.teamfruit.simpleloadingscreen.api.renderer.IRenderer;
import net.teamfruit.simpleloadingscreen.api.renderer.IScreen;
import net.teamfruit.simpleloadingscreen.util.ColorUtils;
import net.teamfruit.simpleloadingscreen.util.RendererUtils;

public class FillRenderer implements IRenderer {

	private int color;

	@Override
	public void init(final IScreen screen) {
		int rgb = 0;
		final String colorstr = (String) screen.getComponent().getBlackboard().get("color");
		try {
			rgb = Integer.decode(colorstr);
		} catch (final NumberFormatException e) {
		}
		final String opacitystr = (String) screen.getComponent().getBlackboard().get("opacity");
		final float opacity = Math.min(Math.max(NumberUtils.toFloat(opacitystr, 1f), 0f), 1f);
		this.color = ColorUtils.toColorCode(rgb, opacity);
	}

	@Override
	public void draw(final IScreen screen) {
		RendererUtils.startShape();
		ColorUtils.glColorRGBA(this.color);
		RendererUtils.draw(screen.getArea());
	}

}
