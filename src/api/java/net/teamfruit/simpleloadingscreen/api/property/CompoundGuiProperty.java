package net.teamfruit.simpleloadingscreen.api.property;

import java.util.Set;

import javax.annotation.Nonnull;

import com.google.common.collect.Sets;

/**
 * 複数の値を足した値
 *
 * @author TeamFruit
 */
public class CompoundGuiProperty implements IGuiProperty {
	/**
	 * ベースとなる値
	 */
	protected @Nonnull IGuiProperty coord;
	/**
	 * 足す値
	 */
	protected final @Nonnull Set<IGuiProperty> coords;

	public CompoundGuiProperty(final @Nonnull IGuiProperty a, final @Nonnull IGuiProperty... b) {
		this.coord = a;
		this.coords = Sets.newHashSet(b);
	}

	@Override
	public float get() {
		float f = this.coord.get();
		for (final IGuiProperty c : this.coords)
			f += c.get();
		return f;
	}

	@Override
	public float getAbsCoord(final float a, final float b) {
		float f = this.coord.getAbsCoord(a, b);
		for (final IGuiProperty c : this.coords)
			f += c.getAbsCoord(0, b-a);
		return f;
	}

	@Override
	public String toString() {
		return String.format("VCompound [coord=%s, coords=%s]", this.coord, this.coords);
	}
}