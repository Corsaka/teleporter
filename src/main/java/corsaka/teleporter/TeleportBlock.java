package corsaka.teleporter;

import com.badlogic.gdx.Gdx;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.world.BlockPosition;
import finalforeach.cosmicreach.world.World;
import finalforeach.cosmicreach.world.blocks.BlockState;
import finalforeach.cosmicreach.world.entities.Entity;
import finalforeach.cosmicreach.world.entities.Player;
import io.github.crmodders.flux.api.blocks.ModBlock;
import io.github.crmodders.flux.api.blocks.WorkingBlock;
import io.github.crmodders.flux.api.generators.data.BlockSupplier;
import io.github.crmodders.flux.menus.ConfigViewMenu;

public class TeleportBlock extends ModBlock implements WorkingBlock {
  public float x;
  public float y;
  public float z;


  public TeleportBlock() {
    super();
  }
  public TeleportBlock(BlockSupplier supplier) {
    super(supplier);
  }
  @Override
  public void onInteract(World world, Player player, BlockState blockState, BlockPosition position) {
    Entity playerEntity = player.getEntity();
    if(playerEntity.isSneaking) {
      GameState.switchToGameState(new BlockMenu(GameState.currentGameState,this));
      Gdx.input.setCursorCatched(false);
    } else {
      playerEntity.position.x = this.x;
      playerEntity.position.y = this.y;
      playerEntity.position.z = this.z;
    }
  }
  @Override
  public void onPlace(World world, Player player, BlockState blockState, BlockPosition position) {
  }

  @Override
  public void onBreak(World world, Player player, BlockState blockState, BlockPosition position) {
  }
}