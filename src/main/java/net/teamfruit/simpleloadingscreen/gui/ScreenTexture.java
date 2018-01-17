package net.teamfruit.simpleloadingscreen.gui;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.IntBuffer;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.lwjgl.BufferUtils;

import cpw.mods.fml.client.SplashProgress;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.ResourceLocation;
import net.teamfruit.simpleloadingscreen.Log;
import net.teamfruit.simpleloadingscreen.api.renderer.ITexture;
import net.teamfruit.simpleloadingscreen.resources.ResourceLoader;

@SuppressWarnings("deprecation")
public class ScreenTexture implements ITexture {
	private static final IntBuffer buf = BufferUtils.createIntBuffer(4*1024*1024);

	private final ResourceLocation location;
	private final int name;
	private final int width;
	private final int height;
	private final int frames;
	private final int size;

	public ScreenTexture(final ResourceLoader loader, final ResourceLocation location) {
		this.location = location;
		int name;
		int width;
		int height;
		int frames;
		int size;
		try (InputStream s = loader.open(location);) {
			final ImageInputStream stream = ImageIO.createImageInputStream(s);
			final Iterator<ImageReader> readers = ImageIO.getImageReaders(stream);
			if (!readers.hasNext())
				throw new IOException("No suitable reader found for image"+location);
			final ImageReader reader = readers.next();
			reader.setInput(stream);
			frames = reader.getNumImages(true);
			final BufferedImage[] images = new BufferedImage[frames];
			for (int i = 0; i<frames; i++)
				images[i] = reader.read(i);
			reader.dispose();
			size = 1;
			width = images[0].getWidth();
			height = images[0].getHeight();
			while (size/width*(size/height)<frames)
				size *= 2;
			glEnable(GL_TEXTURE_2D);
			synchronized (SplashProgress.class) {
				name = glGenTextures();
				glBindTexture(GL_TEXTURE_2D, name);
			}
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
			glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, size, size, 0, GL_BGRA, GL_UNSIGNED_INT_8_8_8_8_REV, (IntBuffer) null);
			SplashProgress.checkGLError("Texture creation");
			for (int i = 0; i*(size/width)<frames; i++)
				for (int j = 0; i*(size/width)+j<frames&&j<size/width; j++) {
					buf.clear();
					final BufferedImage image = images[i*(size/width)+j];
					for (int k = 0; k<height; k++)
						for (int l = 0; l<width; l++)
							buf.put(image.getRGB(l, k));
					buf.position(0).limit(width*height);
					glTexSubImage2D(GL_TEXTURE_2D, 0, j*width, i*height, width, height, GL_BGRA, GL_UNSIGNED_INT_8_8_8_8_REV, buf);
					SplashProgress.checkGLError("Texture uploading");
				}
			glBindTexture(GL_TEXTURE_2D, 0);
			glDisable(GL_TEXTURE_2D);
		} catch (final IOException e) {
			Log.log.warn("Could not load textures: ", e);
			frames = 1;
			size = 1;
			width = 16;
			height = 16;
			while (size/width*(size/height)<frames)
				size *= 2;
			name = TextureUtil.missingTexture.getGlTextureId();
		}
		this.name = name;
		this.width = width;
		this.height = height;
		this.frames = frames;
		this.size = size;
	}

	@Override
	public ResourceLocation getLocation() {
		return this.location;
	}

	@Override
	public int getName() {
		return this.name;
	}

	@Override
	public int getWidth() {
		return this.width;
	}

	@Override
	public int getHeight() {
		return this.height;
	}

	@Override
	public int getFrames() {
		return this.frames;
	}

	@Override
	public int getSize() {
		return this.size;
	}

	@Override
	public void bind() {
		glBindTexture(GL_TEXTURE_2D, this.name);
	}

	@Override
	public void delete() {
		glDeleteTextures(this.name);
	}

	@Override
	public float getU(final int frame, final float u) {
		return this.width*(frame%(this.size/this.width)+u)/this.size;
	}

	@Override
	public float getV(final int frame, final float v) {
		return this.height*(frame/(this.size/this.width)+v)/this.size;
	}

	@Override
	public void texCoord(final int frame, final float u, final float v) {
		glTexCoord2f(getU(frame, u), getV(frame, v));
	}
}
