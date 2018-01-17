package net.teamfruit.simpleloadingscreen.resources;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Lists;

import net.teamfruit.simpleloadingscreen.api.IBlackboard;
import net.teamfruit.simpleloadingscreen.api.IPropertyMapper;
import net.teamfruit.simpleloadingscreen.api.position.Coord;
import net.teamfruit.simpleloadingscreen.api.position.Coord.CoordSide;
import net.teamfruit.simpleloadingscreen.api.position.RelativeArea;
import net.teamfruit.simpleloadingscreen.api.property.GuiProperties;
import net.teamfruit.simpleloadingscreen.api.property.IGuiProperty;

public class AreaConfigMapper implements IPropertyMapper {

	public static final AreaConfigMapper instance = new AreaConfigMapper();

	private AreaConfigMapper() {
	}

	@Override
	public void map(final Map<String, String> source, final IBlackboard dest) {
		final RelativeArea area = createArea(source);
		dest.setValue("area", area);
	}

	public RelativeArea createArea(final Map<String, String> source) {
		final List<Coord> coords = Lists.newArrayList();
		coords.add(getCoord(CoordSide.Left, source.get("left")));
		coords.add(getCoord(CoordSide.Right, source.get("right")));
		coords.add(getCoord(CoordSide.Top, source.get("top")));
		coords.add(getCoord(CoordSide.Bottom, source.get("bottom")));
		coords.add(getCoord(CoordSide.Width, source.get("width")));
		coords.add(getCoord(CoordSide.Height, source.get("height")));
		coords.removeAll(Collections.singleton(null));
		return new RelativeArea(coords.toArray(new Coord[coords.size()]));
	}

	private Coord getCoord(final CoordSide side, final String value) {
		if (value!=null) {
			final String[] items = StringUtils.split(StringUtils.replace(value, "-", "+-"), "+");
			final List<IGuiProperty> properties = Lists.newArrayList();
			for (final String item : items) {
				String numstr = StringUtils.trim(item);
				if (StringUtils.isEmpty(numstr))
					continue;
				boolean percent = false;
				if (StringUtils.endsWith(numstr, "%")) {
					numstr = StringUtils.substringBefore(numstr, "%");
					percent = true;
				}
				try {
					final float num = Float.parseFloat(numstr);
					properties.add(percent ? GuiProperties.percent(num/100f) : GuiProperties.absolute(num));
				} catch (final NumberFormatException e) {
				}
			}
			if (!properties.isEmpty()) {
				final int size = properties.size();
				final IGuiProperty first = properties.get(0);
				if (size==1)
					return new Coord(first, side);
				else
					return new Coord(GuiProperties.combine(first, properties.subList(1, size).toArray(new IGuiProperty[size-1])), side);
			}
		}
		return null;
	}

}
