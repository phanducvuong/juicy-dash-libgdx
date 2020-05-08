package com.ss.objects;

import static com.badlogic.gdx.math.Interpolation.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.ss.GMain;
import com.ss.config.Type;
import com.ss.core.util.GUI;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static com.ss.config.Config.*;

public class Item extends Group {

  private Image fruit;
  public Type type;
  public String name;
  public boolean isAlive = false;

  public Item(String region, Type type) {

    this.name = region;
    this.type = type;

    fruit = GUI.createImage(GMain.itemAtlas, region);
    setSize(fruit.getWidth(), fruit.getHeight());
    this.setOrigin(Align.center);
    this.addActor(fruit);

  }

  public void setPosition(Vector2 pos) {
    float x = pos.x + WIDTH_PIECE/2;
    float y = pos.y + HEIGHT_PIECE/2;
    this.setPosition(x, y, Align.center);
  }

  public void setPosStart(Vector2 pos) {
    float x = pos.x + WIDTH_PIECE/2;
    float y = -HEIGHT_PIECE - 20;
    this.setPosition(x, y, Align.center);
  }

  public void moveToPos(Vector2 pos) {
    this.addAction(
            moveTo(pos.x, pos.y, TIME_ADD_ITEM, linear)
    );
  }

  public Vector2 getPos() {
    return new Vector2(this.getX(), this.getY());
  }

}
