package org.coolcosmos.cosmicquilt.loader;

import finalforeach.cosmicreach.BlockGame;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.quiltmc.loader.impl.QuiltLoaderImpl;
import org.quiltmc.loader.impl.entrypoint.GamePatch;
import org.quiltmc.loader.impl.entrypoint.GamePatchContext;
import org.quiltmc.loader.impl.launch.common.QuiltLauncher;
import org.quiltmc.loader.impl.util.log.Log;
import org.quiltmc.loader.impl.util.log.LogCategory;

import java.nio.file.Path;
import java.nio.file.Paths;

public class CRInitPatch extends GamePatch {
    @Override
    public void process(QuiltLauncher launcher, GamePatchContext context) {
        // Set up the logger
        CRLogger.setUpLogger();


        // Get the game's entrypoint (set in the GameProvider) from the Quilt loader
        String entrypoint = launcher.getEntrypoint();

        // Check to see if we got only the entrypoint we want, as you can have multiple entrypoints set.
        //  (Usually for client/server differences and the like, but I like to see this as being abusable
        //  and allowing one provider to load multiple games.)
        if (!entrypoint.startsWith("finalforeach.cosmicreach.")) {
            return;
        }

        // Store the entrypoint class as a ClassNode variable so that we can more easily work with it.
        ClassNode mainClass = context.getClassNode(entrypoint);

        if (mainClass == null) {
            throw new RuntimeException("Could not load main class " + entrypoint + "!");
        }

        // Set the initializer method, this is usually not the main method,
        //  it should ideally be placed as close to the game loop as possible without being inside it...*/
        MethodNode initMethod = findMethod(mainClass, (method) -> method.name.equals("main"));

        if (initMethod == null) {
            // Do this if our method doesn't exist in the entrypoint class.
            throw new RuntimeException("Could not find init method in " + entrypoint + "!");
        }
        // Debug log stating that we found our initializer method.
        Log.debug(LogCategory.GAME_PATCH, "Found init method: %s -> %s", entrypoint, mainClass.name);
        // Debug log stating that the method is being patched with our hooks.
        Log.debug(LogCategory.GAME_PATCH, "Patching init method %s%s", initMethod.name, initMethod.desc);


        // TODO: Is this needed?, and if it is, where should it be run?
//        Path runDir = Paths.get(".");
//        QuiltLoaderImpl.INSTANCE.prepareModInit(runDir, new BlockGame());


        context.addPatchedClass(mainClass);
    }
}