package com.ss.gameLogic;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.ss.core.util.GLayer;
import com.ss.core.util.GStage;
import com.ss.gameLogic.card.Number;
import com.ss.gameLogic.card.Type;
import com.ss.gameLogic.logic.Logic;
import com.ss.gameLogic.objects.Bot;
import com.ss.gameLogic.objects.Card;
import com.ss.gameLogic.ui.GamePlayUI;

import java.util.ArrayList;
import java.util.List;

public class Game {

  private Logic logic = Logic.getInstance();

  public Group gBackground, gCard;

  public List<Bot> lsBot;
  public List<Card> lsCardDown;
  public List<Card> lsCardUp;

  private GamePlayUI gamePlayUI;

  public Game() {

    initLayer();
    initBotAndCard();

    gamePlayUI = new GamePlayUI(this);

  }

  private void initBotAndCard() {

    lsBot = new ArrayList<>();
    for (int i=0; i<6; i++)
      lsBot.add(new Bot(i));

    lsCardDown = new ArrayList<>();
    for (int i=0; i<52; i++)
      lsCardDown.add(new Card(null, null, true));

    lsCardUp = new ArrayList<>();
    for (Number number : Number.values())
      for (Type type : Type.values())
        lsCardUp.add(new Card(type, number, false));

  }

  private void initLayer() {

    gBackground = new Group();
    gCard = new Group();

    GStage.addToLayer(GLayer.ui, gBackground);
    GStage.addToLayer(GLayer.ui, gCard);

  }

}
