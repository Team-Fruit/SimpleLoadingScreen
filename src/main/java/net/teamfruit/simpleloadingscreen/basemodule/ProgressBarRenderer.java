package net.teamfruit.simpleloadingscreen.basemodule;

import static org.lwjgl.opengl.GL11.*;

import java.util.Iterator;

import net.teamfruit.simpleloadingscreen.api.IFontRenderer;
import net.teamfruit.simpleloadingscreen.api.IForgeSplashProperties;
import net.teamfruit.simpleloadingscreen.api.IProgressBar;
import net.teamfruit.simpleloadingscreen.api.IRenderer;
import net.teamfruit.simpleloadingscreen.api.IScreen;
import net.teamfruit.simpleloadingscreen.api.position.Area;
import net.teamfruit.simpleloadingscreen.util.ColorUtils;
import net.teamfruit.simpleloadingscreen.util.RendererUtils;

public class ProgressBarRenderer implements IRenderer {
	public int barWidth = 400;
	public int barHeight = 20;
	public int textHeight2 = 20;
	public int barOffset = 55;

	public int fontColor;
	public int barColor;
	public int barBorderColor;
	public int barBackgroundColor;

	@Override
	public void init(final IScreen screen) {
		final IForgeSplashProperties properties = screen.getForgeSplashProperties();
		this.fontColor = properties.getFontColor().get();
		this.barColor = properties.getBarColor().get();
		this.barBorderColor = properties.getBarBorderColor().get();
		this.barBackgroundColor = properties.getBarBackgroundColor().get();
	}

	@Override
	public void draw(final IScreen screen) {
		IProgressBar first = null, penult = null, last = null;
		for (final Iterator<IProgressBar> i = screen.getProgressBars().iterator(); i.hasNext();)
			if (first==null)
				first = i.next();
			else {
				penult = last;
				last = i.next();
			}

		// bars
		if (first!=null) {
			glPushMatrix();
			glTranslatef(320-(float) this.barWidth/2, 310, 0);
			drawBar(screen, first);
			if (penult!=null) {
				glTranslatef(0, this.barOffset, 0);
				drawBar(screen, penult);
			}
			if (last!=null) {
				glTranslatef(0, this.barOffset, 0);
				drawBar(screen, last);
			}
			glPopMatrix();
		}
	}

	private void drawBar(final IScreen screen, final IProgressBar b) {
		final IFontRenderer fontRenderer = screen.getFontRenderer();

		glPushMatrix();
		// title - message
		ColorUtils.glColorRGB(this.fontColor);
		glScalef(2, 2, 1);
		glEnable(GL_TEXTURE_2D);
		fontRenderer.drawString(b.getTitle()+" - "+b.getMessage(), 0, 0, 0x000000);
		glDisable(GL_TEXTURE_2D);
		glPopMatrix();
		// border
		glPushMatrix();
		glTranslatef(0, this.textHeight2, 0);
		ColorUtils.glColorRGB(this.barBorderColor);
		RendererUtils.draw(Area.size(0, 0, this.barWidth, this.barHeight));
		// interior
		ColorUtils.glColorRGB(this.barBackgroundColor);
		glTranslatef(1, 1, 0);
		RendererUtils.draw(Area.size(0, 0, this.barWidth-2, this.barHeight-2));
		// slidy part
		ColorUtils.glColorRGB(this.barColor);
		RendererUtils.draw(Area.size(0, 0, (this.barWidth-2)*(b.getStep()+1)/(b.getSteps()+1), this.barHeight-2)); // Step can sometimes be 0.
		// progress text
		final String progress = ""+b.getStep()+"/"+b.getSteps();
		glTranslatef(((float) this.barWidth-2)/2-fontRenderer.getStringWidth(progress), 2, 0);
		ColorUtils.glColorRGB(this.fontColor);
		glScalef(2, 2, 1);
		glEnable(GL_TEXTURE_2D);
		fontRenderer.drawString(progress, 0, 0, 0x000000);
		glPopMatrix();
	}
}
