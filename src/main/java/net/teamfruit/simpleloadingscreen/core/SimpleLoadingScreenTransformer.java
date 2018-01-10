package net.teamfruit.simpleloadingscreen.core;

import javax.annotation.Nullable;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import net.minecraft.launchwrapper.IClassTransformer;
import net.teamfruit.simpleloadingscreen.Log;
import net.teamfruit.simpleloadingscreen.asm.VisitorHelper;
import net.teamfruit.simpleloadingscreen.asm.VisitorHelper.TransformProvider;

public class SimpleLoadingScreenTransformer implements IClassTransformer {
	public static @Nullable String splashProgressClassName;
	public static @Nullable String splashProgressRendererClassName;

	@Override
	public @Nullable byte[] transform(final @Nullable String name, final @Nullable String transformedName, final @Nullable byte[] bytes) {
		if (bytes==null||name==null||transformedName==null)
			return bytes;

		if (transformedName.equals("net.minecraftforge.fml.client.SplashProgress")||transformedName.equals("cpw.mods.fml.client.SplashProgress")) {
			splashProgressClassName = transformedName;
			return VisitorHelper.apply(bytes, name, new TransformProvider(ClassWriter.COMPUTE_FRAMES) {
				@Override
				public ClassVisitor createVisitor(final String name, final ClassVisitor cv) {
					Log.log.info(String.format("Patching SplashProgress (class: %s)", name));
					return new SplashProgressVisitor(name, cv);
				}
			});
		}

		if (transformedName.equals(splashProgressRendererClassName))
			return VisitorHelper.apply(bytes, name, new TransformProvider(ClassWriter.COMPUTE_FRAMES) {
				@Override
				public ClassVisitor createVisitor(final String name, final ClassVisitor cv) {
					Log.log.info(String.format("Patching SplashProgress$Renderer (class: %s)", name));
					return new SplashProgressRendererVisitor(name, cv);
				}
			});

		return bytes;
	}
}