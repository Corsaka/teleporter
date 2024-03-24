package corsaka.teleporter.mixins;

import corsaka.teleporter.TeleportBlock;
import finalforeach.cosmicreach.BlockGame;
import io.github.crmodders.flux.api.generators.data.BlockDataGen;
import io.github.crmodders.flux.api.registries.BuiltInRegistries;
import io.github.crmodders.flux.api.registries.Identifier;
import org.hjson.JsonValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockGame.class)
public class RegisterBlocks {

  @Inject(method = "create", at = @At("TAIL"))
  private void register(CallbackInfo ci) {
    Identifier TeleportBlockID = new Identifier("teleporter", "teleportblock");

    BlockDataGen TeleportBlock = BlockDataGen.createGenerator(
            TeleportBlockID
    ).setBlockstate(
            "default",
            BlockDataGen.BlockStateDataGen.createGenerator()
                    .setData("modelName", JsonValue.valueOf("model_metal_panel"))
    );

    BuiltInRegistries.MODDED_BLOCKS.register(
            TeleportBlockID,
            new TeleportBlock(TeleportBlock::Generate)
    );
  }

}