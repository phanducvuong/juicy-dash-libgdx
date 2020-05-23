package com.ss.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.ss.controller.GameUIController;
import com.ss.core.util.GLayer;
import com.ss.core.util.GScreen;
import com.ss.core.util.GStage;
import com.ss.utils.ShakeScreen;

public class GameScene extends GScreen {

  private Group gParent;
  private GameUIController gameUiController;

  public GameScene() {

    this.gParent = new Group();
    this.gameUiController = new GameUIController(this.gParent);
    ShakeScreen.shake(2f, .3f);

  }

    @Override
  public void dispose() {

  }

  @Override
  public void init() {

    GStage.addToLayer(GLayer.ui, this.gParent);

  }

  @Override
  public void run() {
    if (ShakeScreen.getRumbleTimeLeft() > 0){
      ShakeScreen.tick(Gdx.graphics.getDeltaTime());
      GStage.getCamera().translate(ShakeScreen.getPos());
      System.out.println("AAA");
    }
  }
}
