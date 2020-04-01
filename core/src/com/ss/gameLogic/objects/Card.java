package com.ss.gameLogic.objects;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.ss.GMain;
import com.ss.core.util.GUI;
import com.ss.gameLogic.card.Number;
import com.ss.gameLogic.card.Type;

public class Card {

  public Type type;
  public Number number;
  private boolean isCardDown = false;

  private Image card;

  public Card(Type type, Number number, boolean isCardDown) {

    this.isCardDown = isCardDown;
    if (type != null && number != null) {
      this.type = type;
      this.number = number;
    }

    if (isCardDown)
      card = GUI.createImage(GMain.cardAtlas, "card_down");
    else {
      String name = number.name()+ "_" + type.name();
      card = GUI.createImage(GMain.cardAtlas, name);
    }
    card.setOrigin(Align.center);

  }

  public void addCardToScene(Group group) {
    group.addActor(card);
  }

  public void remove() {
    card.remove();
  }

  public void setPosition(float x, float y) {
    card.setPosition(x, y);
  }

  public float getWidth() {
    return card.getWidth();
  }

  public float getHeight() {
    return card.getHeight();
  }

  public Image getCard() {
    return card;
  }

}
