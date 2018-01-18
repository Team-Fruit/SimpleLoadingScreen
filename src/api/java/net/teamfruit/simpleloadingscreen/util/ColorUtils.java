package net.teamfruit.simpleloadingscreen.util;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * <p>Some font utils from BnnWidget
 *
 * <p>JavaDocs is written in Japanese. Please translate it.
 */
public class ColorUtils {
	private static final FloatBuffer buf = ByteBuffer.allocateDirect(16<<2).order(ByteOrder.nativeOrder()).asFloatBuffer();

	/**
	 * {@link net.minecraft.client.gui.FontRenderer FontRenderer}で使用可能なカラーコードへ変換します。
	 * @param color カラーコード
	 * @return {@link net.minecraft.client.gui.FontRenderer FontRenderer}で使用可能なカラーコード
	 */
	public static int toFontColor(final int color) {
		final int alpha = Math.max(color>>24&255, 0x4)<<24;
		return color&0xffffff|alpha;
	}

	/**
	 * int型RGBAカラーをカラーコードに変換します
	 * <p>
	 * フォントカラーの範囲は0～255です
	 * @param r カラー(赤)
	 * @param g カラー(緑)
	 * @param b カラー(青)
	 * @param a カラー(不透明度)
	 * @return カラーコード
	 */
	public static int toColorCode(final int r, final int g, final int b, final int a) {
		return (a&0xff)<<24|(r&0xff)<<16|(g&0xff)<<8|(b&0xff)<<0;
	}

	/**
	 * float型RGBAカラーをカラーコードに変換します
	 * <p>
	 * フォントカラーの範囲は0～1です
	 * @param r カラー(赤)
	 * @param g カラー(緑)
	 * @param b カラー(青)
	 * @param a カラー(不透明度)
	 * @return カラーコード
	 */
	public static int toColorCode(final float r, final float g, final float b, final float a) {
		return toColorCode((int) (r*255+.5f), (int) (g*255+.5f), (int) (b*255+.5f), (int) (a*255+.5f));
	}

	/**
	 * int型RGBAカラーをカラーコードに変換します
	 * <p>
	 * フォントカラーの範囲は0～255です
	 * @param rgb カラー
	 * @param a カラー(不透明度)
	 * @return カラーコード
	 */
	public static int toColorCode(final int rgb, final int a) {
		return (a&0xff)<<24|rgb;
	}

	/**
	 * float型RGBAカラーをカラーコードに変換します
	 * <p>
	 * フォントカラーの範囲は0～1です
	 * @param rgb カラー
	 * @param a カラー(不透明度)
	 * @return カラーコード
	 */
	public static int toColorCode(final int rgb, final float a) {
		return toColorCode(rgb, (int) (a*255+.5f));
	}

	public static void glColor4i(final int red, final int green, final int blue, final int alpha) {
		glColor4f(red/255f, green/255f, blue/255f, alpha/255f);
	}

	public static void glColorRGB(final int rgb) {
		final int value = 0xff000000|rgb;
		glColor4i(value>>16&0xff, value>>8&0xff, value>>0&0xff, value>>24&0xff);
	}

	public static void glColorRGBA(final int rgba) {
		glColor4i(rgba>>16&0xff, rgba>>8&0xff, rgba>>0&0xff, rgba>>24&0xff);
	}

	public static void glColor(final Color color) {
		glColor4i(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
	}

	public static void glColor(final org.lwjgl.util.Color color) {
		glColor4i(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
	}

	public static int glGetColorRGBA() {
		buf.clear();
		glGetFloat(GL_CURRENT_COLOR, buf);
		final float r = buf.get(0);
		final float g = buf.get(1);
		final float b = buf.get(2);
		final float a = buf.get(3);
		return toColorCode(r, g, b, a);
	}

	public static Color glGetColor() {
		buf.clear();
		glGetFloat(GL_CURRENT_COLOR, buf);
		final float r = Math.min(1f, buf.get(0));
		final float g = Math.min(1f, buf.get(1));
		final float b = Math.min(1f, buf.get(2));
		final float a = Math.min(1f, buf.get(3));
		return new Color(r, g, b, a);
	}

	public static org.lwjgl.util.Color glGetLwjglColor() {
		buf.clear();
		glGetFloat(GL_CURRENT_COLOR, buf);
		final float r = buf.get(0);
		final float g = buf.get(1);
		final float b = buf.get(2);
		final float a = buf.get(3);
		return new org.lwjgl.util.Color((int) (r*255+0.5)&0xff, (int) (g*255+0.5)&0xff, (int) (b*255+0.5)&0xff, (int) (a*255+0.5)&0xff);
	}
}
