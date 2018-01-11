package net.teamfruit.simpleloadingscreen.modules;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;
import java.util.stream.Collectors;

import org.apache.commons.io.filefilter.FileFilterUtils;

import net.teamfruit.simpleloadingscreen.Log;
import net.teamfruit.simpleloadingscreen.Reference;
import net.teamfruit.simpleloadingscreen.api.IManager;
import net.teamfruit.simpleloadingscreen.api.IModule;
import net.teamfruit.simpleloadingscreen.api.Requires;
import net.teamfruit.simpleloadingscreen.gui.ScreenManager;
import net.teamfruit.simpleloadingscreen.splash.LoadingScreen;

/**
 * Manages loading and unloading modules for a SimpleLoadingScreen client.
 */
public class ModuleLoader {

	/**
	 * The classes of the modules loaded by the loader.
	 */
	protected final List<Class<? extends IModule>> modules = new CopyOnWriteArrayList<>();

	private final LoadingScreen loadingScreen;
	/**
	 * The client which owns the module loader.
	 */
	private final ModuleDispatcher dispatcher;
	/**
	 * The modules loaded by the loader.
	 */
	private final List<ModuleContainer> loadedModules = new CopyOnWriteArrayList<>();
	/**
	 * The modules loaded by the loader.
	 */
	private final ModuleClassLoader moduleClassLoader;

	public ModuleLoader(final LoadingScreen loadingScreen, final File modulesDir) {
		this.loadingScreen = loadingScreen;
		this.moduleClassLoader = new ModuleClassLoader(getClass().getClassLoader());
		this.dispatcher = loadingScreen.moduleDispatcher;

		if (modulesDir.exists()) {
			if (!modulesDir.isDirectory())
				throw new RuntimeException(modulesDir.getName()+" isn't a directory!");
		} else if (!modulesDir.mkdirs())
			throw new RuntimeException("Error creating "+modulesDir.getName()+" directory");

		final File[] files = modulesDir.listFiles((FilenameFilter) FileFilterUtils.suffixFileFilter("jar"));
		if (files!=null&&files.length>0) {
			Log.log.info("Attempting to load {} external module(s)...", files.length);
			loadExternalModules(new ArrayList<>(Arrays.asList(files)));
		}

		for (final Class<? extends IModule> clazz : this.modules)
			try {
				final ModuleContainer module = new ModuleContainer(loadingScreen, clazz.newInstance());
				Log.log.info("Loading module {} v{} by {}", module.getModule().getName(), module.getModule().getVersion(), module.getModule().getAuthor());
				if (canModuleLoad(module))
					this.loadedModules.add(module);
				else
					Log.log.warn("Skipped loading of module {} (expected SimpleLoadingScreen v{} instead of v{})", module.getModule().getName(), module.getModule().getMinimumVersion(), Reference.VERSION);
			} catch (InstantiationException|IllegalAccessException e) {
				Log.log.error("Unable to load module "+clazz.getName()+"!", e);
			}

		final List<ModuleContainer> toLoad = new CopyOnWriteArrayList<>(this.loadedModules);
		while (toLoad.size()>0)
			for (final ModuleContainer module : toLoad)
				if (loadModule(module))
					toLoad.remove(module);
	}

	/**
	 * Gets the modules loaded by the module loader.
	 *
	 * @return The modules loaded.
	 */
	public List<ModuleContainer> getLoadedModules() {
		return this.loadedModules;
	}

	/**
	 * Gets the module classes which will be or have been loaded. These may or may not be enabled in a given module
	 * instance.
	 *
	 * @return The module classes.
	 */
	public List<Class<? extends IModule>> getModules() {
		return this.modules;
	}

	/**
	 * Manually loads a module.
	 *
	 * @param module The module to load.
	 * @return Whether the module was successfully loaded.
	 */
	public boolean loadModule(final ModuleContainer module) {
		if (!this.loadedModules.contains(module)&&!canModuleLoad(module))
			return false;
		final Class<? extends IModule> clazz = module.getModule().getClass();
		if (clazz.isAnnotationPresent(Requires.class)) {
			final Requires annotation = clazz.getAnnotation(Requires.class);
			if (!hasDependency(this.loadedModules, annotation.value()))
				return false;
		}
		final boolean enabled = module.enable();
		if (enabled) {
			this.dispatcher.register(module);
			if (!this.loadedModules.contains(module))
				this.loadedModules.add(module);
		}

		return true;
	}

	/**
	 * Manually unloads a module.
	 *
	 * @param module The module to unload.
	 */
	public void unloadModule(final ModuleContainer module) {
		this.loadedModules.remove(module);
		final IManager manager = new ScreenManager(this.loadingScreen, module);
		module.getModule().disable(manager);
		this.dispatcher.unregister(module);

		this.loadedModules.removeIf(mod -> {
			final Class<? extends IModule> clazz = module.getModule().getClass();
			if (clazz.isAnnotationPresent(Requires.class)) {
				final Requires annotation = clazz.getAnnotation(Requires.class);
				if (annotation.value().equals(module.getClass().getName())) {
					unloadModule(mod);
					return true;
				}
			}
			return false;
		});
	}

	/**
	 * Gets whether the given list of modules has a module with the given class name.
	 *
	 * @param modules The modules to check.
	 * @param className The class name to search for.
	 * @return Whether the given list of modules has a module with the given class name.
	 */
	private boolean hasDependency(final List<ModuleContainer> modules, final String className) {
		for (final ModuleContainer module : modules)
			if (module.getModule().getClass().getName().equals(className))
				return true;
		return false;
	}

	/**
	 * Gets whether the given module can be loaded.
	 *
	 * @param module The module to check.
	 * @return Whether the given module can be loaded.
	 */
	private boolean canModuleLoad(final ModuleContainer module) {
		if (!module.getModule().getID().toLowerCase().equals(module.getModule().getID())) {
			Log.log.error("Module {} has incorrect id! id MUST be lowercase ({})", module.getModule().getName(), module.getModule().getID());
			return false;
		}
		String[] versions;
		String[] simpleLoadingScreenVersion;
		try {
			final String stringVersion = module.getModule().getMinimumVersion();
			if (stringVersion==null)
				return true;
			versions = stringVersion.toLowerCase(Locale.ROOT).replace("-snapshot", "").split("\\.");
			simpleLoadingScreenVersion = Reference.VERSION.toLowerCase(Locale.ROOT).replace("-snapshot", "").split("\\.");

			for (int i = 0; i<Math.min(versions.length, 2); i++) {
				int modVersion;
				try {
					modVersion = Integer.parseInt(simpleLoadingScreenVersion[i]);
				} catch (final NumberFormatException e) {
					return true;
				}
				if (Integer.parseInt(versions[i])>modVersion)
					return false;
			}
		} catch (final NumberFormatException e) {
			Log.log.error("Module {} has incorrect minimum SimpleLoadingScreen version syntax! ({})", module.getModule().getName(), module.getModule().getMinimumVersion());
			return false;
		}
		return true;
	}

	/**
	 * Loads a jar file and automatically adds any modules.
	 * To avoid high overhead recursion, specify the attribute "SimpleLoadingScreen-ModuleClass" in your jar manifest.
	 * Multiple classes should be separated by a semicolon ";".
	 *
	 * @param file The jar file to load.
	 */
	@SuppressWarnings("unchecked")
	public synchronized void loadExternalModules(final File file) {
		if (file.isFile()&&file.getName().endsWith(".jar"))
			try (JarFile jar = new JarFile(file)) {
				final Manifest man = jar.getManifest();
				final String moduleAttrib = man.getMainAttributes().getValue("SimpleLoadingScreen-ModuleClass");
				String[] moduleClasses = new String[0];
				if (moduleAttrib!=null)
					moduleClasses = moduleAttrib.split(";");
				// Executes would should be URLCLassLoader.addUrl(file.toURI().toURL());
				final URL url = file.toURI().toURL();
				for (final URL it : Arrays.asList(this.moduleClassLoader.getURLs()))
					if (it.equals(url))
						return;
				this.moduleClassLoader.addURL(url);
				if (moduleClasses.length==0) { // If the Module Developer has not specified the Implementing Class, revert to recursive search
					// Scans the jar file for classes which have IModule as a super class
					final List<String> classes = new ArrayList<>();
					jar.stream().filter(jarEntry -> !jarEntry.isDirectory()&&jarEntry.getName().endsWith(".class")).map(path -> path.getName().replace('/', '.').substring(0, path.getName().length()-".class".length())).forEach(classes::add);
					for (final String clazz : classes)
						try {
							final Class<?> classInstance = loadClass(clazz);
							if (IModule.class.isAssignableFrom(classInstance)&&!classInstance.equals(IModule.class))
								addModuleClass((Class<? extends IModule>) classInstance);
						} catch (final NoClassDefFoundError ignored) {
							/* This can happen. Looking recursively looking through the classpath is hackish... */ }
				} else
					for (final String moduleClass : moduleClasses) {
						Log.log.info("Loading Class from Manifest Attribute: {}", moduleClass);
						final Class<?> classInstance = loadClass(moduleClass);
						if (IModule.class.isAssignableFrom(classInstance))
							addModuleClass((Class<? extends IModule>) classInstance);
					}
			} catch (IOException|ClassNotFoundException e) {
				Log.log.error("Unable to load module "+file.getName()+"!", e);
			}
	}

	/**
	 * Recursively loads the parents of subclasses in order to avoid class loader errors.
	 */
	private Class<?> loadClass(final String clazz) throws ClassNotFoundException {
		if (clazz.contains("$")&&clazz.substring(0, clazz.lastIndexOf("$")).length()>0)
			try {
				loadClass(clazz.substring(0, clazz.lastIndexOf("$")));
			} catch (final ClassNotFoundException ignored) {
			} // If the parent class doesn't exist then it is safe to instantiate the child
		return Class.forName(clazz, true, this.moduleClassLoader);
	}

	/**
	 * Loads a list of jar files and automatically resolves any dependency issues.
	 *
	 * @param files The jar files to load.
	 */
	public void loadExternalModules(final List<File> files) {
		final List<File> independents = new ArrayList<>();
		final List<File> dependents = new ArrayList<>();

		files.forEach((file) -> {
			try {
				if (getModuleRequires(file).length>0)
					dependents.add(file);
				else
					independents.add(file);
			} catch (final IOException e) {
				Log.log.error("SimpleLoadingScreen Internal Exception");
			}
		});

		independents.forEach(this::loadExternalModules);

		final List<File> noLongerDependents = dependents.stream().filter(jarFile -> { // loads all dependents whose requirements have been met already
			try {
				final String[] moduleRequires = getModuleRequires(jarFile);
				final List<Class<?>> classes = new ArrayList<>();
				for (final String clazz : moduleRequires)
					classes.add(Class.forName(clazz, true, this.moduleClassLoader));
				return classes.size()==moduleRequires.length;
			} catch (final Exception e) {
				return false;
			}
		}).collect(Collectors.toList());
		dependents.removeAll(noLongerDependents);
		noLongerDependents.forEach(this::loadExternalModules);

		final int retryAttempts = dependents.size();
		for (int i = 0; i<retryAttempts; i++) {
			dependents.removeIf(file -> { // Filters out all usable files
				boolean loaded = false;
				try {
					final String[] required = getModuleRequires(file);
					for (final String clazz : required) {
						try {
							Class.forName(clazz, true, this.moduleClassLoader);
							loaded = true;
						} catch (final ClassNotFoundException ignored) {
						}

						if (!loaded)
							loaded = findFileForClass(files, clazz)!=null;

						if (!loaded)
							break;
					}
				} catch (final IOException ignored) {
				}

				if (loaded)
					loadExternalModules(file);

				return loaded;
			});

			if (dependents.isEmpty())
				break;
		}

		if (dependents.size()>0)
			Log.log.warn("Unable to load {} modules!", dependents.size());
	}

	/**
	 * Gets the <code>Module-Requires</code> attribute list from the given jar file manifest.
	 *
	 * @param file The jar file to extract the manifest attribute from.
	 * @return The value of the attribute.
	 * @throws IOException If the jar file read operation fails.
	 */
	private String[] getModuleRequires(final File file) throws IOException {
		try (final JarFile jarFile = new JarFile(file);) {
			final Manifest manifest = jarFile.getManifest();
			final Attributes.Name moduleRequiresLower = new Attributes.Name("module-requires"); //TODO remove
			final Attributes.Name moduleRequiresUpper = new Attributes.Name("Module-Requires");
			if (
				manifest!=null&&manifest.getMainAttributes()!=null //TODO remove
						&&manifest.getMainAttributes().containsKey(moduleRequiresLower)
			) {
				final String value = manifest.getMainAttributes().getValue(moduleRequiresLower);
				Log.log.warn("File {} uses the 'module-requires' attribute instead of 'Module-Requires', please rename the attribute!", file.getName());
				return value.contains(";") ? value.split(";") : new String[] { value };
			} else if (
				manifest!=null&&manifest.getMainAttributes()!=null
						&&manifest.getMainAttributes().containsKey(moduleRequiresUpper)
			) {
				final String value = manifest.getMainAttributes().getValue(moduleRequiresUpper);
				return value.contains(";") ? value.split(";") : new String[] { value };
			} else
				return new String[0];
		}
	}

	/**
	 * Gets the jar file which contains a class with the given class name.
	 *
	 * @param files The jar files to search.
	 * @param clazz The class name to search for.
	 * @return The jar file which contains a class with the given class name (or null if one was not found).
	 */
	private File findFileForClass(final List<File> files, final String clazz) {
		return files.stream().filter((file) -> {
			try (final JarFile jarFile = new JarFile(file);) {
				return jarFile.getJarEntry(clazz.replaceAll("\\.", File.pathSeparator)+".class")!=null;
			} catch (final IOException e) {
				return false;
			}
		}).findFirst().orElse(null);
	}

	/**
	 * Manually adds a module class to be considered for loading.
	 *
	 * @param classInstance The module class.
	 */
	public void addModuleClass(final Class<? extends IModule> classInstance) {
		if (
			!Modifier.isAbstract(classInstance.getModifiers())
					&&!Modifier.isInterface(classInstance.getModifiers())
					&&!this.modules.contains(classInstance)
		)
			this.modules.add(classInstance);
	}

	public static class ModuleClassLoader extends URLClassLoader {
		public ModuleClassLoader(final ClassLoader parent) {
			super(new URL[0], parent);
		}

		@Override
		public void addURL(final URL url) {
			super.addURL(url);
		}
	}
}
