package net.teamfruit.simpleloadingscreen.api;

public interface IBlackboard {
	Object getValueGlobal(String globalKey);

	Object getValue(String localKey);

	void setValue(String localKey, Object value);
}
