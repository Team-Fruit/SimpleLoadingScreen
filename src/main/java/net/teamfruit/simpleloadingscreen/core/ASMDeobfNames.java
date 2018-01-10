package net.teamfruit.simpleloadingscreen.core;

import javax.annotation.Nonnull;

import net.teamfruit.simpleloadingscreen.asm.RefName;

public class ASMDeobfNames {
	public static final @Nonnull RefName SplashProgressStart = RefName.name("start");
	public static final @Nonnull RefName SplashProgressFinish = RefName.name("finish");
	public static final @Nonnull RefName ThreadConstructor = RefName.name("<init>");
	public static final @Nonnull RefName RunnableRun = RefName.name("run");
	public static final @Nonnull RefName SplashProgressRendererMutex = RefName.name("mutex");
	public static final @Nonnull RefName SemaphoreAcquireUninterruptibly = RefName.name("acquireUninterruptibly");
	public static final @Nonnull RefName SplashProgressSplashFontRendererConstructor = RefName.name("<init>");
	public static final @Nonnull RefName Gl11GlDisable = RefName.name("glDisable");
}
