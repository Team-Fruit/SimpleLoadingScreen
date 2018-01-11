package net.teamfruit.simpleloadingscreen.api;

public interface IConfigProperty<T> {

	void fillDefault();

	T get();

	void set(T value);

	String getName();

	T getDefault();

	Class<T> getType();

}