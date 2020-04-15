package com.ss.gameLogic.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
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
import com.ss.gameLogic.objects.Chip;

import java.util.ArrayList;
import java.util.List;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class GamePlayUI implements IClickCard {

  private Logic logic = Logic.getInstance();
  private Effect effect;
  private Game game;
  public List<Chip> lsAllChip;

  private Image btnNewRound;

  private Button btnUp, btnTo, btnTheo;
  private Label lbTotoalMoneyBet, lbWin;

  private Group gBannerWin;
  private Image bannerWin;

  private GClipGroup gRateMoney;
  private Image controlRateMoney, rateMoney, bgRateMoney;
  private Label lbMoneyRate, lbMinBet;
  private long moneyBet;
  private float rateXMin, rateXMax;

  private Group gAlerrAds;
  private Image bgBlackAlertAds, btnAlertAds, btnXAlertAds;

  public GamePlayUI(Game game) {

    this.game = game;
    this.effect = Effect.getInstance(game);
    this.lsAllChip = new ArrayList<>();

    initUIGame();
    initButtonBet();
    initRateMoney();
    handleClickBtnBet();
    handleClickBtnAlert();
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
    bgRateMoney = GUI.createImage(GMain.liengAtlas, "bg_rate_money");
    gRateMoney.setClipArea(0, 0, bgRateMoney.getWidth(), bgRateMoney.getHeight());
    gRateMoney.setPosition(btnTo.getX() + 15, btnTo.getY() - btnTo.getHeight() + 60);

    controlRateMoney = GUI.createImage(GMain.liengAtlas, "control_rate_money");
    controlRateMoney.setPosition(bgRateMoney.getX() - 2,
                                  bgRateMoney.getY() + bgRateMoney.getHeight()/2 - controlRateMoney.getHeight()/2);

    rateMoney = GUI.createImage(GMain.liengAtlas, "rate_money");
    rateMoney.setPosition(controlRateMoney.getX() - rateMoney.getWidth() + controlRateMoney.getWidth(),
            bgRateMoney.getY() + bgRateMoney.getHeight()/2 - rateMoney.getHeight()/2 + 2);
    gRateMoney.addActor(rateMoney);
    gRateMoney.addActor(bgRateMoney);
    gRateMoney.addActor(controlRateMoney);
    game.gBackground.addActor(gRateMoney);

    lbMoneyRate = new Label("$0", new Label.LabelStyle(Config.PLUS_MONEY_FONT, null));
    lbMoneyRate.setFontScale(.8f);
    lbMoneyRate.setPosition(gRateMoney.getX() + bgRateMoney.getWidth() + 20,
                                gRateMoney.getY() + bgRateMoney.getHeight()/2 - lbMoneyRate.getHeight()/2 - 10);
    game.gBot.addActor(lbMoneyRate);

    //label: drag and drop
    rateXMin = bgRateMoney.getX() - 2;
    rateXMax = bgRateMoney.getX() + bgRateMoney.getWidth() - controlRateMoney.getWidth();

    controlRateMoney.addListener(new DragListener() {

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

          long moneyOwe = game.bet.getTotalMoneyBet() - game.lsBotActive.get(0).getTotalMoneyBet();
          moneyBet = (long) ((game.lsBotActive.get(0).getTotalMoney() - moneyOwe)*rate);

          String moneyString = logic.convertMoneyBet(moneyBet);
          lbMoneyRate.setText("+" + moneyString);

          //todo: if moneyBet == totalMoneyBot - moneyOwe => show effect all-in

          showBtnTo();

        } //rate > 0
        else if (moveBuyX > rateXMax) {
          controlRateMoney.setPosition(rateXMax - 2, bgRateMoney.getY() + bgRateMoney.getHeight()/2 - controlRateMoney.getHeight()/2);
          rateMoney.setPosition(controlRateMoney.getX() - rateMoney.getWidth() + controlRateMoney.getWidth(),
                  bgRateMoney.getY() + bgRateMoney.getHeight()/2 - rateMoney.getHeight()/2 + 2);

          long moneyOwe = game.bet.getTotalMoneyBet() - game.lsBotActive.get(0).getTotalMoneyBet();
          moneyBet = game.lsBotActive.get(0).getTotalMoney() - moneyOwe;
          lbMoneyRate.setText("+" + logic.convertMoneyBet(moneyBet));
        } //rate is max
        else if (moveBuyX < rateXMin) {
          controlRateMoney.setPosition(bgRateMoney.getX() - 2, bgRateMoney.getY() + bgRateMoney.getHeight()/2 - controlRateMoney.getHeight()/2);
          rateMoney.setPosition(controlRateMoney.getX() - rateMoney.getWidth() + controlRateMoney.getWidth(),
                  bgRateMoney.getY() + bgRateMoney.getHeight()/2 - rateMoney.getHeight()/2 + 2);

          lbMoneyRate.setText("$0");
          hideBtnTo();
        } //rate = 0

      }

      @Override
      public void dragStop(InputEvent event, float x, float y, int pointer) {
        super.dragStop(event, x, y, pointer);
      }
    });

  }

  private void setPosControllRateMoneyBet() {

    float tempPos = (float) (0.2*rateXMax);
    long moneyOwe = game.bet.getTotalMoneyBet() - game.lsBotActive.get(0).getTotalMoneyBet();
    moneyBet = (long) (0.2*(game.lsBotActive.get(0).getTotalMoney() - moneyOwe));
//    System.out.println("MONEY BET: " + moneyBet);

    lbMoneyRate.setText("+" + logic.convertMoneyBet(moneyBet));
    controlRateMoney.moveBy(tempPos, 0);
    rateMoney.setPosition(controlRateMoney.getX() - rateMoney.getWidth() + controlRateMoney.getWidth(),
            bgRateMoney.getY() + bgRateMoney.getHeight()/2 - rateMoney.getHeight()/2 + 2);

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

          if (moneyBet < game.moneyBet) {
            //todo: show label min bet is game.moneyBet
            lbMinBet.setPosition(Config.CENTER_X - lbMinBet.getWidth()/2,
                                    Config.CENTER_Y - lbMinBet.getHeight()/2);
            lbMinBet.setText(C.lang.minBet + " " + logic.convertMoneyBet(game.moneyBet));
            game.gEffect.addActor(lbMinBet);
            effect.alphaLabel(lbMinBet);
          }
          else {
            hideBtnBet();
            game.bet.TO(game.lsBotActive.get(0), moneyBet);
            moneyBet = 0;
            game.gBackground.addAction(
                    sequence(
                            delay(logic.timeDelayToNextTurnBet()),
                            run(() -> game.bet.nextTurn(1))
                    )
            );
          }

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

  private void handleClickBtnAlert() {

    btnAlertAds.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);

        Runnable run = () -> {

          btnAlertAds.setTouchable(Touchable.enabled);

          //todo: show ads
          game.lsBotActive.get(0).setTotalMoney(20000);
          game.lsBotActive.get(0).convertTotalMoneyToString();

        };

        btnAlertAds.setTouchable(Touchable.disabled);
        effect.click(btnAlertAds, run);

      }
    });

    btnXAlertAds.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);

        game.gBtn.addActor(btnNewRound);
        effect.sclMaxToMin(gAlerrAds, () -> {
          bgBlackAlertAds.remove();
          gAlerrAds.remove();
        });

      }
    });

  }

  private void testClick() {

//    Image click = GUI.createImage(GMain.liengAtlas, "button_start");
//    game.gBtn.addActor(click);
//
//    click.addListener(new ClickListener() {
//      @Override
//      public void clicked(InputEvent event, float x, float y) {
//        super.clicked(event, x, y);
//
//        showAllWhenFindWinner();
//        System.out.println(logic.timeDelayToNextTurnBet());
//        System.out.println(Math.round(Math.random() * 1));
//        showAlertAds();
//
//      }
//    });

    btnNewRound.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);

       btnNewRound.remove();
       game.newRound();

      }
    });

  }

  private void initUIGame() {

//    btnNewRound = new Button()

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

    lbMinBet = new Label(C.lang.minBet + " $10,000", new Label.LabelStyle(Config.BUTTON_FONT, null));
    lbMinBet.setAlignment(Align.center);
    lbMinBet.setPosition(Config.CENTER_X - lbMinBet.getWidth()/2, Config.CENTER_Y - lbMinBet.getHeight()/2);
    lbMinBet.getColor().a = 0;

    //label: alert ads
    bgBlackAlertAds = GUI.createImage(GMain.liengAtlas, "bg_black");
    bgBlackAlertAds.setSize(GStage.getWorldWidth(), GStage.getWorldHeight());

    gAlerrAds = new Group();
    Image bgAlert = GUI.createImage(GMain.liengAtlas, "panel_ads");
    gAlerrAds.setSize(bgAlert.getWidth(), bgAlert.getHeight());

    gAlerrAds.setOrigin(bgAlert.getWidth()/2, bgAlert.getHeight()/2);
    gAlerrAds.setPosition(Config.CENTER_X - bgAlert.getWidth()/2, Config.CENTER_Y - bgAlert.getHeight()/2);
    gAlerrAds.addActor(bgAlert);

    Label lbAertAds = new Label(C.lang.adsOutOfMoney, new Label.LabelStyle(Config.ALERT_FONT, null));
    lbAertAds.setAlignment(Align.center);
    lbAertAds.setFontScale(.7f);
    lbAertAds.setPosition(bgAlert.getX() + bgAlert.getWidth()/2 - lbAertAds.getWidth()/2,
                              bgAlert.getY() + bgAlert.getHeight()/2 - lbAertAds.getHeight()/2 - 180);
    gAlerrAds.addActor(lbAertAds);

    btnAlertAds = GUI.createImage(GMain.liengAtlas, "btn_ads");
    btnAlertAds.setPosition(bgAlert.getX() + bgAlert.getWidth()/2 - btnAlertAds.getWidth()/2,
                            bgAlert.getY() + bgAlert.getHeight() - btnAlertAds.getHeight() - 20);
    btnAlertAds.setOrigin(Align.center);
    gAlerrAds.addActor(btnAlertAds);

    btnXAlertAds = GUI.createImage(GMain.liengAtlas, "btn_x");
    btnXAlertAds.setPosition(bgAlert.getX() + bgAlert.getWidth() - btnXAlertAds.getWidth()/2,-btnXAlertAds.getHeight()/2);
    btnXAlertAds.setOrigin(Align.center);
    gAlerrAds.addActor(btnXAlertAds);

    gAlerrAds.setScale(0);

    //label: button new round
    btnNewRound = GUI.createImage(GMain.liengAtlas, "divide_card");
    btnNewRound.setPosition(Config.CENTER_X - btnNewRound.getWidth()/2,
                            Config.CENTER_Y - btnNewRound.getHeight()/2);
    game.gBtn.addActor(btnNewRound);

  }

  public void showAlertAds() {
    game.gAlert.addActor(bgBlackAlertAds);
    game.gAlert.addActor(gAlerrAds);
    effect.sclMinToMax(gAlerrAds);
  }

  public void showBtnNewRound() {
    game.gBtn.addActor(btnNewRound);
  }

  public void eftLbTotalMoney(long money) {
    effect.raiseMoney(lbTotoalMoneyBet, money);
  }

  @Override
  public void click(Card cardDown, Card cardUp) {
    effect.flipCard(cardDown, cardUp);
  }

  public void showAllWhenFindWinner(List<Bot> lsBot, Bot winner) {

    System.out.println("TOTAL MONEY: " + game.bet.totalMoney);

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

    eftArrangeLsChip(winner);

    if (winner.id != 0)
      effect.winnerIsBot(winner);
    else
      winner.eftMoneyWinner(game, game.bet.totalMoney);

  }

  public void eftArrangeLsChip(Bot winner) {
    effect.arrangeLsChip(lsAllChip, winner);
  }

  public void showCardWinner(Bot winner) {
    effect.arrangeLsChip(lsAllChip, winner);
    if (winner.id != 0)
      effect.winnerIsBot(winner);
    else
      winner.eftMoneyWinner(game, game.bet.totalMoney);
  }

  private void hideBtnBet() {

//    clrActionBtnBet();

    resetControlRateMoney();

    controlRateMoney.setTouchable(Touchable.disabled);
    controlRateMoney.setColor(Color.GRAY);
    rateMoney.setColor(Color.GRAY);
    bgRateMoney.setColor(Color.GRAY);
    lbMoneyRate.setColor(Color.GRAY);

    btnTo.setTouchable(Touchable.disabled);
    btnTheo.setTouchable(Touchable.disabled);
    btnUp.setTouchable(Touchable.disabled);

    btnTo.setColor(Color.GRAY);
    btnTheo.setColor(Color.GRAY);
    btnUp.setColor(Color.GRAY);

  }

  public void showBtnBet() {

//    btnTheo.startEftLight(game);
//    btnUp.startEftLight(game);

    btnTheo.setTouchable(Touchable.enabled);
    btnUp.setTouchable(Touchable.enabled);

    btnTheo.setColor(Color.WHITE);
    btnUp.setColor(Color.WHITE);

    if (logic.chkMoneyOweOfPlayer(game.lsBotActive.get(0), game.bet)) {

      controlRateMoney.setTouchable(Touchable.enabled);
      controlRateMoney.setColor(Color.WHITE);
      rateMoney.setColor(Color.WHITE);
      bgRateMoney.setColor(Color.WHITE);
      lbMoneyRate.setColor(Color.WHITE);

      setPosControllRateMoneyBet();
      showBtnTo();

    }

  }

  private void hideBtnTo() {
    btnTo.setTouchable(Touchable.disabled);
    btnTo.setColor(Color.GRAY);
  }

  private void showBtnTo() {
    btnTo.setTouchable(Touchable.enabled);
    btnTo.setColor(Color.WHITE);
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
    removeChip();
    hideBannerWin();
  }

  private void removeChip() {
    for (Chip chip : lsAllChip)
      chip.remove();
    lsAllChip.clear();
  }

  private void resetControlRateMoney() {

    lbMoneyRate.setText("$0");

    controlRateMoney.setPosition(bgRateMoney.getX() - 2,
            bgRateMoney.getY() + bgRateMoney.getHeight()/2 - controlRateMoney.getHeight()/2);
    rateMoney.setPosition(controlRateMoney.getX() - rateMoney.getWidth() + controlRateMoney.getWidth(),
            bgRateMoney.getY() + bgRateMoney.getHeight()/2 - rateMoney.getHeight()/2 + 2);
  }
}
