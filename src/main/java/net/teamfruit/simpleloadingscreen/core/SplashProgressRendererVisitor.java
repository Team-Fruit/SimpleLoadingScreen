package net.teamfruit.simpleloadingscreen.core;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import net.teamfruit.simpleloadingscreen.asm.DescHelper;
import net.teamfruit.simpleloadingscreen.asm.FieldMatcher;
import net.teamfruit.simpleloadingscreen.asm.MappedType;
import net.teamfruit.simpleloadingscreen.asm.MethodMatcher;

public class SplashProgressRendererVisitor extends ClassVisitor {
	private static class RunMethodVisitor extends MethodVisitor {
		private final @Nonnull String targetMutexOwner;
		private final @Nonnull FieldMatcher targetMutexField;
		private final @Nonnull String targetMutexDesc;
		private final @Nonnull String targetAcquireOwner;
		private final @Nonnull MethodMatcher targetAcquireMethod;
		private final @Nonnull String targetAcquireDesc;
		private final @Nonnull String targetFontOwner;
		private final @Nonnull MethodMatcher targetFontMethod;
		private final @Nonnull String targetFontDesc;
		private final @Nonnull String targetGlDisableOwner;
		private final @Nonnull MethodMatcher targetGlDisableMethod;
		private final @Nonnull String targetGlDisableDesc;

		public RunMethodVisitor(final @Nullable MethodVisitor mv, final @Nonnull String baseOwner) {
			super(Opcodes.ASM5, mv);
			final String clsName = SimpleLoadingScreenTransformer.splashProgressClassName;
			this.targetMutexOwner = clsName==null ? "" : MappedType.of(clsName).name();
			this.targetMutexDesc = DescHelper.toDesc("java.util.concurrent.Semaphore");
			this.targetMutexField = new FieldMatcher(this.targetMutexOwner, this.targetMutexDesc, ASMDeobfNames.SplashProgressRendererMutex);
			this.targetAcquireOwner = MappedType.of("java.util.concurrent.Semaphore").name();
			this.targetAcquireDesc = DescHelper.toDesc(void.class, new Object[0]);
			this.targetAcquireMethod = new MethodMatcher(this.targetAcquireOwner, this.targetAcquireDesc, ASMDeobfNames.SemaphoreAcquireUninterruptibly);
			this.targetFontOwner = MappedType.of(SimpleLoadingScreenTransformer.splashProgressClassName+"$SplashFontRenderer").name();
			this.targetFontDesc = DescHelper.toDesc(void.class, new Object[0]);
			this.targetFontMethod = new MethodMatcher(this.targetFontOwner, this.targetFontDesc, ASMDeobfNames.SplashProgressSplashFontRendererConstructor);
			this.targetGlDisableOwner = MappedType.of("org.lwjgl.opengl.GL11").name();
			this.targetGlDisableDesc = DescHelper.toDesc(void.class, int.class);
			this.targetGlDisableMethod = new MethodMatcher(this.targetGlDisableOwner, this.targetGlDisableDesc, ASMDeobfNames.Gl11GlDisable);
		}

		private String targetMutexFieldName;
		private boolean targetFontMethodAfter;

		@Override
		public void visitFieldInsn(final int opcode, final String owner, final String name, final String desc) {
			if (owner!=null&&name!=null&&desc!=null)
				if (opcode==Opcodes.GETSTATIC&&this.targetMutexOwner.equals(owner)&&this.targetMutexField.match(name, desc)&&this.targetMutexDesc.equals(desc))
					this.targetMutexFieldName = name;

			super.visitFieldInsn(opcode, owner, name, desc);
		}

		@Override
		public void visitMethodInsn(final int opcode, final @Nullable String owner, final @Nullable String name, final @Nullable String desc, final boolean itf) {
			if (owner!=null&&name!=null&&desc!=null)
				if (opcode==Opcodes.INVOKEVIRTUAL&&this.targetAcquireOwner.equals(owner)&&this.targetAcquireMethod.match(name, desc)&&this.targetAcquireDesc.equals(desc)) {
					if (this.targetMutexFieldName!=null) {
						// Erase previous getstatic code
						super.visitInsn(Opcodes.POP);
						// Inject code
						super.visitMethodInsn(Opcodes.INVOKESTATIC, MappedType.of("net.teamfruit.simpleloadingscreen.splash.LoadingScreenRenderer").name(), "onDrawLoadingScreen", DescHelper.toDesc(void.class, new Object[0]), false);
						// Add previous getstatic code
						super.visitFieldInsn(Opcodes.GETSTATIC, this.targetMutexOwner, this.targetMutexFieldName, this.targetMutexDesc);
						// Add current method
						super.visitMethodInsn(opcode, owner, name, desc, itf);
						this.targetMutexFieldName = null;
						return;
					}
				} else if (opcode==Opcodes.INVOKESTATIC&&this.targetGlDisableOwner.equals(owner)&&this.targetGlDisableMethod.match(name, desc)&&this.targetGlDisableDesc.equals(desc)) {
					if (this.targetFontMethodAfter) {
						// Add current method
						super.visitMethodInsn(opcode, owner, name, desc, itf);
						// Inject code
						super.visitMethodInsn(Opcodes.INVOKESTATIC, MappedType.of("net.teamfruit.simpleloadingscreen.splash.LoadingScreenRenderer").name(), "onInitLoadingScreen", DescHelper.toDesc(void.class, new Object[0]), false);
						this.targetFontMethodAfter = false;
						return;
					}
				} else if (opcode==Opcodes.INVOKESPECIAL&&this.targetFontOwner.equals(owner)&&this.targetFontMethod.match(name, desc)&&this.targetFontDesc.equals(desc))
					this.targetFontMethodAfter = true;

			super.visitMethodInsn(opcode, owner, name, desc, itf);
		}
	}

	private final @Nonnull String targetRunOwner;
	private final @Nonnull MethodMatcher targetRunMethod;
	private final @Nonnull String targetRunDesc;

	public SplashProgressRendererVisitor(final @Nonnull String obfClassName, final @Nonnull ClassVisitor cv) {
		super(Opcodes.ASM5, cv);
		this.targetRunOwner = obfClassName;
		this.targetRunDesc = DescHelper.toDesc(void.class, new Object[0]);
		this.targetRunMethod = new MethodMatcher(this.targetRunOwner, this.targetRunDesc, ASMDeobfNames.RunnableRun);
	}

	@Override
	public @Nullable MethodVisitor visitMethod(final int access, final @Nullable String name, final @Nullable String desc, final @Nullable String signature, final @Nullable String[] exceptions) {
		final MethodVisitor parent = super.visitMethod(access, name, desc, signature, exceptions);
		if (name!=null&&desc!=null)
			if (this.targetRunMethod.match(name, desc)&&this.targetRunDesc.equals(desc))
				return new RunMethodVisitor(parent, this.targetRunOwner);
		return parent;
	}
}