package com.ss.scenes;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.ss.controller.StartUIController;
import com.ss.core.util.GLayer;
import com.ss.core.util.GScreen;
import com.ss.core.util.GStage;

public class StartScene extends GScreen {

  public  Group             gParent;
  private StartUIController controller;

  public StartScene() {

    gParent                       = new Group();
    controller  = new StartUIController(this);

  }

  @Override
  public void dispose() {

  }

  @Override
  public void init() {
    GStage.addToLayer(GLayer.ui, gParent);
    controller.animStartScene();
  }

  @Override
  public void run() {

  }
}
