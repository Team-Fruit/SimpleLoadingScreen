package net.teamfruit.simpleloadingscreen.basemodule;

import static org.lwjgl.opengl.GL11.*;

import net.teamfruit.simpleloadingscreen.api.IForgeSplashProperties;
import net.teamfruit.simpleloadingscreen.api.position.Area;
import net.teamfruit.simpleloadingscreen.api.renderer.IRenderer;
import net.teamfruit.simpleloadingscreen.api.renderer.IScreen;
import net.teamfruit.simpleloadingscreen.api.renderer.ITexture;
import net.teamfruit.simpleloadingscreen.util.ColorUtils;

public class ForgeLogoRenderer implements IRenderer {
	public int angle;

	public boolean rotate;
	public int backgroundColor;
	public int logoOffset;
	public String forgeLoc;
	public ITexture forgeTexture;

	@Override
	public void init(final IScreen screen) {
		final IForgeSplashProperties properties = screen.getForgeSplashProperties();
		this.rotate = properties.getRotate().get();
		this.backgroundColor = properties.getBackgroundColor().get();
		this.logoOffset = properties.getLogoOffset().get();
		this.forgeLoc = properties.getForgeLoc().get();
		this.forgeTexture = screen.texture(screen.location(this.forgeLoc));
	}

	@Override
	public void draw(final IScreen screen) {
		this.angle += 1;

		// forge logo
		ColorUtils.glColorRGB(this.backgroundColor);
		final Area a = screen.getArea();
		final float fw = (float) this.forgeTexture.getWidth()/2/2;
		final float fh = (float) this.forgeTexture.getHeight()/2/2;
		if (this.rotate) {
			final float sh = Math.max(fw, fh);
			glTranslatef(320+a.w()/2-sh-this.logoOffset, 240+a.h()/2-sh-this.logoOffset, 0);
			glRotatef(this.angle, 0, 0, 1);
		} else
			glTranslatef(320+a.w()/2-fw-this.logoOffset, 240+a.h()/2-fh-this.logoOffset, 0);
		final int f = this.angle/10%this.forgeTexture.getFrames();
		glEnable(GL_TEXTURE_2D);
		this.forgeTexture.bind();
		glBegin(GL_QUADS);
		this.forgeTexture.texCoord(f, 0, 0);
		glVertex2f(-fw, -fh);
		this.forgeTexture.texCoord(f, 0, 1);
		glVertex2f(-fw, fh);
		this.forgeTexture.texCoord(f, 1, 1);
		glVertex2f(fw, fh);
		this.forgeTexture.texCoord(f, 1, 0);
		glVertex2f(fw, -fh);
		glEnd();
		glDisable(GL_TEXTURE_2D);
	}
}
