package corsaka.teleporter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.utils.ScreenUtils;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.ui.UIElement;
import finalforeach.cosmicreach.ui.VerticalAnchor;
import io.github.crmodders.flux.ui.TextElement;

public class BlockMenu extends GameState {
  private static NumberTextButton activeField;
  GameState previousState;

  public BlockMenu(final GameState previousState,TeleportBlock teleportBlock) {
    this.previousState = previousState;
    TextElement element = new TextElement(0,-200,"Teleport Block Config",2,false);
    element.show();
    this.uiElements.add(element);

    NumberTextButton xField = new NumberTextButton("X", teleportBlock.x, 0.0F, -100.0F, 150.0F, 50.0F);
    NumberTextButton yField = new NumberTextButton("Y", teleportBlock.y, 0.0F, 0.0F, 150.0F, 50.0F);
    NumberTextButton zField = new NumberTextButton("Z", teleportBlock.z, 0.0F, 100.0F, 150.0F, 50.0F);
    xField.show();
    yField.show();
    zField.show();
    this.uiElements.add(xField);
    this.uiElements.add(yField);
    this.uiElements.add(zField);

    UIElement doneButton = new UIElement(0.0F, -50.0F, 250.0F, 50.0F) {
      @Override
      public void onClick() {
        super.onClick();
        teleportBlock.x = xField.getFloatValue(true);
        teleportBlock.y = yField.getFloatValue(false);
        teleportBlock.z = zField.getFloatValue(true);
        GameState.switchToGameState(previousState);
      }
    };
    doneButton.vAnchor = VerticalAnchor.BOTTOM_ALIGNED;
    doneButton.setText("Done");
    doneButton.show();
    this.uiElements.add(doneButton);
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

  static class NumberTextButton extends UIElement {
    InputProcessor inputProcessor;
    String label;
    String value;
    private boolean inputted;
    private String oldValue;

    //numbers, minus, dot, and comma (for non-english decimals), for both standard + numpad
    private final int[] permittedKeycodes = new int[]{7,8,9,10,11,12,13,14,15,16,55,56,69,144,145,146,147,148,149,150,151,152,153,156,158,159};

    public NumberTextButton(String label, float value, float x, float y, float w, float h) {
      super(x, y, w, h);
      this.value = Float.toString(value);
      this.label = label;
      this.updateText();
    }

    private float getFloatValue(boolean addHalfToInt) {
      if(value.contains(".") || value.contains(",") || !addHalfToInt) {
        if(value.contains(",")) {
          value = value.replace(',','.');
        }
        return Float.parseFloat(value);
      } else {
        return Float.parseFloat(value) + 0.5F;
      }
    }

    private void deactivate() {
      if(!inputted) { value = oldValue; }
      BlockMenu.activeField = null;
      this.updateText();
      Gdx.input.setInputProcessor(this.inputProcessor);
      this.inputProcessor = null;
    }

    @Override
    public void onCreate() {
      super.onCreate();
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
        oldValue = value;
        value = "";
        BlockMenu.activeField = this;
        this.inputProcessor = Gdx.input.getInputProcessor();
        Gdx.input.setInputProcessor(this);
      }

      this.updateText();
    }

    @Override
    public boolean keyDown(int keycode) {
      if(keycode == 67) { //backspace
        if(value == null) { return false; }
        value = value.substring(0,value.length()-1);
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
      inputted = true;
      value = value + Input.Keys.toString(keycode);

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
