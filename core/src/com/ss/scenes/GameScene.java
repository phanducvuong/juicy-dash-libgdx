package com.ss.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.ss.GMain;
import com.ss.controller.GameUIController;
import com.ss.core.util.GLayer;
import com.ss.core.util.GScreen;
import com.ss.core.util.GStage;
import com.ss.utils.ShakeScreen;

public class GameScene extends GScreen {

  public  Group            gParent;
  private GameUIController controller;
  private ShakeScreen      shake;

  public GameScene() {

    this.gParent          = new Group();
    this.controller       = new GameUIController(this);
    this.shake            = new ShakeScreen(15f, 1f);

  }

    @Override
  public void dispose() {

  }

  @Override
  public void init() {
    GStage.addToLayer(GLayer.ui, this.gParent);
    controller.newGame();
    GMain.platform.ShowBanner(true);
  }

  @Override
  public void run() {
    shake.update(Gdx.graphics.getDeltaTime(), GStage.getCamera());
  }

  public void shake() {
    shake.reset();
  }
}
