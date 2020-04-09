package com.ss.gameLogic.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.ss.GMain;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import com.ss.gameLogic.Game;
import com.ss.gameLogic.config.C;
import com.ss.gameLogic.config.Config;
import com.ss.gameLogic.effects.Effect;
import com.ss.gameLogic.interfaces.IClickCard;
import com.ss.gameLogic.logic.DivideCard;
import com.ss.gameLogic.logic.Logic;
import com.ss.gameLogic.objects.Bot;
import com.ss.gameLogic.objects.Button;
import com.ss.gameLogic.objects.Card;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class GamePlayUI implements IClickCard {

  private Logic logic = Logic.getInstance();
  private Effect effect;
  private Game game;

  private Button btnUp, btnTo, btnTheo;
  private Label lbTotoalMoneyBet;

  private DivideCard divideCard;

  public GamePlayUI(Game game) {

    this.game = game;
    this.effect = Effect.getInstance(game);

    initUIGame();
    initBot();
    initButtonBet();
    handleClickBtnBet();

    divideCard = new DivideCard(game);
    devideCard();

    testClick();

  }

  private void initButtonBet() {

    btnTo = new Button("btn_to", C.lang.raise);
    btnTo.setPosition(GStage.getWorldWidth()/2 + 50, GStage.getWorldHeight() - btnTo.getHeight() - 10);
    btnTo.addToGroup(game.gBtn);

    btnTheo = new Button("btn_theo", C.lang.call);
    btnTheo.setPosition(btnTo.getX() + btnTheo.getWidth() + 20, btnTo.getY());
    btnTheo.addToGroup(game.gBtn);

    btnUp = new Button("btn_up", C.lang.fold);
    btnUp.setPosition(btnTheo.getX() + btnUp.getWidth() + 20, btnTheo.getY());
    btnUp.addToGroup(game.gBtn);

  }

  private void handleClickBtnBet() {

    btnTo.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);

        Runnable run = () -> {

          btnTo.setTouchable(Touchable.enabled);
          game.bet.TO(game.lsBotActive.get(0));
          game.gBackground.addAction(
                  sequence(
                          delay(logic.timeDelayToNextTurnBet()),
                          run(() -> game.bet.startBet(game.lsBotActive.get(1)))
                  )
          );

        };

        btnTo.setTouchable(Touchable.disabled);
        effect.click(btnTo, run);

      }
    });

    btnTheo.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);

        Runnable run = () -> {

          btnTheo.setTouchable(Touchable.enabled);
          game.bet.THEO(game.lsBotActive.get(0));
          game.gBackground.addAction(
                  sequence(
                          delay(logic.timeDelayToNextTurnBet()),
                          run(() -> game.bet.startBet(game.lsBotActive.get(1)))
                  )
          );

        };

        btnTheo.setTouchable(Touchable.disabled);
        effect.click(btnTheo, run);

      }
    });

    btnUp.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);

        Runnable run = () -> {

          btnUp.setTouchable(Touchable.enabled);
          game.bet.UP(game.lsBotActive.get(0));
          game.lsBotActive.get(0).setAlive(false);
          game.gBackground.addAction(
                  sequence(
                          delay(logic.timeDelayToNextTurnBet()),
                          run(() -> game.bet.startBet(game.lsBotActive.get(1)))
                  )
          );

        };

        btnUp.setTouchable(Touchable.disabled);
        effect.click(btnUp, run);

      }
    });

  }

  private void testClick() {

    Image click = GUI.createImage(GMain.liengAtlas, "button_start");
    game.gBackground.addActor(click);

    click.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);

        showAllCardUp();
//        System.out.println(logic.timeDelayToNextTurnBet());

      }
    });

  }

  private void initBot() {

    for (int i=0; i<game.lsBotActive.size(); i++)
      game.lsBotActive.get(i).initAvatar(game.gBot);

  }

  private void initUIGame() {

    Image bgTable = GUI.createImage(GMain.liengAtlas, "bg_table");
    bgTable.setSize(GStage.getWorldWidth(), GStage.getWorldHeight());
    game.gBackground.addActor(bgTable);

    Image bgTotalMoneyBet = GUI.createImage(GMain.liengAtlas, "bg_money_bet");
    bgTotalMoneyBet.setScale(1.7f);
    bgTotalMoneyBet.setPosition(Config.CENTER_X - bgTotalMoneyBet.getWidth()*1.7f/2,
                                Config.CENTER_Y - bgTotalMoneyBet.getHeight()*1.7f/2 - 150);
    game.gBackground.addActor(bgTotalMoneyBet);

    lbTotoalMoneyBet = new Label("$10,000", new Label.LabelStyle(Config.MONEY_FONT, null));
    lbTotoalMoneyBet.setAlignment(Align.center);
    lbTotoalMoneyBet.setFontScale(.8f, .9f);
    lbTotoalMoneyBet.setPosition(bgTotalMoneyBet.getX() + bgTotalMoneyBet.getWidth()*bgTotalMoneyBet.getScaleX()/2 - lbTotoalMoneyBet.getWidth()/2 + 30,
                                  bgTotalMoneyBet.getY() + bgTotalMoneyBet.getHeight()*bgTotalMoneyBet.getScaleY()/2 - lbTotoalMoneyBet.getHeight()/2 - 5);
    game.gBackground.addActor(lbTotoalMoneyBet);

  }

  public void eftLbTotalMoney(long money) {
    effect.raiseMoney(lbTotoalMoneyBet, money);
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
