package net.teamfruit.simpleloadingscreen.resources;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

import net.teamfruit.simpleloadingscreen.api.IBlackboard;
import net.teamfruit.simpleloadingscreen.api.IConfig;
import net.teamfruit.simpleloadingscreen.api.IConfigMapper;
import net.teamfruit.simpleloadingscreen.api.position.Coord;
import net.teamfruit.simpleloadingscreen.api.position.Coord.CoordSide;
import net.teamfruit.simpleloadingscreen.api.position.RelativeArea;
import net.teamfruit.simpleloadingscreen.api.property.GuiProperties;

public class AreaConfigMapper implements IConfigMapper {

	public static final AreaConfigMapper instance = new AreaConfigMapper();

	private AreaConfigMapper() {
	}

	@Override
	public void map(final IConfig source, final IBlackboard dest) {
		final List<Coord> coords = Lists.newArrayList();
		coords.add(getCoord(CoordSide.Left, source.stringProperty("left", null).get()));
		coords.add(getCoord(CoordSide.Right, source.stringProperty("right", null).get()));
		coords.add(getCoord(CoordSide.Top, source.stringProperty("top", null).get()));
		coords.add(getCoord(CoordSide.Bottom, source.stringProperty("bottom", null).get()));
		coords.add(getCoord(CoordSide.Width, source.stringProperty("width", null).get()));
		coords.add(getCoord(CoordSide.Height, source.stringProperty("height", null).get()));
		coords.removeAll(Collections.singleton(null));
		final RelativeArea area = new RelativeArea(coords.toArray(new Coord[coords.size()]));
		dest.setValue("area", area);
	}

	private Coord getCoord(final CoordSide side, final String value) {
		if (value==null) {
			String numstr = value;
			boolean percent = false;
			if (StringUtils.endsWith(value, "%")) {
				numstr = StringUtils.substringBefore(value, "%");
				percent = true;
			}
			try {
				final float num = Float.parseFloat(numstr);
				return new Coord(percent ? GuiProperties.percent(num/100f) : GuiProperties.absolute(num), side);
			} catch (final NumberFormatException e) {
			}
		}
		return null;
	}

}
