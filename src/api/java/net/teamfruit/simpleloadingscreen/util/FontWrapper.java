package net.teamfruit.simpleloadingscreen.util;

import static org.lwjgl.opengl.GL11.*;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import net.teamfruit.simpleloadingscreen.api.position.Area;
import net.teamfruit.simpleloadingscreen.api.renderer.IFontRenderer;

/**
 * <p>Some font utils from BnnWidget
 *
 * <p>JavaDocs is written in Japanese. Please translate it.
 */
public class FontWrapper {
	private static final FloatBuffer buf = ByteBuffer.allocateDirect(16<<2).order(ByteOrder.nativeOrder()).asFloatBuffer();

	private final IFontRenderer fontRenderer;

	public FontWrapper(final IFontRenderer fontRenderer) {
		this.fontRenderer = fontRenderer;
	}

	/**
	 * <p>文字列を描画します
	 *
	 * <p>事前に{@link org.lwjgl.opengl.GL11#glColor3f(float, float, float) glColor3f}を呼ぶことで色を設定できます。
	 * @param text 文字列
	 * @param x X絶対座標
	 * @param y Y絶対座標
	 * @param w 絶対幅
	 * @param h 絶対高さ
	 * @param scale 大きさ
	 * @param align 水平寄せ
	 * @param valign 垂直寄せ
	 * @param shadow 影を付ける場合true
	 */
	private void drawString(final String text, final float x, final float y, final float w, final float h, final float scale, final Align align, final VerticalAlign valign, final boolean shadow) {
		glPushMatrix();
		align.translate(this.fontRenderer, text, x, w/scale);
		valign.translate(this.fontRenderer, text, y, h/scale);
		glScalef(scale, scale, 1);
		buf.clear();
		glGetFloat(GL_CURRENT_COLOR, buf);
		final float r = buf.get(0);
		final float g = buf.get(1);
		final float b = buf.get(2);
		final float a = buf.get(3);
		glColor4f(1f, 1f, 1f, 1f);
		this.fontRenderer.drawString(text, 0, 0, Math.max((int) (a*255+0.5)&0xff, 0x4)<<24|((int) (r*255+0.5)&0xFF)<<16|((int) (g*255+0.5)&0xFF)<<8|((int) (b*255+0.5)&0xFF)<<0, shadow);
		glColor4f(r, g, b, a);
		glPopMatrix();
	}

	/**
	 * <p>文字列を描画します
	 *
	 * <p>事前に{@link org.lwjgl.opengl.GL11#glColor3f(float, float, float) glColor3f}を呼ぶことで色を設定できます。
	 * @param text 文字列
	 * @param a 絶対範囲
	 * @param scale 大きさ
	 * @param align 水平寄せ
	 * @param valign 垂直寄せ
	 * @param shadow 影を付ける場合true
	 */
	public void drawString(final String text, final Area a, final float scale, final Align align, final VerticalAlign valign, final boolean shadow) {
		drawString(text, a.x1(), a.y1(), a.w(), a.h(), scale, align, valign, shadow);
	}

	/**
	 * 水平寄せ
	 *
	 * @author TeamFruit
	 */
	public static enum Align {
		/**
		 * 左寄せ
		 */
		LEFT {
			@Override
			protected void translate(final IFontRenderer fontRenderer, final String text, final float x, final float w) {
				glTranslatef(x, 0, 0);
			}
		},
		/**
		 * 中央寄せ
		 */
		CENTER {
			@Override
			protected void translate(final IFontRenderer fontRenderer, final String text, final float x, final float w) {
				glTranslatef(x+(w-fontRenderer.getStringWidth(text))/2, 0, 0);
			}
		},
		/**
		 * 右寄せ
		 */
		RIGHT {
			@Override
			protected void translate(final IFontRenderer fontRenderer, final String text, final float x, final float w) {
				glTranslatef(x-fontRenderer.getStringWidth(text), 0, 0);
			}
		},
		;
		protected abstract void translate(IFontRenderer fontRenderer, String text, float x, float w);
	}

	/**
	 * 垂直寄せ
	 *
	 * @author TeamFruit
	 */
	public static enum VerticalAlign {
		/**
		 * 上寄せ
		 */
		TOP {
			@Override
			protected void translate(final IFontRenderer fontRenderer, final String text, final float y, final float h) {
				glTranslatef(0, y, 0);
			}
		},
		/**
		 * 中央寄せ
		 */
		MIDDLE {
			@Override
			protected void translate(final IFontRenderer fontRenderer, final String text, final float y, final float h) {
				glTranslatef(0, y+(h-fontRenderer.getFontHeight())/2, 0);
			}
		},
		/**
		 * 下寄せ
		 */
		BOTTOM {
			@Override
			protected void translate(final IFontRenderer fontRenderer, final String text, final float y, final float h) {
				glTranslatef(0, y+h-fontRenderer.getFontHeight(), 0);
			}
		},
		;
		protected abstract void translate(IFontRenderer fontRenderer, String text, float y, float h);
	}
}
