package org.coolcosmos.cosmicquilt.mixins.entrypoint;

import finalforeach.cosmicreach.BlockGame;
import org.coolcosmos.cosmicquilt.api.entrypoint.ModInitializer;
import org.quiltmc.loader.api.entrypoint.EntrypointUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockGame.class)
public class GameLoadMixin {
    @Inject(method = "create", at = @At("TAIL"))
    public void onInitialize(CallbackInfo ci) {
        // Allows for the init entrypoint to be defied in the quilt.mod.json
        EntrypointUtil.invoke(ModInitializer.ENTRYPOINT_KEY, ModInitializer.class, ModInitializer::onInitialize);
    }
}
