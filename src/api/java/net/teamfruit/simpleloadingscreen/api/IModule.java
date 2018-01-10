package net.teamfruit.simpleloadingscreen.api;

/**
 * A SimpleLoadingScreen "module".
 *
 * <p>When enabled, modules are registered as event listeners.
 *
 * <p>Modules <b>MUST</b> have a default constructor.
 */
public interface IModule {

	/**
	 * Called to enable the module. A new instance of the module is created for each call to this method.
	 *
	 * @return Whether the module was successfully enabled.
	 */
	boolean enable(IManager manager);

	/**
	 * Called to disable the module.
	 *
	 * <p>It is important that the module implementation attempt to do any and all disable in this method in order for
	 * the module to properly unload. The module loader can make no guarantees about the unloading of modules if they
	 * do not properly implement this method.
	 */
	default void disable(final IManager manager) {
	}

	/**
	 * <p>Gets the ID of the module.
	 *
	 * <p>ID <b>MUST</b> be lowercase alphanumeric characters or underscore
	 *
	 * @return The ID of the module.
	 */
	String getID();

	/**
	 * Gets the name of the module.
	 *
	 * @return The name of the module.
	 */
	default String getName() {
		return getID();
	}

	/**
	 * Gets the author(s) of the module.
	 *
	 * @return The author(s) of the module.
	 */
	String getAuthor();

	/**
	 * Gets the version of the module.
	 *
	 * @return The version of the module.
	 */
	String getVersion();

	/**
	 * Gets the minimum required version of SimpleLoadingScreen for the module to function.
	 *
	 * @return The minimum required version, i.e. "1.0.0". if null present, version is not checked.
	 */
	default String getMinimumVersion() {
		return null;
	}
}
