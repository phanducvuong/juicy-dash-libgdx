package com.ss.controller;

import com.ss.repository.Leaderboard;
import com.ss.scenes.StartScene;
import com.ss.ui.StartUI;

public class StartUIController {

  public  StartScene scene;
  private StartUI    startUI;

  public StartUIController(StartScene scene) {

    this.scene    = scene;
    this.startUI  = new StartUI(this);
    Leaderboard.PlayerData.init();

    scene.gParent.addActor(this.startUI);

  }

  public void animStartScene() {
    startUI.setVisibleIconSound();
    startUI.resetAnimObject();
    startUI.animObjectStart();
  }

}
