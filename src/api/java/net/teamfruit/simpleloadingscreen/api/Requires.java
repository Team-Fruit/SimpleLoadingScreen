package net.teamfruit.simpleloadingscreen.api;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks a module as having a dependency on another module. This will prevent it from being loaded if its dependency
 * isn't loaded.
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Requires {

	/**
	 * Gets the fully qualified class name of the required module. Ex. "com.foo.Bar"
	 */
	String value();
}
