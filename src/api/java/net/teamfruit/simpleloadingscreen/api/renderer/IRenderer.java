package net.teamfruit.simpleloadingscreen.api.renderer;

public interface IRenderer {
	/**
	 * Called to initialize the module.
	 */
	default void init(final IScreen screen) {
	}

	/**
	 * <p>Called to draw the module.
	 *
	 * <p>{@link org.lwjgl.opengl.GL11#GL_TEXTURE_2D GL_TEXTURE_2D} is enabled before drawing and disabled after doing.
	 */
	void draw(IScreen screen);

	/**
	 * Called to cleanup the module.
	 */
	default void finish(final IScreen screen) {
	}
}
