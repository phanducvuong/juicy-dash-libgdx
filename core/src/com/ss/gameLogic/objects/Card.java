package com.ss.gameLogic.objects;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.ss.GMain;
import com.ss.core.util.GUI;
import com.ss.gameLogic.card.Number;
import com.ss.gameLogic.card.Type;
import com.ss.gameLogic.config.Config;
import com.ss.gameLogic.interfaces.IClickCard;

public class Card {

  public Type type;
  public Number number;
  private IClickCard iClickCard;

  private Image card;

  public Card(Type type, Number number, boolean isCardDown) {

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
    card.setScale(Config.SCL_CARD_INIT);

  }

  public void addListener(Card cardUp) {

    card.addListener(new InputListener() {
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        iClickCard.click(Card.this, cardUp);
        return super.touchDown(event, x, y, pointer, button);
      }
    });

  }

  public void reset() {

    iClickCard = null;
    card.clear();
    card.remove();

  }

  public void setScale(float sclX, float sclY) {
    card.setScale(sclX, sclY);
  }

  public void addAction(ParallelAction par) {
    card.addAction(par);
  }

  public void addAction(SequenceAction seq) {
    card.addAction(seq);
  }

  public void addAction(Action action) {
    card.addAction(action);
  }

  public void setZindex(int zindex) {
    card.setZIndex(zindex);
  }

  public void addCardToScene(Group group) {
    group.addActor(card);
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

  public void setiClickCard(IClickCard iClickCard) {
    this.iClickCard = iClickCard;
  }

}
