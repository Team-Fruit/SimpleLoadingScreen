package net.teamfruit.simpleloadingscreen.style;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class StyleObjectModel {
	public static final String ComponentId = "id";
	public static final String ComponentExtends = "component";
	// public static final String PropertyId = "theme_id";
	public static final String PropertyExtends = "theme";

	transient Map<String, String> property;

	transient Map<String, Object> blackboard;

	List<StyleObjectModel> child;

	public void importSource(final StyleObjectModel model) {
		{
			final Map<String, Object> map = Maps.newHashMap(model.getBlackboard());
			map.putAll(getBlackboard());
			this.blackboard = map;
		}
		{
			final Map<String, String> map = Maps.newHashMap(model.getProperty());
			map.putAll(getProperty());
			this.property = map;
		}
		{
			final List<StyleObjectModel> list = Lists.newArrayList(model.getChild());
			list.addAll(getChild());
			this.child = list;
		}
	}

	public Map<String, Object> getBlackboard() {
		if (this.blackboard==null)
			this.blackboard = Maps.newHashMap();
		return this.blackboard;
	}

	public Map<String, String> getProperty() {
		if (this.property==null)
			this.property = Maps.newHashMap();
		return this.property;
	}

	public List<StyleObjectModel> getChild() {
		if (this.child==null)
			this.child = Lists.newArrayList();
		return this.child;
	}
}
