package com.ss.scenes;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.ss.controller.GameUIController;
import com.ss.core.util.GLayer;
import com.ss.core.util.GScreen;
import com.ss.core.util.GStage;

public class GameScene extends GScreen {

  private Group gParent;
  private GameUIController gameUiController;

  public GameScene() {

    this.gParent = new Group();
    this.gameUiController = new GameUIController(this.gParent);

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

  }
}
