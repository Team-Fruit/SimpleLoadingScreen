package net.teamfruit.simpleloadingscreen.api.property;

import javax.annotation.Nonnull;

/**
 * 絶対的な値と相対的な値を定義た値です
 *
 * @author TeamFruit
 */
public class BasicGuiProperty implements IGuiProperty {
	private final float coord;
	private @Nonnull GuiPropertyTypes type;

	public BasicGuiProperty(final float coord, final @Nonnull GuiPropertyTypes type) {
		this.coord = coord;
		this.type = type;
	}

	@Override
	public float get() {
		return this.coord;
	}

	@Override
	public float getAbsCoord(final float a, final float b) {
		return this.type.calc(a, b, get());
	}

	@Override
	public String toString() {
		return String.format("VBase [coord=%s, type=%s]", this.coord, this.type);
	}
}