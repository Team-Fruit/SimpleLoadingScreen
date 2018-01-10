package net.teamfruit.simpleloadingscreen.core;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import net.teamfruit.simpleloadingscreen.asm.DescHelper;
import net.teamfruit.simpleloadingscreen.asm.MappedType;
import net.teamfruit.simpleloadingscreen.asm.MethodMatcher;

public class SplashProgressVisitor extends ClassVisitor {
	private static class StartMethodVisitor extends MethodVisitor {
		private final @Nonnull String targetOwner;
		private final @Nonnull MethodMatcher targetMethod;
		private final @Nonnull String targetDesc;

		public StartMethodVisitor(final @Nullable MethodVisitor mv, final @Nonnull String baseOwner) {
			super(Opcodes.ASM5, mv);
			this.targetOwner = MappedType.of("java/lang/Thread").name();
			this.targetDesc = DescHelper.toDesc(void.class, "java.lang.Runnable");
			this.targetMethod = new MethodMatcher(this.targetOwner, this.targetDesc, ASMDeobfNames.ThreadConstructor);
		}

		private @Nullable String lastOwner;

		@Override
		public void visitMethodInsn(final int opcode, final @Nullable String owner, final @Nullable String name, final @Nullable String desc, final boolean itf) {
			if (owner!=null&&name!=null&&desc!=null)
				if (opcode==Opcodes.INVOKESPECIAL)
					if (this.targetOwner.equals(owner)&&this.targetMethod.match(name, desc)&&this.targetDesc.equals(desc)) {
						if (this.lastOwner!=null)
							SimpleLoadingScreenTransformer.splashProgressRendererClassName = MappedType.of(this.lastOwner).type().getClassName();
					} else
						this.lastOwner = owner;

			super.visitMethodInsn(opcode, owner, name, desc, itf);
		}
	}

	private static class FinishMethodVisitor extends MethodVisitor {
		public FinishMethodVisitor(final @Nullable MethodVisitor mv, final @Nonnull String baseOwner) {
			super(Opcodes.ASM5, mv);
		}

		@Override
		public void visitCode() {
			// Inject code
			super.visitMethodInsn(Opcodes.INVOKESTATIC, MappedType.of("net.teamfruit.simpleloadingscreen.splash.LoadingScreenRenderer").name(), "onFinishLoadingScreen", DescHelper.toDesc(void.class, new Object[0]), false);
			super.visitCode();
		}
	}

	private final @Nonnull String targetStartOwner;
	private final @Nonnull MethodMatcher targetStartMethod;
	private final @Nonnull String targetStartDesc;
	private final @Nonnull String targetFinishOwner;
	private final @Nonnull MethodMatcher targetFinishMethod;
	private final @Nonnull String targetFinishDesc;

	public SplashProgressVisitor(final @Nonnull String obfClassName, final @Nonnull ClassVisitor cv) {
		super(Opcodes.ASM5, cv);
		this.targetStartOwner = obfClassName;
		this.targetStartDesc = DescHelper.toDesc(void.class, new Object[0]);
		this.targetStartMethod = new MethodMatcher(this.targetStartOwner, this.targetStartDesc, ASMDeobfNames.SplashProgressStart);
		this.targetFinishOwner = obfClassName;
		this.targetFinishDesc = DescHelper.toDesc(void.class, new Object[0]);
		this.targetFinishMethod = new MethodMatcher(this.targetFinishOwner, this.targetFinishDesc, ASMDeobfNames.SplashProgressFinish);

	}

	@Override
	public @Nullable MethodVisitor visitMethod(final int access, final @Nullable String name, final @Nullable String desc, final @Nullable String signature, final @Nullable String[] exceptions) {
		final MethodVisitor parent = super.visitMethod(access, name, desc, signature, exceptions);
		if (name!=null&&desc!=null)
			if (this.targetStartMethod.match(name, desc)&&this.targetStartDesc.equals(desc))
				return new StartMethodVisitor(parent, this.targetStartOwner);
			else if (this.targetFinishMethod.match(name, desc)&&this.targetFinishDesc.equals(desc))
				return new FinishMethodVisitor(parent, this.targetFinishOwner);
		return parent;
	}
}