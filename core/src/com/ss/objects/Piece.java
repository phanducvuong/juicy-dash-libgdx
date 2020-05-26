package com.ss.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.ss.GMain;
import com.ss.config.Type;
import com.ss.core.util.GUI;
import com.ss.gameLogic.effects.Particle;
import com.ss.utils.Util;

import java.util.List;

import static com.badlogic.gdx.math.Interpolation.fastSlow;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.rotateTo;

public class Piece {

  public int row, col;
  public Vector2 pos;
  public Item item;
  public boolean isEmpty = true;

  public Piece(int row, int col, Vector2 pos) {

    this.pos = pos;
    this.row = row;
    this.col = col;

  }

  public void setItem(Item item) {
    this.item = item;
    this.isEmpty = false;
  }

  public void clear() {
    item = null;
    isEmpty = true;
  }

  public void animGlassJuice(boolean hor, boolean isBoth, Runnable onUpdateBoard) {
    item.addAnimGlassToScene(isBoth);
    item.setPosAnimGlassJuice(hor);
    item.animGlassJuice(hor, onUpdateBoard);
    clear();
  }

  public void animClock(Runnable onComplete) {
    item.animClock(onComplete);
    item.animLbScore();
    clear();
  }

  public void animJam(Runnable onComplete) {
    item.animJam(onComplete);
    item.animLbScore();
    clear();
  }

  public void animIce(Particle pIce) {
    item.addAnimIce(pIce);
    clear();
  }

  public void setScore(int score) {
    item.setScoreLb(score);
  }

  public void animLvSuccess() {
    item.animLvSuccess();
    clear();
  }

}
