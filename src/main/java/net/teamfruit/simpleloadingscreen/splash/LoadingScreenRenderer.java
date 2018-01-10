package net.teamfruit.simpleloadingscreen.splash;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

import net.minecraft.client.Minecraft;
import net.teamfruit.simpleloadingscreen.CoreInvoke;
import net.teamfruit.simpleloadingscreen.Log;
import net.teamfruit.simpleloadingscreen.Reference;

public class LoadingScreenRenderer {
	private final LoadingScreen loadingScreen;

	public LoadingScreenRenderer(final LoadingScreen loadingScreen) {
		this.loadingScreen = loadingScreen;
	}

	private boolean initialized;

	public void init() {
		this.loadingScreen.moduleDispatcher.dispatchInit();
		this.initialized = true;
	}

	public void draw() {
		if (!this.initialized)
			return;
		this.loadingScreen.moduleDispatcher.dispatchDraw(
				(
						name
				) -> {
					glPushMatrix();
					glEnable(GL_TEXTURE_2D);
				}, (name) -> {
					glDisable(GL_TEXTURE_2D);
					glPopMatrix();
					checkGLError(Reference.NAME+"/modules/"+name);
				});
	}

	/**
	 * Checks for an OpenGL error. If there is one, prints the error ID and error string.
	 */
	public static void checkGLError(final String where) {
		final int i = GL11.glGetError();

		if (i!=0) {
			final String s1 = GLU.gluErrorString(i);
			Log.log.error("########## GL ERROR ##########");
			Log.log.error("@ "+where);
			Log.log.error(i+": "+s1);
		}
	}

	public void finish() {
		if (!this.initialized)
			return;
		this.loadingScreen.moduleDispatcher.dispatchFinish();
	}

	@CoreInvoke
	public static void onInitLoadingScreen() {
		LoadingScreen.instance.renderer.init();
	}

	@CoreInvoke
	public static void onFinishLoadingScreen() {
		LoadingScreen.instance.renderer.finish();
	}

	@CoreInvoke
	public static void onDrawLoadingScreen() {
		glPushMatrix();

		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		final Minecraft mc = Minecraft.getMinecraft();
		glOrtho(0, mc.displayWidth, mc.displayHeight, 0, -1, 1);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();

		LoadingScreen.instance.renderer.draw();

		glPopMatrix();
	}
}
