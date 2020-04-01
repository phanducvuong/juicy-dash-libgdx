package com.ss.gameLogic.ui;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.ss.GMain;
import com.ss.core.action.exAction.GSimpleAction;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import com.ss.gameLogic.Game;
import com.ss.gameLogic.effects.Effect;
import com.ss.gameLogic.logic.Logic;
import com.ss.gameLogic.objects.Bot;
import com.ss.gameLogic.objects.Card;

import static com.ss.gameLogic.config.Config.*;

public class GamePlayUI {

  private Logic logic = Logic.getInstance();
  private Effect effect = Effect.getInstance();

  private Game G;
  private int numOfPlayer = 6;
  private int turn = -1, turnCardDown = -1, countTurn = -1; //reset when new game
  private Bot turnBot;

  public GamePlayUI(Game G) {

    this.G = G;

    initBgGame();
    showCardDown();
    nextTurn();


  }

  private void initBgGame() {

    Image bgTable = GUI.createImage(GMain.liengAtlas, "bg_table");
    bgTable.setSize(GStage.getWorldWidth(), GStage.getWorldHeight());
    G.gBackground.addActor(bgTable);

  }

  private void showCardDown() {

    for (int i=G.lsCardDown.size()-1; i>=0; i--) {
      Card cardDown = G.lsCardDown.get(i);
      int offset = (G.lsCardDown.size() - i)/2;
      cardDown.setPosition(GStage.getWorldWidth()/2 - cardDown.getWidth()/2 - offset, GStage.getWorldHeight()/2 - cardDown.getHeight()/2 - 50 - offset);
      cardDown.addCardToScene(G.gCard);
    }

  }

  private void startDivide(Card card) {
    countTurn++;
    if (countTurn < numOfPlayer*3)
      card.getCard().addAction(GSimpleAction.simpleAction(this::divide));
    else
      System.out.println("finish divide");
  }

  private boolean divide(float dt, Actor a) {

    Image card = (Image) a;
    card.setZIndex(1000);
    card.setRotation(logic.rndRotate());
    Vector2 v = logic.getPosByIdBot(turnBot.id);
    effect.moveCardTo(card, v.x, v.y);

    G.gCard.addAction(sequence(
            delay(.1f),
            run(this::nextTurn)
    ));

    return true;
  }

  private void nextTurn() {

    turn++;
    turnCardDown++;
    if (turn >= numOfPlayer)
      turn = 0;
    turnBot = G.lsBot.get(turn);
    startDivide(G.lsCardDown.get(turnCardDown));

  }

  private void testPosCard() {

    Image card0 = GUI.createImage(GMain.cardAtlas, "ace_club");
    card0.setScale(1.8f);
    card0.setPosition(POS_BOT_0.x - card0.getWidth()*1.8f/2, POS_BOT_0.y - card0.getHeight()*1.8f);
    G.gCard.addActor(card0);

    Image card1 = GUI.createImage(GMain.cardAtlas, "ace_club");
    card1.setPosition(POS_BOT_1.x - card1.getWidth(), POS_BOT_1.y);
    G.gCard.addActor(card1);

    Image card2 = GUI.createImage(GMain.cardAtlas, "ace_club");
    card2.setPosition(POS_BOT_2.x - card2.getWidth(), POS_BOT_2.y);
    G.gCard.addActor(card2);

    Image card3 = GUI.createImage(GMain.cardAtlas, "ace_club");
    card3.setPosition(POS_BOT_3.x - card3.getWidth(), POS_BOT_3.y);
    G.gCard.addActor(card3);

    Image card4 = GUI.createImage(GMain.cardAtlas, "ace_club");
    card4.setPosition(POS_BOT_4.x, POS_BOT_4.y);
    G.gCard.addActor(card4);

    Image card5 = GUI.createImage(GMain.cardAtlas, "ace_club");
    card5.setPosition(POS_BOT_5.x, POS_BOT_5.y);
    G.gCard.addActor(card5);

  }

}
