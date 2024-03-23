package org.coolcosmos.cosmicquilt.loader;

import org.quiltmc.loader.impl.util.log.Log;
import org.quiltmc.loader.impl.util.log.LogCategory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class CRVersion {
    private final String version;

    /**
     * Parses Cosmic Reach's game version
     */
    public CRVersion(Path path) {
        String version = "0.0.0";

        try {
            ZipFile zipFile = new ZipFile(path.toFile());
            ZipEntry entry = zipFile.getEntry("build_assets/version.txt"); // Assumed file containing version info in cosmic reach

            if (entry != null) {
                InputStream inputStream = zipFile.getInputStream(entry);
                Scanner scanner = new Scanner(inputStream);

                while (scanner.hasNextLine()) {
                    version = scanner.nextLine();
                }

                scanner.close();
                inputStream.close();
            } else {
                Log.error(LogCategory.DISCOVERY, "The version file wasn't found!");
            }

            zipFile.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.version = version;
    }

    public String getVersion() {
        return version;
    }
}
