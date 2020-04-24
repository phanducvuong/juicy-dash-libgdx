package com.ss.gameLogic.ui;

import com.badlogic.gdx.Gdx;
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
import com.ss.gameLogic.effects.Particle;
import com.ss.gameLogic.interfaces.IClickCard;
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

  private Image bgTable, bgTotalMoneyBet, bgMoneyBetInGame, iconSetting, iconExit;

  private Button btnUp, btnTo, btnTheo, btnNewRound;
  private Label lbTotoalMoneyBet, lbWin;
  public Label lbMoneyBetInGame;

  private Group gBannerWin;
  private Image bannerWin;

  private GClipGroup gRateMoney;
  private Image controlRateMoney, rateMoney, bgRateMoney;
  private Label lbMoneyRate, lbMinBet;
  private long moneyBet; //rate money to bet of player
  private float rateXMin, rateXMax;

  private Group gAlerrAds;
  private Image bgBlackAlertAds, btnAlertAds, btnXAlertAds;

  private Group gPanelInGame;
  private Image blackPanelInGame;

  public Particle pWin, pAllIn, pMoneyWheel;

  public GamePlayUI(Game game) {

    this.game = game;
    this.effect = Effect.getInstance(game);
    this.lsAllChip = new ArrayList<>();

    initUIGame();
    initButtonBet();
    initRateMoney();
    initIcon();
    initPanelInGame();
    initParticles();
    handleClickBtnBet();
    handleClickBtnAlert();
    hideBtnBet();

  }

  public void addToScene() {

    game.gBackground.addActor(bgTable);
    game.gBackground.addActor(bgTotalMoneyBet);
    game.gBackground.addActor(bgMoneyBetInGame);
    game.gBackground.addActor(lbMoneyBetInGame);
    game.gBackground.addActor(lbTotoalMoneyBet);
    btnNewRound.addToGroup(game.gBtn);
    btnTo.addToGroup(game.gBtn);
    btnTheo.addToGroup(game.gBtn);
    btnUp.addToGroup(game.gBtn);
    game.gBackground.addActor(gRateMoney);
    game.gBot.addActor(lbMoneyRate);
    game.gBtn.addActor(iconSetting);
    game.gBtn.addActor(iconExit);

    hideBtnBet();

  }

  public void remove() {

    bgTable.remove();
    bgTotalMoneyBet.remove();
    bgMoneyBetInGame.remove();
    lbMoneyBetInGame.remove();
    lbTotoalMoneyBet.remove();
    btnNewRound.remove();

    btnTo.remove();
    btnTheo.remove();
    btnUp.remove();
    gRateMoney.remove();
    lbMoneyRate.remove();
    iconSetting.remove();
    iconExit.remove();

  }

  private void initParticles() {

    if (C.lang.idCountry.equals("vn"))
      pWin = new Particle(game.gEffect, Gdx.files.internal("particles/win_vn"));
    else
      pWin = new Particle(game.gEffect, Gdx.files.internal("particles/win_en"));

    pAllIn = new Particle(game.gEffect, Gdx.files.internal("particles/all_in"));

    pMoneyWheel = new Particle(game.gEffect, Gdx.files.internal("particles/money_wheel"));

//    Image test = GUI.createImage(GMain.liengAtlas, "btn_x");
//    game.gTest.addActor(test);
//    test.addListener(new ClickListener() {
//
//      @Override
//      public void clicked(InputEvent event, float x, float y) {
//        super.clicked(event, x, y);
//
//        pMoneyWheel.start(500, 500, 1f);
//
//      }
//    });

  }

  private void initPanelInGame() {

    blackPanelInGame = GUI.createImage(GMain.liengAtlas, "bg_black");
    blackPanelInGame.setSize(GStage.getWorldWidth(), GStage.getWorldHeight());

    gPanelInGame = new Group();
    Image panelInGame = GUI.createImage(GMain.liengAtlas, "panel_ads");
    gPanelInGame.setSize(panelInGame.getWidth(), panelInGame.getHeight());
    gPanelInGame.setOrigin(Align.center);
    gPanelInGame.setPosition(Config.CENTER_X - gPanelInGame.getWidth()/2, Config.CENTER_Y - gPanelInGame.getHeight()/2);
    gPanelInGame.addActor(panelInGame);

    Label lbTxt = new Label(C.lang.notifyExitGame, new Label.LabelStyle(Config.ALERT_FONT, null));
    lbTxt.setFontScale(.7f);
    lbTxt.setAlignment(Align.center);
    lbTxt.setPosition(gPanelInGame.getWidth()/2 - lbTxt.getWidth()/2, 170);
    gPanelInGame.addActor(lbTxt);

    Button btnOK = new Button(GMain.liengAtlas, "btn_divide", C.lang.yes, Config.ALERT_FONT);
    btnOK.setPosition(120, gPanelInGame.getHeight() - btnOK.getHeight() - 20);
    btnOK.setFontScale(.7f, .7f);
    btnOK.moveByLb(0, -10);
    btnOK.addToGroup(gPanelInGame);

    Button btnNo = new Button(GMain.liengAtlas, "btn_divide", C.lang.no, Config.ALERT_FONT);
    btnNo.setPosition(gPanelInGame.getWidth() - btnNo.getWidth() - 120, btnOK.getY());
    btnNo.setFontScale(.7f, .7f);
    btnNo.moveByLb(0, -10);
    btnNo.addToGroup(gPanelInGame);

    gPanelInGame.setScale(0);

    btnOK.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);

        Runnable run = () -> {

          logic.minusMoneyBot(game.lsBot.get(0), game.moneyBet);

          btnOK.setTouchable(Touchable.enabled);
          game.resetData();
          game.startScene.showStartScene();
          blackPanelInGame.remove();
          gPanelInGame.remove();
          remove(); //remove game play ui to scene

        };

        btnOK.setTouchable(Touchable.disabled);
        effect.click(btnOK, run);

      }
    });

    btnNo.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);

        Runnable run = () -> {

          btnNo.setTouchable(Touchable.enabled);
          effect.sclMaxToMin(gPanelInGame, () -> {
            blackPanelInGame.remove();
            gPanelInGame.remove();
          });


        };

        btnNo.setTouchable(Touchable.disabled);
        effect.click(btnNo, run);

      }
    });

  }

  private void initIcon() {

    iconSetting = GUI.createImage(GMain.startSceneAtlas, "icon_setting");
    iconSetting.setScale(.6f);
    iconSetting.setPosition(GStage.getWorldWidth() - iconSetting.getWidth() - 10, 10);
    iconSetting.setOrigin(Align.center);
//    game.gBtn.addActor(iconSetting);

    iconExit = GUI.createImage(GMain.startSceneAtlas, "icon_exit");
    iconExit.setScale(.6f);
    iconExit.setPosition(iconSetting.getX() - iconExit.getWidth() + 20, iconSetting.getY());
    iconExit.setOrigin(Align.center);
//    game.gBtn.addActor(iconExit);

    iconSetting.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);

        Runnable run = () -> {
          iconSetting.setTouchable(Touchable.enabled);
          game.gStartScene.addActor(game.startScene.blackSetting);
          game.gStartScene.addActor(game.startScene.gPanelSetting);
          effect.sclMinToMax(game.startScene.gPanelSetting);
        };

        iconSetting.setTouchable(Touchable.disabled);
        effect.click(iconSetting, run);

      }
    });

    iconExit.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);

        Runnable run = () -> {

          iconExit.setTouchable(Touchable.enabled);
          if (game.isInGame) {
            game.gAlert.addActor(blackPanelInGame);
            game.gAlert.addActor(gPanelInGame);
            effect.sclMinToMax(gPanelInGame);
          }
          else {
            game.resetData();
            game.startScene.showStartScene();
            remove(); //remove game play ui to scene
          }

        };

        iconExit.setTouchable(Touchable.disabled);
        effect.click(iconExit, run);

      }
    });

  }

  private void initButtonBet() {

    btnTo = new Button(GMain.liengAtlas, "btn_to", C.lang.raise, Config.BUTTON_FONT);
    btnTo.setPosition(GStage.getWorldWidth()/2 + 20, GStage.getWorldHeight() - btnTo.getHeight() - 10);
    btnTo.startEftLight(game);
    btnTo.setFontScale(.6f, .6f);
//    btnTo.addToGroup(game.gBtn);

    btnTheo = new Button(GMain.liengAtlas, "btn_theo", C.lang.call, Config.BUTTON_FONT);
    btnTheo.setPosition(btnTo.getX() + btnTheo.getWidth(), btnTo.getY());
    btnTheo.startEftLight(game);
    btnTheo.setFontScale(.6f, .6f);
//    btnTheo.addToGroup(game.gBtn);

    btnUp = new Button(GMain.liengAtlas, "btn_up", C.lang.fold, Config.BUTTON_FONT);
    btnUp.setPosition(btnTheo.getX() + btnUp.getWidth(), btnTheo.getY());
    btnUp.startEftLight(game);
    btnUp.setFontScale(.6f, .6f);
//    btnUp.addToGroup(game.gBtn);

  }

  private void initRateMoney() {

    gRateMoney = new GClipGroup();
    bgRateMoney = GUI.createImage(GMain.liengAtlas, "bg_rate_money");
    gRateMoney.setClipArea(0, 0, bgRateMoney.getWidth(), bgRateMoney.getHeight());
    gRateMoney.setPosition(btnTo.getX() + 15, btnTo.getY() - btnTo.getHeight() + 30);

    controlRateMoney = GUI.createImage(GMain.liengAtlas, "control_rate_money");
    controlRateMoney.setPosition(bgRateMoney.getX() - 2,
                                  bgRateMoney.getY() + bgRateMoney.getHeight()/2 - controlRateMoney.getHeight()/2);

    rateMoney = GUI.createImage(GMain.liengAtlas, "rate_money");
    rateMoney.setPosition(controlRateMoney.getX() - rateMoney.getWidth() + controlRateMoney.getWidth(),
            bgRateMoney.getY() + bgRateMoney.getHeight()/2 - rateMoney.getHeight()/2 + 2);
    gRateMoney.addActor(rateMoney);
    gRateMoney.addActor(bgRateMoney);
    gRateMoney.addActor(controlRateMoney);
//    game.gBackground.addActor(gRateMoney);

    lbMoneyRate = new Label("$0", new Label.LabelStyle(Config.PLUS_MONEY_FONT, null));
    lbMoneyRate.setFontScale(.7f);
    lbMoneyRate.setAlignment(Align.center);
    lbMoneyRate.setPosition(gRateMoney.getX() + bgRateMoney.getWidth()/2 - 20,
                                gRateMoney.getY() + bgRateMoney.getHeight() - lbMoneyRate.getHeight() - 50);
//    game.gBot.addActor(lbMoneyRate);

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
            btnTo.setTouchable(Touchable.enabled);
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

    btnNewRound.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);

        btnNewRound.remove();
        game.newRound();

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
          if (GMain.platform.isVideoRewardReady())
            GMain.platform.ShowVideoReward((boolean success) -> {

              if (success) {
                game.lsBotActive.get(0).setTotalMoney(Config.MONEY_ADS);
                game.lsBotActive.get(0).convertTotalMoneyToString();

                logic.saveMoney(game.lsBotActive.get(0).getTotalMoney());

                btnNewRound.addToGroup(game.gBtn);
                effect.sclMaxToMin(gAlerrAds, () -> {
                  bgBlackAlertAds.remove();
                  gAlerrAds.remove();
                });
              }

            });

        };

        btnAlertAds.setTouchable(Touchable.disabled);
        effect.click(btnAlertAds, run);

      }
    });

    btnXAlertAds.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);

        btnNewRound.addToGroup(game.gBtn);
        effect.sclMaxToMin(gAlerrAds, () -> {
          bgBlackAlertAds.remove();
          gAlerrAds.remove();
        });

      }
    });

  }

  private void initUIGame() {

//    btnNewRound = new Button()

    bgTable = GUI.createImage(GMain.liengAtlas, "bg_table");
    bgTable.setSize(GStage.getWorldWidth(), GStage.getWorldHeight());

    bgTotalMoneyBet = GUI.createImage(GMain.liengAtlas, "bg_money_bet");
    bgTotalMoneyBet.setPosition(Config.CENTER_X - bgTotalMoneyBet.getWidth()/2,
                                Config.CENTER_Y - bgTotalMoneyBet.getHeight()/2 - 100);

    bgMoneyBetInGame = GUI.createImage(GMain.liengAtlas, "bg_money_bet_in_game");
    bgMoneyBetInGame.setPosition(10, 10);

    lbMoneyBetInGame = new Label(C.lang.bet + ": $20,000", new Label.LabelStyle(Config.RANK_FONT, null));
    lbMoneyBetInGame.setFontScale(.7f, .8f);
    lbMoneyBetInGame.setAlignment(Align.center);
    lbMoneyBetInGame.setPosition(bgMoneyBetInGame.getX() + bgMoneyBetInGame.getWidth()/2 - lbMoneyBetInGame.getWidth()/2,
            bgMoneyBetInGame.getY() + bgMoneyBetInGame.getHeight()/2 - lbMoneyBetInGame.getHeight()/2);
//    game.gBackground.addActor(lbMoneyBetInGame);

    lbTotoalMoneyBet = new Label("$10,000", new Label.LabelStyle(Config.MONEY_FONT, null));
    lbTotoalMoneyBet.setAlignment(Align.center);
    lbTotoalMoneyBet.setFontScale(.5f, .6f);
    lbTotoalMoneyBet.setPosition(bgTotalMoneyBet.getX() + bgTotalMoneyBet.getWidth()*bgTotalMoneyBet.getScaleX()/2 - lbTotoalMoneyBet.getWidth()/2 + 30,
                                  bgTotalMoneyBet.getY() + bgTotalMoneyBet.getHeight()*bgTotalMoneyBet.getScaleY()/2 - lbTotoalMoneyBet.getHeight()/2 - 5);
//    game.gBackground.addActor(lbTotoalMoneyBet);

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
    lbMinBet.setFontScale(.6f);
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
    lbAertAds.setFontScale(.55f);
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
    btnNewRound = new Button(GMain.liengAtlas, "btn_divide", C.lang.divideCard, Config.ALERT_FONT);
    btnNewRound.setPosition(Config.CENTER_X - btnNewRound.getWidth()/2,
                            Config.CENTER_Y - btnNewRound.getHeight()/2);
    btnNewRound.moveByLb(0, -10);
    btnNewRound.setFontScale(.5f, .5f);
    btnNewRound.moveByLb(0, -5);
//    btnNewRound.addToGroup(game.gBtn);

  }

  public void showAlertAds() {
    game.gAlert.addActor(bgBlackAlertAds);
    game.gAlert.addActor(gAlerrAds);
    effect.sclMinToMax(gAlerrAds);
  }

  public void showBtnNewRound() {
    btnNewRound.addToGroup(game.gBtn);
  }

  public void eftLbTotalMoney(long money) {
    effect.raiseMoney(lbTotoalMoneyBet, money);
  }

  @Override
  public void click(Card cardDown, Card cardUp) {
    effect.flipCard(cardDown, cardUp);
  }

  public void showAllWhenFindWinner(List<Bot> lsBot, Bot winner) {

//    System.out.println("TOTAL MONEY: " + game.bet.totalMoney);

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

    if (winner.id != 0) {
      pMoneyWheel.start(winner.avatar.getX() + winner.avatar.getWidth()/2,
              winner.avatar.getY() + winner.avatar.getHeight()/2, .5f);
      effect.winnerIsBot(winner);
    }
    else {
      pWin.start(Config.CENTER_X, Config.CENTER_Y - 100, Config.SCL_EFFECT_WIN); //particle
      winner.eftMoneyWinner(game, game.bet.totalMoney);
    }

  }

  public void eftArrangeLsChip(Bot winner) {
    effect.arrangeLsChip(lsAllChip, winner);
  }

  public void showCardWinner(Bot winner) {
    effect.arrangeLsChip(lsAllChip, winner);
    if (winner.id != 0) {
      pMoneyWheel.start(winner.avatar.getX() + winner.avatar.getWidth()/2,
              winner.avatar.getY() + winner.avatar.getHeight()/2, .5f);
      effect.winnerIsBot(winner);
    }
    else {
      pWin.start(Config.CENTER_X, Config.CENTER_Y - 100, Config.SCL_EFFECT_WIN); //particle
      winner.eftMoneyWinner(game, game.bet.totalMoney);
    }
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

    if (game.lsBotActive.size() > 0)
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
    game.isInGame = false;
    logic.saveMoney(game.lsBot.get(0).getTotalMoney());
    System.out.println(game.lsBot.get(0).getTotalMoney() + "    " + GMain.pref.getLong("money"));
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
    pMoneyWheel.remove();
    lbTotoalMoneyBet.setText("$0");
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
