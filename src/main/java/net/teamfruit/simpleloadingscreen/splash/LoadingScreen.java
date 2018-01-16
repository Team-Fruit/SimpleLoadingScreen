package net.teamfruit.simpleloadingscreen.splash;

import static net.teamfruit.simpleloadingscreen.reflect.ReflectionUtil.*;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import cpw.mods.fml.client.SplashProgress;
import cpw.mods.fml.common.asm.FMLSanityChecker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.resources.FileResourcePack;
import net.teamfruit.simpleloadingscreen.Log;
import net.teamfruit.simpleloadingscreen.api.IForgeSplashProperties;
import net.teamfruit.simpleloadingscreen.gui.ScreenBlackboard;
import net.teamfruit.simpleloadingscreen.gui.ScreenComponent;
import net.teamfruit.simpleloadingscreen.gui.ScreenFontRenderer;
import net.teamfruit.simpleloadingscreen.modules.ModuleDispatcher;
import net.teamfruit.simpleloadingscreen.modules.ModuleLoader;
import net.teamfruit.simpleloadingscreen.progress.ScreenProgressManager;
import net.teamfruit.simpleloadingscreen.resources.ResourceLoader;
import net.teamfruit.simpleloadingscreen.resources.ScreenConfig;
import net.teamfruit.simpleloadingscreen.style.StyleLoader;

@SuppressWarnings("deprecation")
public class LoadingScreen {
	public static final LoadingScreen instance = new LoadingScreen();

	public final Map<String, Object> blackboard;
	public final ScreenBlackboard globalBlackboard;
	public final List<ScreenComponent> components;
	public final IForgeSplashProperties config;
	public final ScreenFontRenderer fontRenderer;
	public final ScreenProgressManager progressManager;
	public final File loadingScreenDir;
	public final File loadingScreenModuleConfigDir;
	public final ModuleDispatcher moduleDispatcher;
	public final ResourceLoader resourceLoader;
	public final StyleLoader styleLoader;
	public final ModuleLoader moduleLoader;
	public final LoadingScreenRenderer renderer;

	public LoadingScreen() {
		final Minecraft mc = Minecraft.getMinecraft();

		this.blackboard = Maps.newHashMap();
		this.globalBlackboard = new ScreenBlackboard(this.blackboard);
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
		this.loadingScreenDir = new File(mc.mcDataDir, "config/LoadingScreen");
		this.loadingScreenDir.mkdirs();
		this.loadingScreenModuleConfigDir = new File(this.loadingScreenDir, "config");
		this.loadingScreenModuleConfigDir.mkdirs();

		{
			this.resourceLoader = new ResourceLoader();
			this.resourceLoader.addResourcePack(mc.mcDefaultResourcePack);
			this.resourceLoader.addResourcePackLocation(FMLSanityChecker.fmlLocation);
			this.resourceLoader.addResourcePackLocation(new File(mc.mcDataDir, this.config.getResourcePackPath().get()));
			this.resourceLoader.addAssetsLocation(new File(mc.mcDataDir, "resources"));
			final File resourceDir = new File(this.loadingScreenDir, "resources");
			resourceDir.mkdirs();
			this.resourceLoader.addAssetsLocation(resourceDir);
		}

		this.styleLoader = new StyleLoader(this, new File(this.loadingScreenDir, "themes"));

		this.moduleDispatcher = new ModuleDispatcher(this);
		this.moduleLoader = new ModuleLoader(this, new File(this.loadingScreenDir, "modules"));

		for (final FileResourcePack resource : this.moduleLoader.getModuleResources())
			this.resourceLoader.addResourcePack(resource);

		this.renderer = new LoadingScreenRenderer(this);
	}
}
