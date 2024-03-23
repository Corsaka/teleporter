package org.coolcosmos.cosmicquilt.api.entrypoint;

import finalforeach.cosmicreach.BlockGame;
import org.quiltmc.loader.api.ModContainer;

/**
 * A mod initializer.
 * <p>
 * In {@code quilt.mod.json}, the entrypoint is defined with {@value #ENTRYPOINT_KEY} key.
 * <p>
 * Currently, it is executed in {@link BlockGame#create()}, at it's tail
 *
 * @see org.coolcosmos.cosmicquilt.api.entrypoint.client.ClientModInitializer
 * @see org.coolcosmos.cosmicquilt.api.entrypoint.server.DedicatedServerModInitializer
 */
public interface ModInitializer {
    /**
     * Represents the key which this entrypoint is defined with, whose value is {@value}.
     */
    String ENTRYPOINT_KEY = "init";

    /**
     * Runs the mod initializer.
     *
     * @param mod the mod which is initialized
     */
    void onInitialize(ModContainer mod);
}
