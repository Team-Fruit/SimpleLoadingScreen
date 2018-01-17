package net.teamfruit.simpleloadingscreen.api;

@Deprecated
public interface IBlackboard {
	Object getValueGlobal(String globalKey);

	Object getValue(String localKey);

	void setValue(String localKey, Object value);
}
