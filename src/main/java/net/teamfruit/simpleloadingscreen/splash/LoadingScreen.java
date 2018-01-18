package net.teamfruit.simpleloadingscreen.splash;

import static net.teamfruit.simpleloadingscreen.reflect.ReflectionUtil.*;

import java.io.File;
import java.util.List;
import java.util.Properties;

import com.google.common.collect.Lists;

import cpw.mods.fml.client.SplashProgress;
import cpw.mods.fml.common.asm.FMLSanityChecker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.FileResourcePack;
import net.teamfruit.simpleloadingscreen.Log;
import net.teamfruit.simpleloadingscreen.api.IForgeSplashProperties;
import net.teamfruit.simpleloadingscreen.gui.ScreenComponent;
import net.teamfruit.simpleloadingscreen.gui.ScreenFontRenderer;
import net.teamfruit.simpleloadingscreen.modules.ModuleDispatcher;
import net.teamfruit.simpleloadingscreen.modules.ModuleLoader;
import net.teamfruit.simpleloadingscreen.progress.ScreenProgressManager;
import net.teamfruit.simpleloadingscreen.resources.DirectoryInitializer;
import net.teamfruit.simpleloadingscreen.resources.ResourceLoader;
import net.teamfruit.simpleloadingscreen.resources.ScreenConfig;
import net.teamfruit.simpleloadingscreen.style.StyleLoader;
import net.teamfruit.simpleloadingscreen.style.StyleManager;

@SuppressWarnings("deprecation")
public class LoadingScreen {
	public static final LoadingScreen instance = new LoadingScreen();

	public final List<ScreenComponent> components;
	public final IForgeSplashProperties config;
	public final ScreenFontRenderer fontRenderer;
	public final ScreenProgressManager progressManager;
	public final DirectoryInitializer directories;
	public final ModuleDispatcher moduleDispatcher;
	public final ResourceLoader resourceLoader;
	public final StyleLoader styleLoader;
	public final ModuleLoader moduleLoader;
	public final LoadingScreenRenderer renderer;

	public LoadingScreen() {
		final Minecraft mc = Minecraft.getMinecraft();

		this.components = Lists.newArrayList();

		{
			final File configFile = new File(mc.mcDataDir, "config/splash.properties");
			Properties properties;
			try {
				properties = _pfield(SplashProgress.class, $("config")).$get(null);
			} catch (final Exception e) {
				Log.log.fatal(e.getMessage(), e);
				throw new RuntimeException(e);
			}
			this.config = new LoadingScreenConfig(new ScreenConfig(configFile, properties));
		}
		{
			FontRenderer nativeFontRenderer;
			try {
				nativeFontRenderer = _pfield(SplashProgress.class, $("fontRenderer")).$get(null);
			} catch (final Exception e) {
				Log.log.fatal(e.getMessage(), e);
				throw new RuntimeException(e);
			}
			this.fontRenderer = new ScreenFontRenderer(nativeFontRenderer);
		}

		this.progressManager = new ScreenProgressManager();
		this.directories = new DirectoryInitializer(new File(mc.mcDataDir, "config/LoadingScreen"));
		this.directories.init();

		{
			this.resourceLoader = new ResourceLoader();
			this.resourceLoader.addResourcePack(mc.mcDefaultResourcePack);
			this.resourceLoader.addResourcePackLocation(FMLSanityChecker.fmlLocation);
			this.resourceLoader.addResourcePackLocation(new File(mc.mcDataDir, this.config.getResourcePackPath().get()));
			this.resourceLoader.addAssetsLocation(new File(mc.mcDataDir, "resources"));
			this.resourceLoader.addAssetsLocation(this.directories.resourceDir);
		}

		this.styleLoader = new StyleLoader(this, this.directories.themeDir);

		this.moduleDispatcher = new ModuleDispatcher(this);
		this.moduleLoader = new ModuleLoader(this, this.directories.moduleDir);

		for (final FileResourcePack resource : this.moduleLoader.getModuleResources())
			this.resourceLoader.addResourcePack(resource);

		new StyleManager(this).load();

		this.renderer = new LoadingScreenRenderer(this);
	}
}
