package com.ss.gameLogic.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.ss.GMain;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import com.ss.gameLogic.Game;
import com.ss.gameLogic.effects.Effect;
import com.ss.gameLogic.interfaces.IClickCard;
import com.ss.gameLogic.logic.DivideCard;
import com.ss.gameLogic.logic.Logic;
import com.ss.gameLogic.objects.Bot;
import com.ss.gameLogic.objects.Card;

public class GamePlayUI implements IClickCard {

  private Logic logic = Logic.getInstance();
  private Effect effect;
  private Game game;

  private DivideCard divideCard;

  public GamePlayUI(Game game) {

    this.game = game;
    this.effect = Effect.getInstance(game);

    initBgGame();
    divideCard = new DivideCard(game);
    devideCard();

    testClick();

  }

  private void testClick() {

    Image click = GUI.createImage(GMain.liengAtlas, "button_start");
    game.gBackground.addActor(click);

    click.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);

        showAllCardUp();

      }
    });

  }

  private void initBgGame() {

    Image bgTable = GUI.createImage(GMain.liengAtlas, "bg_table");
    bgTable.setSize(GStage.getWorldWidth(), GStage.getWorldHeight());
    game.gBackground.addActor(bgTable);

  }

  private void devideCard() {
    divideCard.nextTurn();
  }

  @Override
  public void click(Card cardDown, Card cardUp) {
    effect.flipCard(cardDown, cardUp);
  }

  private void showAllCardUp() {

    for (int i=1; i<game.lsBotActive.size(); i++) {
      Bot bot = game.lsBotActive.get(i);
      for (int j=0; j<bot.lsCardDown.size(); j++) {
        Card cardDown = bot.lsCardDown.get(j);
        Card cardUp = bot.lsCardUp.get(j);
        effect.showAllCard(cardDown, cardUp);
      }
    }

  }
}
