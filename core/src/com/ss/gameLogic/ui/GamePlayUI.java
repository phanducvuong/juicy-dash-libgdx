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
import com.platform.IPlatform;
import com.ss.GMain;
import com.ss.core.effect.SoundEffects;
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
  private IPlatform plf = GMain.platform;
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

  private Group gAlertAds;
  private Image bgBlackAlertAds, btnAlertAds, btnXAlertAds;

  private Group gPanelInGame;
  private Image blackPanelInGame;

  public Particle pWin, pAllIn, pMoneyWheel;

  private Group gBigWin;
  private Image blackBigWin;
  private Label lbMoneyBigWin;
  private Button btnX2BigWin;

  private Group gAlertDonate;
  private Image blackDonate;

  private Group gAlertFailNetwork;
  private Image blackNetwork;

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
    initIconBigWin();
    initPanelDonate();
    initPanelFailNetwork();
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

  public void initIconBigWin() {

    blackBigWin = GUI.createImage(GMain.liengAtlas, "bg_black");
    blackBigWin.setSize(GStage.getWorldWidth(), GStage.getWorldHeight());

    gBigWin = new Group();
    Image flare = GUI.createImage(GMain.liengAtlas, "flare_big_win");
    flare.setOrigin(Align.center);
    gBigWin.setSize(flare.getWidth(), flare.getHeight());
    gBigWin.setOrigin(Align.center);
    gBigWin.setPosition(GStage.getWorldWidth()/2 - gBigWin.getWidth()/2,
            GStage.getWorldHeight()/2 - gBigWin.getHeight()/2);
    gBigWin.addActor(flare);

    Image bgBigWin = GUI.createImage(GMain.liengAtlas, "bg_big_win");
    bgBigWin.setPosition(flare.getX() + flare.getWidth()/2 - bgBigWin.getWidth()/2,
                         flare.getY() + flare.getHeight()/2 - bgBigWin.getHeight()/2 + 100);
    gBigWin.addActor(bgBigWin);

    Image bigWin;
    if (C.lang.idCountry.equals("vn"))
      bigWin = GUI.createImage(GMain.liengAtlas, "big_win_vn");
    else
      bigWin = GUI.createImage(GMain.liengAtlas, "big_win_en");

    bigWin.setPosition(flare.getX() + flare.getWidth()/2 - bigWin.getWidth()/2,
                       flare.getY() + flare.getHeight()/2 - bigWin.getHeight()/2 - 130);
    gBigWin.addActor(bigWin);

    lbMoneyBigWin = new Label("$1,000,000", new Label.LabelStyle(Config.PLUS_MONEY_FONT, null));
    lbMoneyBigWin.setAlignment(Align.left);
    lbMoneyBigWin.setPosition(bgBigWin.getX() + bgBigWin.getWidth()/2 - lbMoneyBigWin.getWidth()/2,
                              bgBigWin.getY() + bgBigWin.getHeight()/2 - lbMoneyBigWin.getHeight()/2);
    gBigWin.addActor(lbMoneyBigWin);

    Image iconChip = GUI.createImage(GMain.liengAtlas, "icon_chip_big_win");
    iconChip.setPosition(lbMoneyBigWin.getX() - iconChip.getWidth() - 20,
            lbMoneyBigWin.getY() + lbMoneyBigWin.getHeight()/2 - iconChip.getHeight()/2 + 10);
    gBigWin.addActor(iconChip);

    btnX2BigWin = new Button(GMain.liengAtlas, "btn_big_win", C.lang.moneyBigWin, Config.ALERT_FONT);
    btnX2BigWin.setPosition(bgBigWin.getX() + bgBigWin.getWidth()/2 - btnX2BigWin.getWidth()/2,
            bgBigWin.getY() + bgBigWin.getHeight() - btnX2BigWin.getHeight()/2);
    btnX2BigWin.setFontScale(.4f, .4f);
    btnX2BigWin.moveByLb(30, -50);
    btnX2BigWin.addToGroup(gBigWin);

    Image btnXBigWin = GUI.createImage(GMain.liengAtlas, "btn_x");
    btnXBigWin.setPosition(bigWin.getX() + bigWin.getWidth() - 10, bigWin.getY() - 30);
    btnXBigWin.setScale(.8f);
    gBigWin.addActor(btnXBigWin);

    gBigWin.setScale(0);

    //effect
    effect.rotate(flare);

    //listener
    btnX2BigWin.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);

        SoundEffects.startSound("btn_click");

        if (plf.isVideoRewardReady())
          plf.ShowVideoReward((boolean success) -> {

            if (success) {

              long money = GMain.pref.getLong("money") + game.bet.totalMoney * (Config.REWARD_BIG_WIN - 1);
              logic.saveMoney(money);
              game.lsBot.get(0).setTotalMoney(money);
              game.lsBot.get(0).convertTotalMoneyToString();

              hideBigWin();

            }
            else
              showAlertFailNetwork();

          });
        else
          showAlertFailNetwork();

      }
    });

    btnXBigWin.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);

        SoundEffects.startSound("btn_click");

        hideBigWin();

      }
    });

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
//        float rnd = Math.round(Math.random() * 2);
//        System.out.println(rnd);
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
    gPanelInGame.setPosition(GStage.getWorldWidth()/2 - gPanelInGame.getWidth()/2,
            GStage.getWorldHeight()/2 - gPanelInGame.getHeight()/2);
    gPanelInGame.addActor(panelInGame);

    Label lbTxt = new Label(C.lang.notifyExitGame, new Label.LabelStyle(Config.ALERT_FONT, null));
//    lbTxt.setFontScale(.7f);
    lbTxt.setAlignment(Align.center);
    lbTxt.setPosition(gPanelInGame.getWidth()/2 - lbTxt.getWidth()/2, 130);
    gPanelInGame.addActor(lbTxt);

    Button btnOK = new Button(GMain.liengAtlas, "btn_divide", C.lang.yes, Config.BUTTON_FONT);
    btnOK.setPosition(120, gPanelInGame.getHeight() - btnOK.getHeight() - 20);
    btnOK.setFontScale(.8f, .8f);
    btnOK.addToGroup(gPanelInGame);

    Button btnNo = new Button(GMain.liengAtlas, "btn_divide", C.lang.no, Config.BUTTON_FONT);
    btnNo.setPosition(gPanelInGame.getWidth() - btnNo.getWidth() - 120, btnOK.getY());
    btnNo.setFontScale(.8f, .8f);
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

        SoundEffects.startSound("btn_click");

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

        SoundEffects.startSound("btn_click");

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

        SoundEffects.startSound("btn_click");

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

        SoundEffects.startSound("btn_click");

        iconExit.setTouchable(Touchable.disabled);
        effect.click(iconExit, run);

      }
    });

  }

  private void initButtonBet() {

    btnTo = new Button(GMain.liengAtlas, "btn_to", C.lang.raise, Config.BUTTON_FONT);
    btnTo.setPosition(GStage.getWorldWidth()/2 + 20, GStage.getWorldHeight() - btnTo.getHeight() - 10);
    btnTo.startEftLight(game);
    btnTo.setFontScale(.8f, .8f);
//    btnTo.addToGroup(game.gBtn);

    btnTheo = new Button(GMain.liengAtlas, "btn_theo", C.lang.call, Config.BUTTON_FONT);
    btnTheo.setPosition(btnTo.getX() + btnTheo.getWidth(), btnTo.getY());
    btnTheo.startEftLight(game);
    btnTheo.setFontScale(.8f, .8f);
    btnTheo.moveByLb(0, -2f);
//    btnTheo.addToGroup(game.gBtn);

    btnUp = new Button(GMain.liengAtlas, "btn_up", C.lang.fold, Config.BUTTON_FONT);
    btnUp.setPosition(btnTheo.getX() + btnUp.getWidth(), btnTheo.getY());
    btnUp.startEftLight(game);
    btnUp.setFontScale(.8f, .8f);
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

  private void initPanelDonate() {

    blackDonate = GUI.createImage(GMain.liengAtlas, "bg_black");
    blackDonate.setSize(GStage.getWorldWidth(), GStage.getWorldHeight());

    gAlertDonate = new Group();
    Image donate = GUI.createImage(GMain.liengAtlas, "panel_ads");
    donate.setScale(.8f);
    gAlertDonate.setSize(donate.getWidth()*.8f, donate.getHeight()*.8f);
    gAlertDonate.setOrigin(Align.center);
    gAlertDonate.setPosition(GStage.getWorldWidth()/2 - gAlertDonate.getWidth()/2,
            GStage.getWorldHeight()/2 - gAlertDonate.getHeight()/2);
    gAlertDonate.addActor(donate);

    Label lbDonate = new Label(C.lang.donate, new Label.LabelStyle(Config.ALERT_FONT, null));
    lbDonate.setAlignment(Align.center);
    lbDonate.setFontScale(.9f);
    lbDonate.setPosition(donate.getX() + donate.getWidth()*donate.getScaleX()/2 - lbDonate.getWidth()/2,
            donate.getY() + donate.getHeight()*donate.getScaleY()/2 - lbDonate.getHeight()/2 - 60);
    gAlertDonate.addActor(lbDonate);

    Button btnOk = new Button(GMain.startSceneAtlas, "btn_get", C.lang.yes, Config.BUTTON_FONT);
    btnOk.setPosition(donate.getX() + donate.getWidth()*donate.getScaleX()/2 - btnOk.getWidth()/2,
            donate.getY() + donate.getHeight()*donate.getScaleY() - btnOk.getHeight() - 5);
    btnOk.moveByLb(0, -5);
    btnOk.addToGroup(gAlertDonate);

    gAlertDonate.setScale(0);

    btnOk.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);

        SoundEffects.startSound("btn_click");

        long money = GMain.pref.getLong("money") + Config.MONEY_DONATE;
        logic.saveMoney(money);
        game.lsBot.get(0).setTotalMoney(money);
        game.lsBot.get(0).convertTotalMoneyToString();

        removeAlertAds();
        btnNewRound.addToGroup(game.gBtn);
        effect.sclMaxToMin(gAlertDonate, () -> {
          blackDonate.remove();
          gAlertDonate.remove();
        });

      }
    });

  }

  private void initPanelFailNetwork() {

    blackNetwork = GUI.createImage(GMain.liengAtlas, "bg_black");
    blackNetwork.setSize(GStage.getWorldWidth(), GStage.getWorldHeight());

    gAlertFailNetwork = new Group();
    Image netWork = GUI.createImage(GMain.liengAtlas, "panel_ads");
    netWork.setScale(.8f);
    gAlertFailNetwork.setSize(netWork.getWidth()*.8f, netWork.getHeight()*.8f);
    gAlertFailNetwork.setOrigin(Align.center);
    gAlertFailNetwork.setPosition(GStage.getWorldWidth()/2 - gAlertFailNetwork.getWidth()/2,
            GStage.getWorldHeight()/2 - gAlertFailNetwork.getHeight()/2);
    gAlertFailNetwork.addActor(netWork);

    Label lbFailNetwork = new Label(C.lang.failNetwork, new Label.LabelStyle(Config.ALERT_FONT, null));
    lbFailNetwork.setAlignment(Align.center);
    lbFailNetwork.setFontScale(.9f);
    lbFailNetwork.setPosition(netWork.getX() + netWork.getWidth()*netWork.getScaleX()/2 - lbFailNetwork.getWidth()/2,
            netWork.getY() + netWork.getHeight()*netWork.getScaleY()/2 - lbFailNetwork.getHeight()/2 - 60);
    gAlertFailNetwork.addActor(lbFailNetwork);

    Button btnOk = new Button(GMain.startSceneAtlas, "btn_get", C.lang.yes, Config.BUTTON_FONT);
    btnOk.setPosition(netWork.getX() + netWork.getWidth()*netWork.getScaleX()/2 - btnOk.getWidth()/2,
            netWork.getY() + netWork.getHeight()*netWork.getScaleY() - btnOk.getHeight() - 5);
    btnOk.moveByLb(0, -5);
    btnOk.addToGroup(gAlertFailNetwork);

    gAlertFailNetwork.setScale(0);

    btnOk.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);

        SoundEffects.startSound("btn_click");

        effect.sclMaxToMin(gAlertFailNetwork, () -> {
          blackNetwork.remove();
          gAlertFailNetwork.remove();
        });

      }
    });

    game.gTest.addActor(gAlertFailNetwork);

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
            lbMinBet.setPosition(GStage.getWorldWidth()/2 - lbMinBet.getWidth()/2,
                                    GStage.getWorldHeight()/2 - lbMinBet.getHeight()/2);
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

        SoundEffects.startSound("aww");

        hideBtnBet();
        effect.click(btnUp, run);

      }
    });

    btnNewRound.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);

        SoundEffects.startSound("btn_click");

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
                      long money = game.lsBotActive.get(0).getTotalMoney();
                      game.lsBotActive.get(0).setTotalMoney(Config.MONEY_ADS + money);
                      game.lsBotActive.get(0).convertTotalMoneyToString();

                      logic.saveMoney(game.lsBotActive.get(0).getTotalMoney());

                      btnNewRound.addToGroup(game.gBtn);
                      effect.sclMaxToMin(gAlertAds, () -> {
                        bgBlackAlertAds.remove();
                        gAlertAds.remove();
                      });
                    }
                    else
                      showAlertFailNetwork();

                  });
                else if (game.lsBot.get(0).getTotalMoney() < 10000)
                  showAlertDonate();
                else
                  showAlertFailNetwork();

        };

        SoundEffects.startSound("btn_click");

        btnAlertAds.setTouchable(Touchable.disabled);
        effect.click(btnAlertAds, run);

      }
    });

    btnXAlertAds.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);

        SoundEffects.startSound("btn_click");

        btnNewRound.addToGroup(game.gBtn);
        effect.sclMaxToMin(gAlertAds, () -> {
          bgBlackAlertAds.remove();
          gAlertAds.remove();
        });

      }
    });

  }

  private void initUIGame() {

//    btnNewRound = new Button()

    bgTable = GUI.createImage(GMain.liengAtlas, "bg_table");
    bgTable.setSize(GStage.getWorldWidth(), GStage.getWorldHeight());

    bgTotalMoneyBet = GUI.createImage(GMain.liengAtlas, "bg_money_bet");
    bgTotalMoneyBet.setPosition(GStage.getWorldWidth()/2 - bgTotalMoneyBet.getWidth()/2,
                                GStage.getWorldHeight()/2 - bgTotalMoneyBet.getHeight()/2 - 100);

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
    lbMinBet.setPosition(GStage.getWorldWidth()/2 - lbMinBet.getWidth()/2, GStage.getWorldHeight()/2 - lbMinBet.getHeight()/2);
    lbMinBet.getColor().a = 0;

    //label: alert ads
    bgBlackAlertAds = GUI.createImage(GMain.liengAtlas, "bg_black");
    bgBlackAlertAds.setSize(GStage.getWorldWidth(), GStage.getWorldHeight());

    gAlertAds = new Group();
    Image bgAlert = GUI.createImage(GMain.liengAtlas, "panel_ads");
    gAlertAds.setSize(bgAlert.getWidth(), bgAlert.getHeight());

    gAlertAds.setOrigin(bgAlert.getWidth()/2, bgAlert.getHeight()/2);
    gAlertAds.setPosition(GStage.getWorldWidth()/2 - bgAlert.getWidth()/2, GStage.getWorldHeight()/2 - bgAlert.getHeight()/2);
    gAlertAds.addActor(bgAlert);

    Label lbAertAds = new Label(C.lang.adsOutOfMoney, new Label.LabelStyle(Config.ALERT_FONT, null));
    lbAertAds.setAlignment(Align.center);
    lbAertAds.setFontScale(.9f);
    lbAertAds.setPosition(bgAlert.getX() + bgAlert.getWidth()/2 - lbAertAds.getWidth()/2,
                              bgAlert.getY() + bgAlert.getHeight()/2 - lbAertAds.getHeight()/2 - 180);
    gAlertAds.addActor(lbAertAds);

    btnAlertAds = GUI.createImage(GMain.liengAtlas, "btn_ads");
    btnAlertAds.setPosition(bgAlert.getX() + bgAlert.getWidth()/2 - btnAlertAds.getWidth()/2,
                            bgAlert.getY() + bgAlert.getHeight() - btnAlertAds.getHeight() - 20);
    btnAlertAds.setOrigin(Align.center);
    gAlertAds.addActor(btnAlertAds);

    btnXAlertAds = GUI.createImage(GMain.liengAtlas, "btn_x");
    btnXAlertAds.setPosition(bgAlert.getX() + bgAlert.getWidth() - btnXAlertAds.getWidth()/2,-btnXAlertAds.getHeight()/2);
    btnXAlertAds.setOrigin(Align.center);
    gAlertAds.addActor(btnXAlertAds);

    gAlertAds.setScale(0);

    //label: button new round
    btnNewRound = new Button(GMain.liengAtlas, "btn_divide", C.lang.divideCard, Config.ALERT_FONT);
    btnNewRound.setPosition(GStage.getWorldWidth()/2 - btnNewRound.getWidth()/2,
                            GStage.getWorldHeight()/2 - btnNewRound.getHeight()/2);
    btnNewRound.moveByLb(0, -10);
    if (C.lang.idCountry.equals("vn"))
      btnNewRound.setFontScale(.8f, .8f);
    else
      btnNewRound.setFontScale(.6f, .6f);
//    btnNewRound.addToGroup(game.gBtn);

  }

  public void showAlertAds() {
    game.gAlert.addActor(bgBlackAlertAds);
    game.gAlert.addActor(gAlertAds);
    effect.sclMinToMax(gAlertAds);
  }

  private void removeAlertAds() {
    gAlertAds.setScale(0);
    bgBlackAlertAds.remove();
    gAlertAds.remove();
  }

  public void showAlertFailNetwork() {

    game.gAlert.addActor(blackNetwork);
    game.gAlert.addActor(gAlertFailNetwork);
    effect.sclMinToMax(gAlertFailNetwork);

  }

  public void showAlertDonate() {

    game.gAlert.addActor(blackDonate);
    game.gAlert.addActor(gAlertDonate);
    effect.sclMinToMax(gAlertDonate);

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

    SoundEffects.startSound("show_all_card");

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

      SoundEffects.startSound("win");

      pWin.start(GStage.getWorldWidth()/2, GStage.getWorldHeight()/2 - 100, Config.SCL_EFFECT_WIN); //particle
      winner.eftMoneyWinner(game, game.bet.totalMoney);
      logic.chkMoneyBotIsZero(game.lsBotActive, game.moneyBet, game.lsBot.get(0).getTotalMoney());

      showBigWin();
      game.countPlayWinInGame += 1;
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

      SoundEffects.startSound("win");

      pWin.start(GStage.getWorldWidth()/2, GStage.getWorldHeight()/2 - 100, Config.SCL_EFFECT_WIN); //particle
      winner.eftMoneyWinner(game, game.bet.totalMoney);
      logic.chkMoneyBotIsZero(game.lsBotActive, game.moneyBet, game.lsBot.get(0).getTotalMoney());

      showBigWin();
      game.countPlayWinInGame += 1;
    }
  }

  private void showBigWin() {

    long moneyBetPlayer = game.lsBot.get(0).getTotalMoneyBet();
    if (game.bet.totalMoney >= (game.moneyBet * Config.TIME_TO_SHOW_BIG_WIN_IN_GAME)) {
      game.gAlert.addActor(blackBigWin);
      game.gAlert.addActor(gBigWin);
      lbMoneyBigWin.setText(logic.convertMoneyBet(game.bet.totalMoney));

      effect.zoomIn(gBigWin, 1f, 1f);
    }

  }

  private void hideBigWin() {

    effect.zoomOut(gBigWin, 2f, 2f,
       () -> {
          blackBigWin.remove();
          gBigWin.remove();
       }
    );

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

  public void showAdsFullscreen() {
    plf.ShowFullscreen();
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
