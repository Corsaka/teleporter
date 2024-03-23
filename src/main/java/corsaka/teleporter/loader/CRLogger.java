package org.coolcosmos.cosmicquilt.loader;

import org.quiltmc.loader.impl.game.minecraft.Slf4jLogHandler;
import org.quiltmc.loader.impl.util.log.Log;
import uk.org.lidalia.sysoutslf4j.context.SysOutOverSLF4J;

import java.io.File;
import java.io.IOException;
import java.util.logging.LogManager;

public class CRLogger {
    public static void setUpLoggingConfigs() throws IOException {
        // Create the logs folder if it doesn't exist already
        File logFolder = new File("logs");
        if (!logFolder.exists())
            logFolder.mkdir();

        // TODO: Setup cache for old logs here

        // Load our logging properties
        LogManager.getLogManager().readConfiguration(CRLogger.class.getResourceAsStream("/logging.properties"));

        // Redirect System.out.println calls to SLF4J
        SysOutOverSLF4J.sendSystemOutAndErrToSLF4J();
    }

    public static void setUpLogger() {
        try {
            setUpLoggingConfigs();
        } catch (IOException e) {
            System.err.println("Failed to get & setup logger configs. Throwing error...");
            throw new RuntimeException(e);
        }

        // Initializes Quilt's logger
        Log.init(new Slf4jLogHandler(), true);
    }
}
