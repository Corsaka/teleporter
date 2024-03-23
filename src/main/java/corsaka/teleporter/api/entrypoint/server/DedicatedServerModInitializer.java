package org.coolcosmos.cosmicquilt.api.entrypoint.server;

import org.quiltmc.loader.api.ModContainer;

/* *
 * A mod initializer which is run only on {@link net.fabricmc.api.EnvType#SERVER}.
 * <p>
 * In {@code quilt.mod.json}, the entrypoint is defined with {@value #ENTRYPOINT_KEY} key.
 * <p>
 * Currently, it is executed in {@link net.minecraft.server.Main#main(String[])}, just after the EULA has been agreed to.
 *
 * @see org.coolcosmos.cosmicquilt.api.entrypoint.ModInitializer
 * @see org.coolcosmos.cosmicquilt.entrypoint.client.ClientModInitializer
 */

/**
 * NOT IMPLEMENTED!!!
 * (waiting on Cosmic Reach server-side support)
 */
public interface DedicatedServerModInitializer {
    /**
     * Represents the key which this entrypoint is defined with, whose value is {@value}.
     */
    String ENTRYPOINT_KEY = "server_init";

    /**
     * Runs the mod initializer on the dedicated server environment.
     *
     * @param mod the mod which is initialized
     */
    void onInitializeServer(ModContainer mod);
}