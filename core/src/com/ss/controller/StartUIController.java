package com.ss.controller;

import com.ss.scenes.StartScene;
import com.ss.ui.StartUI;

public class StartUIController {

  public  StartScene scene;
  private StartUI    startUI;

  public StartUIController(StartScene scene) {

    this.scene    = scene;
    this.startUI  = new StartUI(this);

    scene.gParent.addActor(this.startUI);

  }

  public void animStartScene() {
    startUI.resetAnimObject();
    startUI.animObjectStart();
  }

}
