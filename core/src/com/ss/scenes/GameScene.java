package com.ss.scenes;

import com.ss.core.util.GScreen;
import com.ss.gameLogic.Game;

public class GameScene extends GScreen {

    @Override
    public void dispose() {

    }

    @Override
    public void init() {

      new Game();

    }

    @Override
    public void run() {

    }
}
