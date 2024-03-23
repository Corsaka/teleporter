package org.coolcosmos.cosmicquilt.api.entrypoint.client;

import org.quiltmc.loader.api.ModContainer;

/* *
 * A mod initializer which is run only on {@link net.fabricmc.api.EnvType#CLIENT}.
 * <p>
 * This entrypoint is suitable for setting up client-specific logic, such as rendering
 * or integrated server tweaks.
 * <p>
 * In {@code quilt.mod.json}, the entrypoint is defined with {@value #ENTRYPOINT_KEY} key.
 * <p>
 * Currently, it is executed in the {@link net.minecraft.client.MinecraftClient} constructor, just before the initialization of
 * the {@link net.minecraft.client.option.GameOptions}.
 *
 * @see org.coolcosmos.cosmicquilt.api.entrypoint.ModInitializer
 * @see org.coolcosmos.cosmicquilt.api.entrypoint.server.DedicatedServerModInitializer
 */

/**
 * NOT IMPLEMENTED!!!
 * (waiting on Cosmic Reach server-side support)
 */
public interface ClientModInitializer {
    /**
     * Represents the key which this entrypoint is defined with, whose value is {@value}.
     */
    String ENTRYPOINT_KEY = "client_init";

    /**
     * Runs the mod initializer on the client environment.
     *
     * @param mod the mod which is initialized
     */
    void onInitializeClient(ModContainer mod);
}