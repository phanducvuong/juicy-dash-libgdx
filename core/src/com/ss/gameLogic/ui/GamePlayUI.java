package com.ss.gameLogic.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Align;
import com.ss.GMain;
import com.ss.core.util.GClipGroup;
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

import java.util.List;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class GamePlayUI implements IClickCard {

  private Logic logic = Logic.getInstance();
  private Effect effect;
  private Game game;

  private Button btnUp, btnTo, btnTheo;
  private Label lbTotoalMoneyBet, lbWin;

  private Group gBannerWin;
  private Image bannerWin;

  private GClipGroup gRateMoney;
  private Image controlRateMoney;

  private DivideCard divideCard;

  public GamePlayUI(Game game) {

    this.game = game;
    this.effect = Effect.getInstance(game);

    initUIGame();
    initButtonBet();
    initRateMoney();
    handleClickBtnBet();
    hideBtnBet();

    testClick();

  }

  private void initButtonBet() {

    btnTo = new Button("btn_to", C.lang.raise);
    btnTo.setPosition(GStage.getWorldWidth()/2 + 50, GStage.getWorldHeight() - btnTo.getHeight() - 10);
    btnTo.startEftLight(game);
    btnTo.addToGroup(game.gBtn);

    btnTheo = new Button("btn_theo", C.lang.call);
    btnTheo.setPosition(btnTo.getX() + btnTheo.getWidth() + 20, btnTo.getY());
    btnTheo.startEftLight(game);
    btnTheo.addToGroup(game.gBtn);

    btnUp = new Button("btn_up", C.lang.fold);
    btnUp.setPosition(btnTheo.getX() + btnUp.getWidth() + 20, btnTheo.getY());
    btnUp.startEftLight(game);
    btnUp.addToGroup(game.gBtn);

  }

  private void initRateMoney() {

    gRateMoney = new GClipGroup();
    Image bgRateMoney = GUI.createImage(GMain.liengAtlas, "bg_rate_money");
    gRateMoney.setClipArea(0, 0, bgRateMoney.getWidth(), bgRateMoney.getHeight());
    gRateMoney.setPosition(btnTo.getX() + 15, btnTo.getY() - btnTo.getHeight() + 60);

    controlRateMoney = GUI.createImage(GMain.liengAtlas, "control_rate_money");
    controlRateMoney.setPosition(bgRateMoney.getX() - 2,
                                  bgRateMoney.getY() + bgRateMoney.getHeight()/2 - controlRateMoney.getHeight()/2);

    Image rateMoney = GUI.createImage(GMain.liengAtlas, "rate_money");
    rateMoney.setPosition(controlRateMoney.getX() - rateMoney.getWidth() + controlRateMoney.getWidth(),
            bgRateMoney.getY() + bgRateMoney.getHeight()/2 - rateMoney.getHeight()/2 + 2);
    gRateMoney.addActor(rateMoney);
    gRateMoney.addActor(bgRateMoney);
    gRateMoney.addActor(controlRateMoney);
    game.gBackground.addActor(gRateMoney);

    Label lbMoneyRate = new Label("+$12,000", new Label.LabelStyle(Config.PLUS_MONEY_FONT, null));
    lbMoneyRate.setFontScale(.8f);
    lbMoneyRate.setPosition(gRateMoney.getX() + bgRateMoney.getWidth() + 20,
                                gRateMoney.getY() + bgRateMoney.getHeight()/2 - lbMoneyRate.getHeight()/2 - 10);
    game.gBot.addActor(lbMoneyRate);

    //label: drag and drop
    float rateXMin = bgRateMoney.getX() - 2;
    float rateXMax = bgRateMoney.getX() + bgRateMoney.getWidth() - controlRateMoney.getWidth();
    controlRateMoney.addListener(new DragListener() {
      @Override
      public void dragStart(InputEvent event, float x, float y, int pointer) {
        super.dragStart(event, x, y, pointer);

        System.out.println("RATEXMIN: " + rateXMin + "  RATEXMAX: " + rateXMax);

      }

      @Override
      public void drag(InputEvent event, float x, float y, int pointer) {
        super.drag(event, x, y, pointer);

        float moveBuyX = controlRateMoney.getX() + (x - controlRateMoney.getWidth()/2);

        if (moveBuyX >= rateXMin && moveBuyX <= rateXMax) {
          controlRateMoney.moveBy(x - controlRateMoney.getWidth()/2, 0);
          rateMoney.setPosition(controlRateMoney.getX() - rateMoney.getWidth() + controlRateMoney.getWidth(),
                  bgRateMoney.getY() + bgRateMoney.getHeight()/2 - rateMoney.getHeight()/2 + 2);

          float tempRate = Math.round(((moveBuyX+controlRateMoney.getWidth()/2)/bgRateMoney.getWidth())*100);
          float rate = tempRate/100;
          String moneyString = logic.convertMoneyBet((long) (game.lsBotActive.get(0).getTotalMoney()*rate));
          lbMoneyRate.setText("+" + moneyString);

        }
        else if (moveBuyX > rateXMax) {
          controlRateMoney.setPosition(rateXMax - 2, bgRateMoney.getY() + bgRateMoney.getHeight()/2 - controlRateMoney.getHeight()/2);
          rateMoney.setPosition(controlRateMoney.getX() - rateMoney.getWidth() + controlRateMoney.getWidth(),
                  bgRateMoney.getY() + bgRateMoney.getHeight()/2 - rateMoney.getHeight()/2 + 2);

          lbMoneyRate.setText("+" + logic.convertMoneyBet(game.lsBotActive.get(0).getTotalMoney()));
        }
        else if (moveBuyX < rateXMin) {
          controlRateMoney.setPosition(bgRateMoney.getX() - 2, bgRateMoney.getY() + bgRateMoney.getHeight()/2 - controlRateMoney.getHeight()/2);
          rateMoney.setPosition(controlRateMoney.getX() - rateMoney.getWidth() + controlRateMoney.getWidth(),
                  bgRateMoney.getY() + bgRateMoney.getHeight()/2 - rateMoney.getHeight()/2 + 2);

          lbMoneyRate.setText("$0");
        }

      }

      @Override
      public void dragStop(InputEvent event, float x, float y, int pointer) {
        super.dragStop(event, x, y, pointer);
      }
    });

  }

  private void clrActionBtnBet() {
    btnTo.stopEftLight();
    btnTheo.stopEftLight();
    btnUp.stopEftLight();
  }

  private void handleClickBtnBet() {

    btnTo.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);

        Runnable run = () -> {

          game.bet.TO(game.lsBotActive.get(0));
          game.gBackground.addAction(
                  sequence(
                          delay(logic.timeDelayToNextTurnBet()),
                          run(() -> game.bet.nextTurn(1))
                  )
          );

        };

        hideBtnBet();
        effect.click(btnTo, run);

      }
    });

    btnTheo.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);

        Runnable run = () -> {

          game.bet.THEO(game.lsBotActive.get(0));
          game.gBackground.addAction(
                  sequence(
                          delay(logic.timeDelayToNextTurnBet()),
                          run(() -> game.bet.nextTurn(1))
                  )
          );

        };

        hideBtnBet();
        effect.click(btnTheo, run);

      }
    });

    btnUp.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);

        Runnable run = () -> {

          game.bet.UP(game.lsBotActive.get(0));
          game.gBackground.addAction(
                  sequence(
                          delay(logic.timeDelayToNextTurnBet()),
                          run(() -> game.bet.nextTurn(1))
                  )
          );

        };

        hideBtnBet();
        effect.click(btnUp, run);

      }
    });

  }

  private void testClick() {

    Image click = GUI.createImage(GMain.liengAtlas, "button_start");
    game.gBackground.addActor(click);

    Image divideCard = GUI.createImage(GMain.liengAtlas, "divide_card");
    divideCard.setPosition(click.getX() + divideCard.getWidth(), 0);
    game.gBackground.addActor(divideCard);

    divideCard.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);

        game.newRound();

      }
    });

    click.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);

//        showAllWhenFindWinner();
//        System.out.println(logic.timeDelayToNextTurnBet());
        System.out.println(logic.convertMoneyBet(3254870002500005300L));

      }
    });

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

    //label: banner win
    gBannerWin = new Group();
    bannerWin = GUI.createImage(GMain.liengAtlas, "banner_win");
    gBannerWin.setSize(bannerWin.getWidth(), bannerWin.getHeight());
    gBannerWin.addActor(bannerWin);

    lbWin = new Label(C.lang.winner, new Label.LabelStyle(Config.WIN_FONT, null));
    lbWin.setAlignment(Align.center);
    lbWin.setFontScale(.7f);
    lbWin.setPosition(bannerWin.getX() + bannerWin.getWidth()/2 - lbWin.getWidth()/2,
                      bannerWin.getY() + bannerWin.getHeight()/2 - lbWin.getHeight()/2 - 5);
    gBannerWin.addActor(lbWin);

  }

  public void eftLbTotalMoney(long money) {
    effect.raiseMoney(lbTotoalMoneyBet, money);
  }

  @Override
  public void click(Card cardDown, Card cardUp) {
    effect.flipCard(cardDown, cardUp);
  }

  public void showAllWhenFindWinner(List<Bot> lsBot, Bot winner) {

    for (Bot bot : lsBot) {
      if (bot.id != 0) {
        for (int j=0; j<bot.lsCardDown.size(); j++) {
          Card cardDown = bot.lsCardDown.get(j);
          Card cardUp = bot.lsCardUp.get(j);
          effect.showAllCard(cardDown, cardUp, Config.SCL_SHOW_CARD, Config.SCL_SHOW_CARD);
        }
      }

      long tempMoneyChange = bot.getTotalMoneyBet() - winner.getTotalMoneyBet();
      if (tempMoneyChange > 0) {
        game.bet.totalMoney -= tempMoneyChange;
        bot.eftLbMoneyChange(game, tempMoneyChange);
      }
    }

    if (winner.id != 0)
      effect.winnerIsBot(winner);
    else
      winner.eftMoneyWinner(game, game.bet.totalMoney);

  }

  public void showCardWinner(Bot winner) {
    if (winner.id != 0)
      effect.winnerIsBot(winner);
    else
      winner.eftMoneyWinner(game, game.bet.totalMoney);
  }

  private void hideBtnBet() {

    clrActionBtnBet();

    btnTo.setTouchable(Touchable.disabled);
    btnTheo.setTouchable(Touchable.disabled);
    btnUp.setTouchable(Touchable.disabled);

    btnTo.setColor(Color.GRAY);
    btnTheo.setColor(Color.GRAY);
    btnUp.setColor(Color.GRAY);

  }

  public void showBtnBet() {

    btnTo.startEftLight(game);
    btnTheo.startEftLight(game);
    btnUp.startEftLight(game);

    btnTo.setTouchable(Touchable.enabled);
    btnTheo.setTouchable(Touchable.enabled);
    btnUp.setTouchable(Touchable.enabled);

    btnTo.setColor(Color.WHITE);
    btnTheo.setColor(Color.WHITE);
    btnUp.setColor(Color.WHITE);

  }

  public void showBannerWin(Bot bot) {
    if (bot.id != 0) {
      Image avatar = bot.avatar;
      gBannerWin.setPosition(avatar.getX() + avatar.getWidth()/2 - bannerWin.getWidth()/2,
              avatar.getY() + avatar.getHeight() - bannerWin.getHeight()/2);
      game.gEffect.addActor(gBannerWin);
    }
  }

  public void hideBannerWin() {
    gBannerWin.remove();
  }

  public void reset() {
    hideBannerWin();
  }
}
