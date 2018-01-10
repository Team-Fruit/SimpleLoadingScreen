package net.teamfruit.simpleloadingscreen.util;

import static org.lwjgl.opengl.GL11.*;

import net.teamfruit.simpleloadingscreen.api.Area;

public class RendererUtils {
	/**
	 * テクスチャ倍率
	 * <p>
	 * GUIを描画する際に使用します。
	 */
	public static final float textureScale = 1f/256f;

	public static final Area defaultTextureArea = Area.abs(0f, 0f, 1f, 1f);

	/**
	 * 4つの絶対座標からテクスチャを描画します
	 * <p>
	 * テクスチャ座標(倍率)は(0, 0)⇒(1, 1)にすることでテクスチャを1枚表示できます
	 * @param vx1 1つ目のX絶対座標
	 * @param vy1 1つ目のY絶対座標
	 * @param vx2 2つ目のX絶対座標
	 * @param vy2 2つ目のY絶対座標
	 * @param tx1 1つ目のXテクスチャ座標(倍率)
	 * @param ty1 1つ目のYテクスチャ座標(倍率)
	 * @param tx2 2つ目のXテクスチャ座標(倍率)
	 * @param ty2 2つ目のYテクスチャ座標(倍率)
	 */
	private static void drawTextureAbs(final float vx1, final float vy1, final float vx2, final float vy2, final float tx1, final float ty1, final float tx2, final float ty2) {
		glBegin(GL_QUADS);
		glTexCoord2f(tx1, ty2);
		glVertex2f(vx1, vy2);
		glTexCoord2f(tx2, ty2);
		glVertex2f(vx2, vy2);
		glTexCoord2f(tx2, ty1);
		glVertex2f(vx2, vy1);
		glTexCoord2f(tx1, ty1);
		glVertex2f(vx1, vy1);
		glEnd();
	}

	/**
	 * 4つの絶対座標からテクスチャを描画します
	 * <p>
	 * リピートされる無限サイズのテクスチャをトリミング絶対座標でくり抜きます。
	 * @param vx1 1つ目のX画像絶対座標
	 * @param vy1 1つ目のY画像絶対座標
	 * @param vx2 2つ目のX画像絶対座標
	 * @param vy2 2つ目のY画像絶対座標
	 * @param rx1 1つ目のXトリミング絶対座標
	 * @param ry1 1つ目のYトリミング絶対座標
	 * @param rx2 2つ目のXトリミング絶対座標
	 * @param ry2 2つ目のYトリミング絶対座標
	 * @param tx1 1つ目のXテクスチャ座標(倍率)
	 * @param ty1 1つ目のYテクスチャ座標(倍率)
	 * @param tx2 2つ目のXテクスチャ座標(倍率)
	 * @param ty2 2つ目のYテクスチャ座標(倍率)
	 */
	private static void drawTextureAbsTrim(final float vx1, final float vy1, final float vx2, final float vy2, final float rx1, final float ry1, final float rx2, final float ry2, final float tx1, final float ty1, final float tx2, final float ty2) {
		final float ox1 = tx2-tx1;
		final float oy1 = ty2-ty1;
		final float ox2 = vx2-vx1;
		final float oy2 = vy2-vy1;
		final float ox3 = ox2/ox1;
		final float oy3 = oy2/oy1;
		final float ox4 = (rx1-vx1)/ox3;
		final float oy4 = (ry1-vy1)/oy3;
		final float ox5 = (rx2-vx1)/ox3;
		final float oy5 = (ry2-vy1)/oy3;
		final float ox6 = (rx2-rx1)/ox3;
		final float oy6 = (ry2-ry1)/oy3;
		final float ox7 = ox2/ox1*ox6;
		final float oy7 = oy2/oy1*oy6;
		glBegin(GL_QUADS);
		glTexCoord2f(ox4+tx1, oy5+ty1);
		glVertex2f(rx1, ry1+oy7);
		glTexCoord2f(ox5+tx1, oy5+ty1);
		glVertex2f(rx1+ox7, ry1+oy7);
		glTexCoord2f(ox5+tx1, oy4+ty1);
		glVertex2f(rx1+ox7, ry1);
		glTexCoord2f(ox4+tx1, oy4+ty1);
		glVertex2f(rx1, ry1);
		glEnd();
	}

	/**
	 * 4つの絶対座標からテクスチャを描画します
	 * <p>
	 * 画像絶対座標サイズのテクスチャをトリミング絶対座標でくり抜きます。
	 * @param vx1 1つ目のX画像絶対座標
	 * @param vy1 1つ目のY画像絶対座標
	 * @param vx2 2つ目のX画像絶対座標
	 * @param vy2 2つ目のY画像絶対座標
	 * @param rx1 1つ目のXトリミング絶対座標
	 * @param ry1 1つ目のYトリミング絶対座標
	 * @param rx2 2つ目のXトリミング絶対座標
	 * @param ry2 2つ目のYトリミング絶対座標
	 * @param tx1 1つ目のXテクスチャ座標(倍率)
	 * @param ty1 1つ目のYテクスチャ座標(倍率)
	 * @param tx2 2つ目のXテクスチャ座標(倍率)
	 * @param ty2 2つ目のYテクスチャ座標(倍率)
	 */
	@SuppressWarnings("unused")
	@Deprecated
	private static void drawTextureAbsTrimOne(final float vx1, final float vy1, final float vx2, final float vy2, final float rx1, final float ry1, final float rx2, final float ry2, final float tx1, final float ty1, final float tx2, final float ty2) {
		drawTextureAbsTrim(vx1, vy1, vx2, vy2, Math.max(vx1, rx1), Math.max(vy1, ry1), Math.min(vx2, rx2), Math.min(vy2, ry2), tx1, ty1, tx2, ty2);
	}

	/**
	 * 絶対範囲からテクスチャを描画します
	 * <p>
	 * テクスチャ範囲は(0, 0)⇒(1, 1)にすることでテクスチャを1枚表示でき、nullが指定された場合と同様です
	 * @param vertex 絶対範囲 デフォルト:(0, 0)⇒(1, 1)
	 * @param trim トリミング範囲 デフォルト:(-∞, -∞)⇒(∞, ∞)
	 * @param texture テクスチャ範囲 デフォルト:(0, 0)⇒(1, 1)
	 */
	public static void drawTexture(final Area vertex, Area trim, final Area texture) {
		final Area v = vertex!=null ? vertex : defaultTextureArea;
		final Area t = texture!=null ? texture : defaultTextureArea;
		if (trim!=null) {
			trim = v.trimArea(trim);
			if (trim!=null)
				drawTextureAbsTrim(v.x1(), v.y1(), v.x2(), v.y2(), trim.x1(), trim.y1(), trim.x2(), trim.y2(), t.x1(), t.y1(), t.x2(), t.y2());
		} else
			drawTextureAbs(v.x1(), v.y1(), v.x2(), v.y2(), t.x1(), t.y1(), t.x2(), t.y2());
	}

	/**
	 * テクスチャ倍率(1/256)をかけ、{@link #drawTexture(Area Area Area)}と同様に描画します。
	 * <p>
	 * GUIを描画する場合主にこちらを使用します。
	 * @param vertex 絶対範囲 デフォルト:(0, 0)⇒(1, 1)
	 * @param trim トリミング範囲 デフォルト:(-∞, -∞)⇒(∞, ∞)
	 * @param texture テクスチャ範囲 デフォルト:(0, 0)⇒((1/256), (1/256))
	 */
	public static void drawTextureModal(final Area vertex, final Area trim, final Area texture) {
		drawTexture(vertex, trim, (texture!=null ? texture : defaultTextureArea).scale(textureScale));
	}

	/**
	 * 4つの絶対座標とGL描画モードを使用して描画します。
	 * @param x1 1つ目のX絶対座標
	 * @param y1 1つ目のY絶対座標
	 * @param x2 2つ目のX絶対座標
	 * @param y2 2つ目のY絶対座標
	 * @param mode GL描画モード
	 */
	private static void drawAbs(final float x1, final float y1, final float x2, final float y2, final int mode) {
		glBegin(GL_QUADS);
		glVertex2f(x1, y2);
		glVertex2f(x2, y2);
		glVertex2f(x2, y1);
		glVertex2f(x1, y1);
		glEnd();
	}

	/**
	 * 絶対範囲とGL描画モードを使用して描画します。
	 * @param vertex 絶対範囲 デフォルト:(0, 0)⇒(1, 1)
	 * @param mode GL描画モード
	 */
	public static void draw(final Area vertex, final int mode) {
		final Area v = vertex!=null ? vertex : defaultTextureArea;
		drawAbs(v.x1(), v.y1(), v.x2(), v.y2(), mode);
	}

	/**
	 * 絶対範囲を使用して描画します。
	 * <p>
	 * GL描画モードは{@link org.lwjgl.opengl.GL11#GL_QUADS GL_QUADS}が使用されます
	 * @param vertex 絶対範囲 デフォルト:(0, 0)⇒(1, 1)
	 */
	public static void draw(final Area vertex) {
		draw(vertex, GL_QUADS);
	}
}
