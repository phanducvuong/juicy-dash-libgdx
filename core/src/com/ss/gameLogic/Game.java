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
import java.util.Collections;
import java.util.List;

public class Game {

  private Logic logic = Logic.getInstance();

  public Group gBackground, gCard;

  public List<Bot> lsBot, lsBotActive; //reset lsBotActive when change numOfPlayer
  public List<Card> lsCardDown, lsCardUp;
  public int numOfPlayer = 6;

  public GamePlayUI gamePlayUI;

  public Game() {

    this.lsBotActive = new ArrayList<>();

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

  public void setNumOfPlayer(int num) {
    this.numOfPlayer = num;
  }

  public void getLsBotActive() {

    lsBotActive.clear();
    switch (numOfPlayer) {

      case 2:
        lsBotActive.add(lsBot.get(0));
        lsBotActive.add(lsBot.get(3));
        break;
      case 3:
        lsBotActive.add(lsBot.get(0));
        lsBotActive.add(lsBot.get(2));
        lsBotActive.add(lsBot.get(4));
        break;
      case 4:
        lsBotActive.add(lsBot.get(0));
        lsBotActive.add(lsBot.get(2));
        lsBotActive.add(lsBot.get(3));
        lsBotActive.add(lsBot.get(4));
        break;
      case 5:
        lsBotActive.add(lsBot.get(0));
        lsBotActive.add(lsBot.get(1));
        lsBotActive.add(lsBot.get(2));
        lsBotActive.add(lsBot.get(4));
        lsBotActive.add(lsBot.get(5));
        break;
      case 6:
        lsBotActive.add(lsBot.get(0));
        lsBotActive.add(lsBot.get(1));
        lsBotActive.add(lsBot.get(2));
        lsBotActive.add(lsBot.get(3));
        lsBotActive.add(lsBot.get(4));
        lsBotActive.add(lsBot.get(5));

    }

  }

  public void resetGame() {

    //todo: reset lsCardDown and lsCardUp

  }

}
