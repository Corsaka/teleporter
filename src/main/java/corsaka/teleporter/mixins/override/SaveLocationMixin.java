package org.coolcosmos.cosmicquilt.mixins.override;

import finalforeach.cosmicreach.io.SaveLocation;
import org.quiltmc.loader.api.QuiltLoader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(SaveLocation.class)
public class SaveLocationMixin {
    /**
     * @author coolGi
     * @reason Allows the launcher to provide the location of the game
     */
    @Overwrite
    public static String getSaveFolderLocation() {
        return QuiltLoader.getGameDir().toString();
    }
}
