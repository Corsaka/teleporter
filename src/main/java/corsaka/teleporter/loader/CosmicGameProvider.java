package org.coolcosmos.cosmicquilt.loader;

import org.quiltmc.loader.api.ModContributor;
import org.quiltmc.loader.api.Version;
import org.quiltmc.loader.impl.FormattedException;
import org.quiltmc.loader.impl.entrypoint.GameTransformer;
import org.quiltmc.loader.impl.game.GameProvider;
import org.quiltmc.loader.impl.game.GameProviderHelper;
import org.quiltmc.loader.impl.launch.common.QuiltLauncher;
import org.quiltmc.loader.impl.metadata.qmj.V1ModMetadataBuilder;
import org.quiltmc.loader.impl.util.Arguments;
import org.quiltmc.loader.impl.util.SystemProperties;
import org.quiltmc.loader.impl.util.log.Log;
import org.quiltmc.loader.impl.util.log.LogCategory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/*
 * A custom GameProvider which grants Fabric Loader the necessary information to launch the app.
 */
public class CosmicGameProvider implements GameProvider {
    public static final String[] ENTRYPOINTS = new String[]{
            "finalforeach.cosmicreach.lwjgl3.Lwjgl3Launcher"
    };
    public static final String PROPERTY_GAME_DIRECTORY = "appDirectory";

    private Arguments arguments;
    private Path gameJar;
    private CRVersion version;
    private String entrypoint;

    private static final GameTransformer TRANSFORMERS = new GameTransformer(new CRInitPatch());

    /*
     * Display an identifier for the app.
     */
    @Override
    public String getGameId() {
        return "cosmic_reach";
    }

    /*
     * Display a readable name for the app.
     */
    @Override
    public String getGameName() {
        return "Cosmic Reach";
    }

    /*
     * Display a raw version string that may include build numbers or git hashes.
     */
    @Override
    public String getRawGameVersion() {
        return version.getVersion();
    }

    /*
     * Display a clean version string for display.
     */
    @Override
    public String getNormalizedGameVersion() {
        return version.getVersion();
    }

    /*
     * Provides built-in mods, for example a mod that represents the app itself so
     * that mods can depend on specific versions.
     */
    @Override
    public Collection<BuiltinMod> getBuiltinMods() {
        HashMap<String, String> cosmicReachContacts = new HashMap<>() {{
            put("homepage", "https://finalforeach.itch.io/cosmic-reach");
            put("issues", "https://github.com/FinalForEach/Cosmic-Reach-Issue-Tracker/issues/");
        }};

        V1ModMetadataBuilder cosmicReachMetadata = new V1ModMetadataBuilder() {{
            setId(getGameId());
            setVersion(Version.of(getNormalizedGameVersion()));
            setName(getGameName());
            setGroup("finalforeach");
            setDescription("The base game");

            contributors.add(ModContributor.of("FinalForEach", List.of("developer")));
            contactInformation.forEach(cosmicReachContacts::putIfAbsent);
        }};


        return List.of(new BuiltinMod[] {
                new BuiltinMod(Collections.singletonList(gameJar), cosmicReachMetadata.build())
        });
    }

    /*
     * Provides the full class name of the app's entrypoint.
     */
    @Override
    public String getEntrypoint() {
        return entrypoint;
    }

    /*
     * Provides the directory path where the app's resources (such as config) should
     * be located
     * This is where the `mods` folder will be located.
     */
    @Override
    public Path getLaunchDirectory() {
        if (arguments == null) {
            return Paths.get(".");
        }
        return getLaunchDirectory(arguments);
    }

    private static Path getLaunchDirectory(Arguments arguments) {
        return Paths.get(arguments.getOrDefault(PROPERTY_GAME_DIRECTORY, "."));
    }

    /*
     * Return true if the app needs to be deobfuscated.
     */
    @Override
    public boolean isObfuscated() {
        return false;
    }

    @Override
    public boolean requiresUrlClassLoader() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    /*
     * Parse the arguments, locate the game directory, and return true if the game
     * directory is valid.
     */
    @Override
    public boolean locateGame(QuiltLauncher launcher, String[] args) {
        this.arguments = new Arguments();
        this.arguments.parse(args);

        // Build a list of possible locations for the app jar
        List<String> appLocations = new ArrayList<>();

        // Respect "loader.gameJarPath" if it is set.
        if (System.getProperty(SystemProperties.GAME_JAR_PATH) != null) {
            appLocations.add(System.getProperty(SystemProperties.GAME_JAR_PATH));
        }

        // If the user didnt set the jar location using loader args, then attempt to load the jar file called:
        appLocations.add("./cosmic-reach.jar");

        // Filter the list of possible locations based on whether the file exists.
        List<Path> existingAppLocations = appLocations.stream().map(p -> Paths.get(p).toAbsolutePath().normalize())
                .filter(Files::exists).toList();

        // Filter the list of possible locations based on whether they contain the required entrypoints
        GameProviderHelper.FindResult result = GameProviderHelper.findFirst(existingAppLocations, new HashMap<>(), true, ENTRYPOINTS);

		// If none is found, error out
        if (result == null || result.path == null) {
            // Tell the user we couldn't find the app jar
            String appLocationsString = appLocations.stream().map(p -> (String.format("* %s", Paths.get(p).toAbsolutePath().normalize())))
                    .collect(Collectors.joining("\n"));

            Log.error(LogCategory.GAME_PROVIDER, "Could not locate the application JAR! We looked in: \n" + appLocationsString);

            return false;
        }

        entrypoint = result.name;
        version = new CRVersion(result.path);
        gameJar = result.path;


        return true;
    }

    /*
     * Add additional configuration to the QuiltLauncher, but do not launch your app.
     */
    @Override
    public void initialize(QuiltLauncher launcher) {
        TRANSFORMERS.locateEntrypoints(launcher, Collections.singletonList(gameJar));
    }

    /*
     * Return a GameTransformer that does extra modification on the app's JAR.
     */
    @Override
    public GameTransformer getEntrypointTransformer() {
        return TRANSFORMERS;
    }

    /*
     * Called after transformers were initialized and mods were detected and loaded
     * (but not initialized).
     */
    @Override
    public void unlockClassPath(QuiltLauncher launcher) {
        launcher.addToClassPath(gameJar);
    }

    /*
     * Launch the app in this function. This MUST be done via reflection.
     */
    @Override
    public void launch(ClassLoader loader) {
        String targetClass = entrypoint;
        try {
            Class<?> main = loader.loadClass(targetClass);
            Method method = main.getMethod("main", String[].class);
            method.invoke(null, (Object) this.arguments.toArray());
        } catch (InvocationTargetException e) {
            throw new FormattedException("The game has crashed!", e.getCause());
        } catch (ReflectiveOperationException e) {
            throw new FormattedException("Failed to launch the game", e);
        }
    }

    @Override
    public Arguments getArguments() {
        return this.arguments;
    }

    @Override
    public String[] getLaunchArguments(boolean sanitize) {
        return arguments == null ? new String[0] : arguments.toArray();
    }
}
