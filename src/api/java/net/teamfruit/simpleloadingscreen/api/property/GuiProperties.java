package net.teamfruit.simpleloadingscreen.api.property;

import javax.annotation.Nonnull;

/**
 * 様々な値を作成します
 *
 * @author TeamFruit
 */
public abstract class GuiProperties {
	/**
	 * 絶対的な値
	 * @param n 値
	 * @return 絶対的な値
	 */
	public static @Nonnull BasicGuiProperty absolute(final float n) {
		return new BasicGuiProperty(n, GuiPropertyTypes.Absolute);
	}

	/**
	 * 相対的な値
	 * @param n 値
	 * @return 相対的な値
	 */
	public static @Nonnull BasicGuiProperty percent(final float n) {
		return new BasicGuiProperty(n, GuiPropertyTypes.Percent);
	}

	/**
	 * 複数の値を足した値を作ります
	 * @param a ベースとなる値
	 * @param b 足す値
	 * @return 値
	 */
	public static @Nonnull CompoundGuiProperty combine(final @Nonnull IGuiProperty a, final @Nonnull IGuiProperty... b) {
		return new CompoundGuiProperty(a, b);
	}
}
