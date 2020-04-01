package com.ss.gameLogic.effects;

import static com.badlogic.gdx.math.Interpolation.*;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Effect {

  private static Effect instance = null;

  private Effect() {}

  public static Effect getInstance() {
    return instance == null ? instance = new Effect() : instance;
  }

  public void moveCardTo(Image card, float x, float y) {

    card.addAction(moveTo(x, y, .5f, fastSlow));

  }

}
