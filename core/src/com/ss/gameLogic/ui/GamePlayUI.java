package com.ss.gameLogic.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.ss.GMain;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import com.ss.gameLogic.Game;
import com.ss.gameLogic.effects.Effect;
import com.ss.gameLogic.logic.DivideCard;
import com.ss.gameLogic.logic.Logic;
import static com.ss.gameLogic.config.Config.*;

public class GamePlayUI {

  private Logic logic = Logic.getInstance();
  private Effect effect = Effect.getInstance();
  private Game game;

  private DivideCard divideCard;

  public GamePlayUI(Game game) {

    this.game = game;

    initBgGame();
    divideCard = new DivideCard(game);

  }

  private void initBgGame() {

    Image bgTable = GUI.createImage(GMain.liengAtlas, "bg_table");
    bgTable.setSize(GStage.getWorldWidth(), GStage.getWorldHeight());
    game.gBackground.addActor(bgTable);

  }

  private void testPosCard() {

    Image card0 = GUI.createImage(GMain.cardAtlas, "ace_club");
    card0.setScale(1.8f);
    card0.setPosition(POS_BOT_0.x - card0.getWidth()*1.8f/2, POS_BOT_0.y - card0.getHeight()*1.8f);
    game.gCard.addActor(card0);

    Image card1 = GUI.createImage(GMain.cardAtlas, "ace_club");
    card1.setPosition(POS_BOT_1.x - card1.getWidth(), POS_BOT_1.y);
    game.gCard.addActor(card1);

    Image card2 = GUI.createImage(GMain.cardAtlas, "ace_club");
    card2.setPosition(POS_BOT_2.x - card2.getWidth(), POS_BOT_2.y);
    game.gCard.addActor(card2);

    Image card3 = GUI.createImage(GMain.cardAtlas, "ace_club");
    card3.setPosition(POS_BOT_3.x - card3.getWidth(), POS_BOT_3.y);
    game.gCard.addActor(card3);

    Image card4 = GUI.createImage(GMain.cardAtlas, "ace_club");
    card4.setPosition(POS_BOT_4.x, POS_BOT_4.y);
    game.gCard.addActor(card4);

    Image card5 = GUI.createImage(GMain.cardAtlas, "ace_club");
    card5.setPosition(POS_BOT_5.x, POS_BOT_5.y);
    game.gCard.addActor(card5);

  }

}
