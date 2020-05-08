package com.ss.objects;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.ss.GMain;
import com.ss.config.Type;
import com.ss.core.util.GUI;

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

  public void reset() {
    this.item = null;
    this.isEmpty = true;
  }

}
