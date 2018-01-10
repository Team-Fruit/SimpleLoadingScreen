package net.teamfruit.simpleloadingscreen.gui;

import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import net.teamfruit.simpleloadingscreen.api.IBlackboard;

public class ScreenBlackboard implements IBlackboard {
	private final Map<String, Object> map;
	private final String currentPath;

	public ScreenBlackboard(final Map<String, Object> map, final String... paths) {
		this.map = map;
		this.currentPath = StringUtils.join(paths);
	}

	@Override
	public Object getValueGlobal(final String globalKey) {
		return this.map.get(globalKey);
	}

	@Override
	public Object getValue(final String localKey) {
		if (localKey.contains("."))
			throw new IllegalArgumentException("Local key connot contain period character.");
		return this.map.get(this.currentPath+"."+localKey);
	}

	@Override
	public void setValue(final String localKey, final Object value) {
		if (localKey.contains("."))
			throw new IllegalArgumentException("Local key connot contain period character.");
		this.map.put(this.currentPath+"."+localKey, value);
	}

	public static void copy(final ScreenBlackboard from, final ScreenBlackboard to) {
		for (final Entry<String, Object> entry : from.map.entrySet()) {
			final String key = entry.getKey();
			if (key.startsWith(from.currentPath))
				to.setValue(StringUtils.strip(StringUtils.substringAfter(key, from.currentPath), "."), entry.getValue());
		}
	}
}
