package corsaka.teleporter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.Viewport;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.ui.FontRenderer;
import finalforeach.cosmicreach.ui.UIElement;
import finalforeach.cosmicreach.ui.VerticalAnchor;

public class BlockMenu extends GameState {
  private static NumberTextButton activeField;
  private Viewport uiViewport;
  GameState previousState;
  //TeleportBlock teleportBlock;

  public BlockMenu(final GameState previousState) {
    this.previousState = previousState;

    FontRenderer.drawText(batch,uiViewport,"Teleport Block Config",-120.0F,-200.0F);
    //addMenu(teleportBlock.x,teleportBlock.y,teleportBlock.z);
    UIElement doneButton = new UIElement(0.0F, -50.0F, 250.0F, 50.0F) {
      @Override
      public void onClick() {
        super.onClick();
        GameState.switchToGameState(previousState);
      }
    };
    doneButton.vAnchor = VerticalAnchor.BOTTOM_ALIGNED;
    doneButton.setText("Done");
    doneButton.show();
  }

  @Override
  public void render(float partTick) {
    super.render(partTick);

    ScreenUtils.clear(0.145F, 0.078F, 0.153F, 1.0F, true);
    Gdx.gl.glEnable(2929);
    Gdx.gl.glDepthFunc(513);
    Gdx.gl.glEnable(2884);
    Gdx.gl.glCullFace(1029);
    Gdx.gl.glEnable(3042);
    Gdx.gl.glBlendFunc(770, 771);
    this.drawUIElements();
  }

  private void addMenu(float xVal, float yVal, float zVal) {
    NumberTextButton xField = new NumberTextButton("X", Float.toString(xVal), 0.0F, -100.0F, 150.0F, 50.0F);
    NumberTextButton yField = new NumberTextButton("Y", Float.toString(yVal), 0.0F, 0.0F, 150.0F, 50.0F);
    NumberTextButton zField = new NumberTextButton("Z", Float.toString(zVal), 0.0F, 100.0F, 150.0F, 50.0F);
    this.uiElements.add(xField);
    this.uiElements.add(yField);
    this.uiElements.add(zField);
  }

  static class NumberTextButton extends UIElement {
    InputProcessor inputProcessor;
    String label;
    String value;
    int[] permittedKeycodes = new int[]{7,8,9,10,11,12,13,14,15,16,55,56,144,145,146,147,148,149,150,151,152,153,158,159};
    //numbers, numpad, dot, and comma (for non-english decimals)

    public NumberTextButton(String label, String value, float x, float y, float w, float h) {
      super(x, y, w, h);
      this.label = label;
    }

    private float getValue() {
      if(value.contains(".") || value.contains(",")) {
        if(value.contains(",")) {
          value = value.replace(',','.');
        }
        return Float.parseFloat(value);
      } else {
        return Float.parseFloat(value) + 0.5F;
      }
    }

    @Override
    public void onCreate() {
      super.onCreate();
    }

    public void deactivate() {
      BlockMenu.activeField = null;
      this.updateText();
      Gdx.input.setInputProcessor(this.inputProcessor);
      this.inputProcessor = null;
    }

    @Override
    public void onClick() {
      super.onClick();
      if (BlockMenu.activeField == this) {
        this.deactivate();
      } else {
        if (BlockMenu.activeField != null) {
          BlockMenu.activeField.deactivate();
        }
        BlockMenu.activeField = this;
        this.inputProcessor = Gdx.input.getInputProcessor();
        Gdx.input.setInputProcessor(this);
      }

      this.updateText();
    }

    @Override
    public boolean keyDown(int keycode) {
      if(keycode == 67) { //backspace
        if(value != null) { value = value.substring(0,value.length()-1); }
        this.updateText();
        return true;
      }

      if(keycode == 66 || keycode == 160) { //enter or numpadEnter
        this.deactivate();
        return true;
      }

      boolean permitted = false;
      for(int permittedKeycode : permittedKeycodes) {
        if(keycode == permittedKeycode) { permitted = true; break; }
      }
      if(!permitted) { return false; }

      String qwertyKeyName = Input.Keys.toString(keycode);
      value = value + qwertyKeyName; //append

      this.updateText();
      return true;
    }

    @Override
    public void updateText() {
      String valueStr = BlockMenu.activeField == null ? this.value : "<" + this.value + ">";
      this.setText(this.label + ": " + valueStr);
    }
  }
}
