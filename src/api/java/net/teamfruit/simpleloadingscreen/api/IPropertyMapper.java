package net.teamfruit.simpleloadingscreen.api;

import java.util.Map;

public interface IPropertyMapper {
	void map(Map<String, String> sourceProperty, Map<String, Object> destBlackboard);
}
