package com.ss.scenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.ss.controller.GameUIController;
import com.ss.core.util.GLayer;
import com.ss.core.util.GScreen;
import com.ss.core.util.GStage;
import com.ss.utils.ShakeScreen;

public class GameScene extends GScreen {

  public  Group            gParent;
  private GameUIController gameUIController;
  private ShakeScreen      shake;
  private float            baseX, baseY;

  public GameScene() {

    this.gParent          = new Group();
    this.gameUIController = new GameUIController(this);
    this.shake            = new ShakeScreen(15f, 1f);
    this.baseX            = GStage.getCamera().position.x;
    this.baseY            = GStage.getCamera().position.y;

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
    GStage.getCamera().position.x = baseX;
    GStage.getCamera().position.y = baseY;
    shake.update(Gdx.graphics.getDeltaTime(), GStage.getCamera());
  }

  public void shake() {
    shake.reset();
  }
}
