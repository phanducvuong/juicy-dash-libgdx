package com.ss.scenes;

import com.ss.core.util.GScreen;
import com.ss.gameLogic.Game;

public class GameScene extends GScreen {

  private Game game;
  public GameScene() {
    game = new Game();
  }

    @Override
  public void dispose() {

  }

  @Override
  public void init() {
    game.initLayer();
  }

  @Override
  public void run() {

  }
}
