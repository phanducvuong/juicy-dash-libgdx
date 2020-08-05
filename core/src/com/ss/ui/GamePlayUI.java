package com.ss.ui;

import static com.badlogic.gdx.math.Interpolation.*;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;
import com.ss.GMain;
import com.ss.config.C;
import com.ss.config.Config;
import com.ss.controller.GameUIController;
import com.ss.core.effect.SoundEffects;
import com.ss.core.exSprite.particle.GParticleSystem;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import com.ss.gameLogic.effects.Particle;
import com.ss.objects.Piece;
import com.ss.utils.Button;
import com.ss.utils.Clipping;
import com.ss.utils.Solid;
import com.ss.utils.Util;

public class GamePlayUI extends Group {

  public final float CENTER_X = GStage.getWorldWidth()/2;
  public final float CENTER_Y = GStage.getWorldHeight()/2;

  private       GameUIController controller;
  private       I18NBundle       locale = C.lang.locale;

  public        Group            gBackground, gItem, gAnimLb, gAnimSkill, gPopup, gTutorial;
  public static Image            bgTable;
  private       Label            lbRound;

  public        Image    iPause;

  private       Group    gTime;
  public        Clipping timeLine;
  private       Image    bgBar;
  public        Label    lbTime;
  private float count    = 0f;
  public static Vector2  posCLock;

  private       Group     gScore;
  private       Image     bgScore;
  private       Label     lbScore, lbGoal;
  public        Clipping  scoreLine;
  public static float     posScoreX = 0,
                          posScoreY = 0;

  private Group     gPopupAdsTime;

  private Group     gPopupGameOver;
  private Label     lbRoundGameOver,
                    lbScoreGameOver,
                    lbGoalGameOver;

  private Particle  pComplete;
  private Group     gLbComplete;
  private Label     lbComplete;

  private Group     gAnimLbRound;
  private Label     animLbRound;
  private Image     black;

  private Group     gItemStar,
                    gItemBoom,
                    gFlareSkillItem;
  private Image     itemStar, itemBoom, flare;             //start anim boom and star when user click item boom | item star
  private Label     lbAmountItemStar,
                    lbAmountBoom,
                    lbTutorialSkill;

  private Group     gPopupAdsSkill;
  private Image     imgPopupAdsSkillBoom,
                    imgPopupAdsSkillStar,
                    imgBtnXPopupSkill;
  private Label     lbAdsSkill;
  private Button    btnOKPopupAdsSkill;

  public boolean    isChooseBoom             = false,
                    isChooseStar             = false;

  private Particle  pWonder,
                    pLovely,
                    pNewRound,
                    pWind;
  public  Particle  pBoom, pFallLeaf, pStar;

  public GamePlayUI(GameUIController controller) {

    //label: init layer
    this.gBackground  = new Group();
    this.gItem        = new Group();
    this.gAnimSkill   = new Group();
    this.gAnimLb      = new Group();
    this.gPopup       = new Group();
    this.gTutorial    = new Group();

    this.controller   = controller;

    //label: init particle
    this.pWonder      = new Particle(gAnimSkill, Config.WONDER, GMain.particleAtlas);
    this.pWonder.initLsEmitter();

    this.pLovely      = new Particle(gAnimSkill, Config.LOVELY, GMain.particleAtlas);
    this.pNewRound    = new Particle(gAnimSkill, Config.NEW_ROUND, GMain.particleAtlas);
    this.pWind        = new Particle(gAnimSkill, Config.WIND, GMain.particleAtlas);
    this.pBoom        = new Particle(gAnimSkill, Config.BOOM, GMain.particleAtlas);
    this.pFallLeaf    = new Particle(gAnimSkill, Config.FALL_LEAF, GMain.particleAtlas);
    this.pStar        = new Particle(gAnimSkill, Config.SKILL_STAR, GMain.particleAtlas);

    this.addActor(gBackground);
    this.addActor(gItem);
    this.addActor(gAnimLb);
    this.addActor(gAnimSkill);
    this.addActor(gPopup);
    this.addActor(gTutorial);

    this.setSize(CENTER_X*2, CENTER_Y*2);

    this.black = new Image(Solid.create(new Color(0/255f, 0/255f, 0/255f, .35f)));
    this.black.setSize(CENTER_X*2, CENTER_Y*2);

    initBg();
    initTime();
    initScore();
    initIcon();
    initLbLvUp();
    initPopupAdsTime();
    initPopupAdsSkill();
    initPopupGameOver();
    initAnimLbRound();

  }

  private void initAnimLbRound() {
    gAnimLbRound = new Group();
    Image bgRound = GUI.createImage(GMain.bgAtlas, "bg_round");
    gAnimLbRound.setSize(bgRound.getWidth(), bgRound.getHeight());
    gAnimLbRound.setPosition(-gAnimLbRound.getWidth() - 10, CENTER_Y - gAnimLbRound.getHeight()/2);
    gAnimLbRound.addActor(bgRound);

    animLbRound = new Label(locale.get("round") + " 3", new Label.LabelStyle(Config.whiteFont, null));
    animLbRound.setAlignment(Align.center);
    animLbRound.setFontScale(1.7f);
    animLbRound.setPosition(bgRound.getX() + bgRound.getWidth()/2 - animLbRound.getWidth()/2,
            bgRound.getY() + bgRound.getHeight()/2 - animLbRound.getHeight()/2 - 10);
    gAnimLbRound.addActor(animLbRound);
  }

  private void initLbLvUp() {
    gLbComplete = new Group();
    lbComplete  = new Label(locale.get("complete"), new Label.LabelStyle(Config.greenFont, null));
    lbComplete.setAlignment(Align.center);
    lbComplete.setFontScale(5f);
    gLbComplete.setSize(lbComplete.getWidth(), lbComplete.getHeight());
    gLbComplete.addActor(lbComplete);
    gLbComplete.setOrigin(Align.center);
    gLbComplete.getColor().a = 0f;
    gLbComplete.setPosition(CENTER_X - gLbComplete.getWidth()*gLbComplete.getScaleX()/2,
                           CENTER_Y - gLbComplete.getHeight()*gLbComplete.getScaleY()/2);

    pComplete = new Particle(gAnimSkill, Config.EXPLODE, GMain.particleAtlas);
  }

  private void initTime() {

    gTime = new Group();
    bgBar = GUI.createImage(GMain.bgAtlas, "bg_line");
    gTime.setSize(bgBar.getWidth(), bgBar.getHeight());
    gTime.setPosition(CENTER_X*2 - gTime.getWidth() - 25,
            bgTable.getY() - bgBar.getHeight() - 30);
    gTime.addActor(bgBar);

    timeLine = new Clipping(bgBar.getX() + 6, bgBar.getY() + 4, GMain.bgAtlas, "line_time");
    gTime.addActor(timeLine);

    lbTime = new Label("", new Label.LabelStyle(Config.whiteFont, null));
    lbTime.setAlignment(Align.center);
    lbTime.setPosition(bgBar.getX() + bgBar.getWidth()/2 - lbTime.getWidth()/2,
            bgBar.getY() + bgBar.getHeight()/2 - lbTime.getHeight()/2 - 8);
    gTime.addActor(lbTime);
    setTimeLineForNewRound(Config.TIME_START_GAME);

    Image clock = GUI.createImage(GMain.itemAtlas, "item_clock");
    clock.setScale(.5f);
    clock.setPosition(bgBar.getX() + bgBar.getWidth()/2 - clock.getWidth()*clock.getScaleX() - 30,
            bgBar.getY() - clock.getHeight()*clock.getScaleY());
    if (locale.get("id_country").equals("vn"))
      clock.moveBy(-30, 0);
    gTime.addActor(clock);

    posCLock   = new Vector2();
    posCLock.x = gTime.getX() + clock.getX();
    posCLock.y = gTime.getY() - clock.getHeight()*clock.getScaleY();

    Label lbTxtTime = new Label(locale.get("txt_time"), new Label.LabelStyle(Config.whiteFont, null));
    lbTxtTime.setPosition(clock.getX() + clock.getWidth()*clock.getScaleX() + 5,
            clock.getY() + clock.getHeight()*clock.getScaleY()/2 - lbTxtTime.getHeight()/2 - 5);
    gTime.addActor(lbTxtTime);

    gBackground.addActor(gTime);

  }

  private void initScore() {

    gScore = new Group();
    bgScore = GUI.createImage(GMain.bgAtlas, "bg_line");
    gScore.setSize(bgScore.getWidth(), bgScore.getHeight());
    gScore.setPosition(gTime.getX(), gTime.getY() - gScore.getHeight() - 50);
    gScore.addActor(bgScore);
    posScoreX = gScore.getX();
    posScoreY = gScore.getY();

    scoreLine = new Clipping(bgScore.getX() + 6,bgScore.getY() + 4, GMain.bgAtlas, "line_score");
    scoreLine.clipBy(1f, 0f);
    gScore.addActor(scoreLine);

    Label lbTxtScore = new Label(locale.get("txt_score"),
            new Label.LabelStyle(Config.whiteFont, null));
    lbTxtScore.setPosition(bgScore.getX(), bgScore.getY() - lbTxtScore.getHeight() - 10);
    gScore.addActor(lbTxtScore);

    lbScore = new Label("0", new Label.LabelStyle(Config.whiteFont, null));
    lbScore.setAlignment(Align.left);
    lbScore.setPosition(bgScore.getX() + 5,
            bgScore.getY() + bgScore.getHeight()/2 - lbScore.getHeight()/2 - 8);
    gScore.addActor(lbScore);

    Label lbTxtGoal = new Label(locale.get("txt_goal"),
            new Label.LabelStyle(Config.whiteFont, null));
    lbTxtGoal.setPosition(bgScore.getX() + bgScore.getWidth() - lbTxtGoal.getWidth(),
            bgScore.getY() - lbTxtGoal.getHeight() - 10);
    gScore.addActor(lbTxtGoal);

    lbGoal = new Label("3000", new Label.LabelStyle(Config.whiteFont, null));
    lbGoal.setAlignment(Align.right);
    lbGoal.setPosition(bgScore.getX() + bgScore.getWidth() - lbGoal.getWidth() - 8,
            bgScore.getY() + bgScore.getHeight()/2 - lbGoal.getHeight()/2 - 8);
    gScore.addActor(lbGoal);

    gBackground.addActor(gScore);

  }

  private void initBg() {

    Image bg = GUI.createImage(GMain.bgAtlas, "bg");
    bg.setSize(CENTER_X*2, CENTER_Y*2);
    bg.setOrigin(Align.center);
    gBackground.addActor(bg);

    //label: flare item skill
    gFlareSkillItem = new Group();
    flare = GUI.createImage(GMain.itemAtlas, "flare_skill");
    gFlareSkillItem.setSize(flare.getWidth(), flare.getHeight());
    flare.setOrigin(Align.center);
    gFlareSkillItem.setPosition(0, -10);
    gFlareSkillItem.setVisible(false);
    gFlareSkillItem.addActor(flare);
    gItem.addActor(gFlareSkillItem);

    lbTutorialSkill = new Label(locale.get("tutorial_skill"), new Label.LabelStyle(Config.whiteFont, null));
    lbTutorialSkill.setFontScale(.7f);
    lbTutorialSkill.setPosition(20, flare.getY() + flare.getHeight() - 70);
    gFlareSkillItem.addActor(lbTutorialSkill);

    bgTable = GUI.createImage(GMain.bgAtlas, "bg_table");
    bgTable.setPosition(CENTER_X, CENTER_Y + Config.OFFSET_Y_BG_TABLE, Align.center);
    gItem.addActor(bgTable);

    //label: skill item
    Image bgSkill   = GUI.createImage(GMain.bgAtlas, "bg_skill");
    bgSkill.setPosition(bgTable.getX(), bgTable.getY() - bgSkill.getHeight() - 10);
    gBackground.addActor(bgSkill);

    //label: skill star
    gItemStar   = new Group();
    Image iStar = GUI.createImage(GMain.itemAtlas, "item_star");
    gItemStar.setSize(iStar.getWidth(), iStar.getHeight());
    gItemStar.setOrigin(Align.center);
    gItemStar.setPosition(bgSkill.getX() + bgSkill.getWidth()/2 - gItemStar.getWidth() - 20,
            bgSkill.getY() + bgSkill.getHeight()/2 - gItemStar.getHeight()/2 - 5);
    gItemStar.addActor(iStar);
    gItem.addActor(gItemStar);

    lbAmountItemStar = new Label(controller.amountItemStar+"", new Label.LabelStyle(Config.greenFont, null));
    lbAmountItemStar.setFontScale(.6f);
    lbAmountItemStar.setPosition(iStar.getX() + iStar.getWidth()/2 - 15, iStar.getY() + iStar.getHeight()/2);
    gItemStar.addActor(lbAmountItemStar);

    itemStar = GUI.createImage(GMain.itemAtlas, "item_star");
    itemStar.setOrigin(Align.topLeft);
    itemStar.setPosition(gItemStar.getX(), gItemStar.getY());

    //label: skill boom
    gItemBoom   = new Group();
    Image iBoom = GUI.createImage(GMain.itemAtlas, "item_boom");
    gItemBoom.setSize(iBoom.getWidth(), iBoom.getHeight());
    gItemBoom.setOrigin(Align.center);
    gItemBoom.setPosition(bgSkill.getX() + bgSkill.getWidth()/2 + 15,
            bgSkill.getY() + bgSkill.getHeight()/2 - gItemBoom.getHeight()/2 - 5);
    gItemBoom.addActor(iBoom);
    gItem.addActor(gItemBoom);

    lbAmountBoom = new Label(controller.amountItemBoom+"", new Label.LabelStyle(Config.greenFont, null));
    lbAmountBoom.setFontScale(.6f);
    lbAmountBoom.setPosition(iBoom.getX() + iBoom.getWidth()/2 + 10, iBoom.getY() + iBoom.getHeight()/2);
    gItemBoom.addActor(lbAmountBoom);

    itemBoom = GUI.createImage(GMain.itemAtlas, "item_boom");
    itemBoom.setOrigin(Align.center);
    itemBoom.setPosition(gItemBoom.getX(), gItemBoom.getY());

    //label: lbRound
    lbRound = new Label(locale.get("txt_round"), new Label.LabelStyle(Config.whiteFont, null));
    lbRound.setFontScale(1.2f);
    lbRound.setPosition(bgSkill.getX() + bgSkill.getWidth()/2 - lbRound.getWidth()/2 - 40,
                        bgSkill.getY() - lbRound.getHeight() - 30);
    gBackground.addActor(lbRound);

    //label: event click
    gItemBoom.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);

        if (controller.amountItemBoom == 0) {
          controller.unlockClick = true;

          resetSkillItem();
          setTxtAndIconPopupAdsSkillWhenShow(false);
          showPopupSkill();
        }
        else {
          if (!isChooseBoom) {
            isChooseBoom = true;
            isChooseStar = false;
            controller.unlockClick = true;

            gFlareSkillItem.setVisible(true);
            flare.clearActions();
            animFlareSkillItem();
            gAnimSkill.addActor(itemBoom);
            itemBoom.setPosition(gFlareSkillItem.getX() + gFlareSkillItem.getWidth()/2 - itemBoom.getWidth()/2,
                                 gFlareSkillItem.getY() + gFlareSkillItem.getHeight()/2 - itemBoom.getHeight()/2);

            itemStar.remove();
            gBackground.addActor(black);
            gItemStar.setScale(1f);
            gItemBoom.setScale(1.1f);
            controller.startAnimZoomAndVibrateOnBoard();
          }
          else {
            isChooseBoom = false;
            controller.unlockClick = false;

            black.remove();
            gFlareSkillItem.setVisible(false);
            itemBoom.remove();
            gItemBoom.setScale(1f);
            controller.stopAnimZoomAndVibrateOnBoard();
          }
        }

      }
    });

    gItemStar.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);

        if (controller.amountItemStar == 0) {
          //todo: show ads
          controller.unlockClick = true;

          resetSkillItem();
          setTxtAndIconPopupAdsSkillWhenShow(true);
          showPopupSkill();
        }
        else {
          if (!isChooseStar) {
            isChooseStar = true;
            isChooseBoom = false;
            controller.unlockClick = true;

            gFlareSkillItem.setVisible(true);
            flare.clearActions();
            animFlareSkillItem();
            gAnimSkill.addActor(itemStar);
            itemStar.setPosition(gFlareSkillItem.getX() + gFlareSkillItem.getWidth()/2 - itemStar.getWidth()/2,
                                 gFlareSkillItem.getY() + gFlareSkillItem.getHeight()/2 - itemStar.getHeight()/2);

            itemBoom.remove();
            gBackground.addActor(black);
            gItemStar.setScale(1.1f);
            gItemBoom.setScale(1f);
            controller.startAnimZoomAndVibrateOnBoard();
          }
          else {
            isChooseStar = false;
            controller.unlockClick = false;

            black.remove();
            itemStar.remove();
            gFlareSkillItem.setVisible(false);
            gItemStar.setScale(1f);
            controller.stopAnimZoomAndVibrateOnBoard();
          }
        }

      }
    });

  }

  private void initIcon() {
    iPause = GUI.createImage(GMain.bgAtlas, "icon_pause");
    iPause.setScale(.8f);
    iPause.setOrigin(Align.center);
    iPause.setPosition(CENTER_X*2 - iPause.getWidth() - 10, gScore.getY() - iPause.getHeight() - 50);
    gBackground.addActor(iPause);

    //label: event click
    imgClick(iPause, () -> controller.showPauseUI());
  }

  private void initPopupAdsTime() {
    gPopupAdsTime = new Group();
    Image bg      = GUI.createImage(GMain.popupAtlas, "pop_ads_time");
    gPopupAdsTime.setSize(bg.getWidth(), bg.getHeight());
    gPopupAdsTime.setOrigin(Align.center);
    gPopupAdsTime.setPosition(CENTER_X - gPopupAdsTime.getWidth()/2, CENTER_Y - gPopupAdsTime.getHeight()/2);
    gPopupAdsTime.addActor(bg);

    Label lbAdsTime = new Label(locale.get("quote_ads_time"), new Label.LabelStyle(Config.whiteFont, null));
    lbAdsTime.setAlignment(Align.center);
    lbAdsTime.setFontScale(1.1f, 1.2f);
    lbAdsTime.setPosition(bg.getX() + bg.getWidth()/2 - lbAdsTime.getWidth()/2 + 10,
                          bg.getY() + bg.getHeight()/2 - 100);
    gPopupAdsTime.addActor(lbAdsTime);

    Button btnOkAdsTime = new Button(GMain.popupAtlas, "btn_ok_ads_time",
                                     locale.get("txt_ads_time"), Config.greenFont);
    btnOkAdsTime.setPosition(bg.getX() + bg.getWidth()/2 - btnOkAdsTime.getWidth()/2,
                             bg.getY() + bg.getHeight()/2 + btnOkAdsTime.getHeight()*2);
    btnOkAdsTime.movebyLb(10, -12);
    btnOkAdsTime.setScale(1.25f);
    btnOkAdsTime.setFontScale(.7f, .7f);
    gPopupAdsTime.addActor(btnOkAdsTime);

    Image btnX = GUI.createImage(GMain.popupAtlas, "btn_x_popup");
    btnX.setScale(.9f);
    btnX.setOrigin(Align.center);
    btnX.setScale(.8f);
    btnX.setPosition(bg.getX() + bg.getWidth() - btnX.getWidth()/2 - 60,bg.getX() - btnX.getHeight()/2 + 70);
    gPopupAdsTime.addActor(btnX);

    gPopupAdsTime.setScale(0f);

    //label: even click
    btnClick(btnOkAdsTime, () -> {
      //todo: get free 30s when ads success
      if (GMain.platform.isVideoRewardReady()) {
        GMain.platform.ShowVideoReward((success -> {
          if (success) {
            hidePopupAdsTime();
            controller.continueWhenWatchAdsGetTime();
          }
        }));
      }
    });

    btnX.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);
        hidePopupAdsTime();
        showPopupGameOver();
      }
    });
  }

  private void initPopupAdsSkill() {
    gPopupAdsSkill = new Group();
    Image bg = GUI.createImage(GMain.popupAtlas, "pop_ads_time");
    gPopupAdsSkill.setSize(bg.getWidth(), bg.getHeight());
    gPopupAdsSkill.setOrigin(Align.center);
    gPopupAdsSkill.setPosition(CENTER_X, CENTER_Y, Align.center);
    gPopupAdsSkill.addActor(bg);

    lbAdsSkill = new Label(locale.format("ads_reward_skill", Config.ADS_SKILL_STAR),
                           new Label.LabelStyle(Config.whiteFont, null));
    lbAdsSkill.setAlignment(Align.center);
    lbAdsSkill.setPosition(bg.getX() + bg.getWidth()/2,
                           bg.getY() + bg.getHeight()/2 - 120,  Align.center);
    gPopupAdsSkill.addActor(lbAdsSkill);

    imgPopupAdsSkillStar = GUI.createImage(GMain.itemAtlas, "item_star");
    imgPopupAdsSkillStar.setPosition(bg.getX() + bg.getWidth()/2,
                                     bg.getY() + bg.getHeight()/2 + 50, Align.center);
    gPopupAdsSkill.addActor(imgPopupAdsSkillStar);

    imgPopupAdsSkillBoom = GUI.createImage(GMain.itemAtlas, "item_boom");
    imgPopupAdsSkillBoom.setPosition(bg.getX() + bg.getWidth()/2,
                                     bg.getY() + bg.getHeight()/2 + 50, Align.center);
    imgPopupAdsSkillBoom.setVisible(false);
    gPopupAdsSkill.addActor(imgPopupAdsSkillBoom);

    btnOKPopupAdsSkill = new Button(GMain.popupAtlas, "btn_ok_ads_time", locale.get("txt_ads_time"), Config.greenFont);
    btnOKPopupAdsSkill.setFontScale(.8f);
    btnOKPopupAdsSkill.movebyLb(10, -15);
    btnOKPopupAdsSkill.setPosition(bg.getX() + bg.getWidth()/2 - btnOKPopupAdsSkill.getWidth()/2,
                                   bg.getY() + bg.getHeight() - btnOKPopupAdsSkill.getHeight() - 70);
    gPopupAdsSkill.addActor(btnOKPopupAdsSkill);

    imgBtnXPopupSkill = GUI.createImage(GMain.popupAtlas, "btn_x_popup");
    imgBtnXPopupSkill.setScale(.8f);
    imgBtnXPopupSkill.setPosition(bg.getX() + bg.getWidth() - 110, -imgBtnXPopupSkill.getHeight()/2 + 70);
    gPopupAdsSkill.addActor(imgBtnXPopupSkill);

    gPopupAdsSkill.getColor().a = 0f;
    gPopupAdsSkill.setScale(0f);

    //label: even click
    btnClick(btnOKPopupAdsSkill, () -> {
      //todo: if ads success
      if (imgPopupAdsSkillStar.isVisible()) {
//        System.out.println("STAR");
        if (GMain.platform.isVideoRewardReady())
          GMain.platform.ShowVideoReward((success -> {
            if (success) {
              controller.amountItemStar = Config.ADS_SKILL_STAR;
              Util.inst().saveData(controller.amountItemStar, "amount_item_star");
              updateAmountSkill();
              hidePopupSkill();
              setTouchableItemSkill(Touchable.enabled);
            }
          }));
      }
      else {
//        System.out.println("BOOM");
        if (GMain.platform.isVideoRewardReady())
          GMain.platform.ShowVideoReward((success -> {
            if (success) {
              controller.amountItemBoom = Config.ADS_SKILL_BOOM;
              Util.inst().saveData(controller.amountItemBoom, "amount_item_boom");
              updateAmountSkill();
              hidePopupSkill();
              setTouchableItemSkill(Touchable.enabled);
            }
          }));
      }
    });

    imgBtnXPopupSkill.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);

        imgBtnXPopupSkill.setTouchable(Touchable.disabled);
        hidePopupSkill();

      }
    });

  }

  private void initPopupGameOver() {
    gPopupGameOver = new Group();
    Image bg       = GUI.createImage(GMain.popupAtlas, "popup_game_over");
    gPopupGameOver.setSize(bg.getWidth(), bg.getHeight());
    gPopupGameOver.setOrigin(Align.center);
    gPopupGameOver.setPosition(CENTER_X - gPopupGameOver.getWidth()/2, CENTER_Y - gPopupGameOver.getHeight()/2);
    gPopupGameOver.addActor(bg);

//    Label lbGameOver = new Label(locale.get("game_over"), new Label.LabelStyle(Config.brownFont, null));
//    lbGameOver.setAlignment(Align.center);
//    lbGameOver.setPosition(bg.getX() + bg.getWidth()/2 - lbGameOver.getWidth()/2,
//                           bg.getY() + 80);
//    gPopupGameOver.addActor(lbGameOver);

    Image ribbonGameOver = GUI.createImage(GMain.popupAtlas, "ribbon_game_over");
    ribbonGameOver.setPosition(bg.getX() + bg.getWidth()/2 - ribbonGameOver.getWidth()/2,
                                  bg.getY() + 40);
    gPopupGameOver.addActor(ribbonGameOver);

    Label lbRound = new Label(locale.get("txt_round"), new Label.LabelStyle(Config.greenFont, null));
    lbRound.setFontScale(.6f);
    lbRound.setPosition(bg.getX() + 80, bg.getY() + 220);
    gPopupGameOver.addActor(lbRound);

    lbRoundGameOver = new Label(controller.round+"", new Label.LabelStyle(Config.greenFont, null));
    lbRoundGameOver.setFontScale(.6f);
    lbRoundGameOver.setAlignment(Align.right);
    lbRoundGameOver.setPosition(lbRound.getX() + bg.getWidth() - 160 - lbRoundGameOver.getWidth(), lbRound.getY());
    gPopupGameOver.addActor(lbRoundGameOver);

    Label lbScore = new Label(locale.get("txt_score") + ":", new Label.LabelStyle(Config.greenFont, null));
    lbScore.setFontScale(.6f);
    lbScore.setPosition(lbRound.getX(), lbRound.getY() + 100);
    gPopupGameOver.addActor(lbScore);

    lbScoreGameOver = new Label("15000000", new Label.LabelStyle(Config.greenFont, null));
    lbScoreGameOver.setFontScale(.6f);
    lbScoreGameOver.setAlignment(Align.right);
    lbScoreGameOver.setPosition(lbScore.getX() + bg.getWidth() - 160 - lbScoreGameOver.getWidth(), lbScore.getY());
    gPopupGameOver.addActor(lbScoreGameOver);

    Label lbGoal = new Label(locale.get("txt_goal") + ":", new Label.LabelStyle(Config.greenFont, null));
    lbGoal.setFontScale(.6f);
    lbGoal.setPosition(lbScore.getX(), lbScore.getY() + 100);
    gPopupGameOver.addActor(lbGoal);

    lbGoalGameOver = new Label("15000000", new Label.LabelStyle(Config.greenFont, null));
    lbGoalGameOver.setFontScale(.6f);
    lbGoalGameOver.setAlignment(Align.right);
    lbGoalGameOver.setPosition(lbGoal.getX() + bg.getWidth() - 160 - lbGoalGameOver.getWidth(), lbGoal.getY());
    gPopupGameOver.addActor(lbGoalGameOver);

    Image btnRestart = GUI.createImage(GMain.bgAtlas, "icon_restart");
    btnRestart.setScale(.9f);
    btnRestart.setOrigin(Align.center);
    btnRestart.setPosition(bg.getX() + bg.getWidth()/2 - btnRestart.getWidth()/2 - 110,
            bg.getY() + bg.getHeight() - btnRestart.getHeight() - 20);
    gPopupGameOver.addActor(btnRestart);

    Image btnHome = GUI.createImage(GMain.bgAtlas, "icon_home");
    btnHome.setScale(.9f);
    btnHome.setOrigin(Align.center);
    btnHome.setPosition(bg.getX() + bg.getWidth()/2 - btnHome.getWidth()/2 + 110, btnRestart.getY());
    gPopupGameOver.addActor(btnHome);

    //label: event click
    imgClick(btnRestart, () -> {
      //todo: restart game
      GMain.platform.ShowFullscreen();

      hidePopupGameOver();
      controller.newGame();
    });

    imgClick(btnHome, () -> {
      GMain.platform.ShowFullscreen();

      controller.scene.setScreen(GMain.inst.startScene);
      controller.blackScreen.remove();
      controller.isPause = false;
      btnHome.setTouchable(Touchable.enabled);
      hidePopupGameOver();
      gPopupGameOver.remove();
    });

    gPopupGameOver.moveBy(0, 600);
    gPopupGameOver.getColor().a = 0f;
  }

  public void updateScore(long score) {
    lbScore.setText(score+"");
  }

  public void updateGoal(long target) {
    lbGoal.setText(target+"");
  }

  public void updateRound(int round) {
    String s = locale.get("txt_round");
    lbRound.setText(s + " " + round);
    animLbRound.setText(locale.get("round") + " " + round);
  }

  public void setTimeLineForNewRound(int timeOut) {
    int minute = timeOut %3600/60;
    int second = timeOut %60;

    String time = minute + ":" + second;
    lbTime.setText(time);
    timeLine.clipBy(0f, 0f);
  }

  public void addTimeLine(float time) {
    timeLine.clipBy(-time, 0f);
  }

  //--------------------------------------show/hide popup-------------------------------------------
  public void showPopupAdsTime() {
    gPopup.addActor(controller.blackScreen);
    gPopup.addActor(gPopupAdsTime);
    gPopupAdsTime.addAction(
            scaleTo(1f, 1f, .25f, swingOut)
    );
  }

  private void hidePopupAdsTime() {
    controller.blackScreen.remove();
    gPopupAdsTime.addAction(
            sequence(
                    parallel(
                            scaleTo(0f, 0f, .5f, linear),
                            alpha(0f, .25f, linear)
                    ),
                    run(() -> {
                      gPopupAdsTime.getColor().a = 1f;
                      gPopupAdsTime.remove();
                    })
            )
    );
  }

  public void showPopupGameOver() {
    SoundEffects.start("lose", Config.LOSE_VOLUME);

    lbRoundGameOver.setText(controller.round+"");
    lbScoreGameOver.setText(controller.scorePre+"");
    lbGoalGameOver.setText(controller.target+"");

    gPopup.addActor(controller.blackScreen);
    gPopup.addActor(gPopupGameOver);
    gPopupGameOver.addAction(
            parallel(
                    alpha(1f, .45f, linear),
                    Actions.moveBy(0, -600, .35f, fastSlow)
            )
    );
  }

  private void hidePopupGameOver() {
    controller.blackScreen.remove();
    gPopupGameOver.getColor().a = 0f;
    gPopupGameOver.moveBy(0, 600);
    gPopupGameOver.remove();
  }

  private void showPopupSkill() {
    gPopupAdsSkill.clearActions();
    gPopupAdsSkill.getColor().a = 0f;
    gPopupAdsSkill.setScale(0f);
    gPopupAdsSkill.remove();

    gPopup.addActor(black);
    gPopup.addActor(gPopupAdsSkill);
    gPopupAdsSkill.addAction(
            sequence(
                    parallel(
                            scaleTo(1f, 1f, .25f, fastSlow),
                            alpha(1f, .35f, linear)
                    ),
                    run(() -> {
                      btnOKPopupAdsSkill.setTouchable(Touchable.enabled);
                      imgBtnXPopupSkill.setTouchable(Touchable.enabled);
                    })
            )
    );
  }

  public void hidePopupSkill() {
    controller.unlockClick = false;
    black.remove();
    gPopupAdsSkill.addAction(
            sequence(
                    parallel(
                            scaleTo(0f, 0f, .5f, fastSlow),
                            alpha(0f, .25f, linear)
                    ),
                    run(() -> gPopupAdsSkill.remove())
            )
    );
  }
  //--------------------------------------show/hide popup-------------------------------------------

  //--------------------------------------animation game play---------------------------------------

  public void animComplete(Runnable onNextLv) {
    gAnimLb.addActor(gLbComplete);
    float x = gLbComplete.getX() + gLbComplete.getWidth()/2;
    float y = gLbComplete.getY() + gLbComplete.getHeight()/2;

    SoundEffects.start("win", Config.WIN_VOLUME);
    pComplete.start(x, y, 2f);

    gLbComplete.addAction(
            sequence(
                    parallel(
                            alpha(1f, .5f, linear),
                            scaleTo(.35f, .35f, .65f, swingOut)
                    ),
                    delay(1.5f),
                    alpha(0f, .5f, linear),
                    delay(.5f),
                    run(() -> {
                      resetLbComplete();
                      animLbRound(onNextLv);
                    })
            )
    );
  }

  public void animLbRound(Runnable onNextLv) {
    gAnimLb.addActor(black);
    gAnimLb.addActor(gAnimLbRound);
    gAnimLbRound.addAction(
            sequence(
                    delay(.75f),
                    run(() -> SoundEffects.start("line", 1f)),
                    moveTo(CENTER_X - gAnimLbRound.getWidth()/2,
                           CENTER_Y - gAnimLbRound.getHeight()/2, .5f, fastSlow),
                    delay(1.25f),
                    run(() -> {
                      gAnimLbRound.getColor().a = 0f;
                      pNewRound.start(CENTER_X, CENTER_Y, 1.5f);
                      SoundEffects.start("laser_off", 1f);
                    }),
                    delay(.75f),
                    run(() -> black.remove()),
                    run(onNextLv),
                    run(this::resetAnimLbRound)
            )
    );
  }

  public void showPWonder(int idWonder) { //idWonder => sprite tương ứng với mỗi điểm mỗi khi ăn được
    switch (idWonder) {
      case 0: SoundEffects.start("fantastic", Config.FANTASTIC_VOLUME); break;
      case 1: SoundEffects.start("awesome", Config.AWESOME_VOLUME); break;
      case 2: SoundEffects.start("amazing", Config.AMAZING_VOLUME); break;
    }

    pWonder.changeSprite(idWonder);
    pWonder.start(CENTER_X, CENTER_Y, 2.5f);
    pWind.start(CENTER_X, CENTER_Y - 300, 1f);
  }

  public void showLovely() {
    SoundEffects.start("lovely", Config.LOVELY_VOLUME);
    pLovely.start(CENTER_X, CENTER_Y, 2.5f);
  }

  public void animItemBoom(Vector2 pos, Runnable onComplete) {
    gFlareSkillItem.setVisible(false);
    black.remove();
    gAnimSkill.addActor(itemBoom);
    itemBoom.setPosition(pos.x + Config.WIDTH_PIECE/2 - itemBoom.getWidth()/2,
                         pos.y + Config.HEIGHT_PIECE/2 - itemBoom.getHeight()/2);
    itemBoom.addAction(
            sequence(
                    parallel(
                            sequence(
                                    Actions.rotateBy(10, .075f, fastSlow),
                                    Actions.rotateBy(-20, .075f, fastSlow),
                                    Actions.rotateBy(20, .075f, fastSlow),
                                    Actions.rotateBy(-20, .075f, fastSlow),
                                    Actions.rotateBy(20, .075f, fastSlow),
                                    Actions.rotateBy(-20, .075f, fastSlow),
                                    Actions.rotateBy(20, .075f, fastSlow),
                                    Actions.rotateBy(-20, .075f, fastSlow),
                                    Actions.rotateTo(0, .075f, fastSlow)
                            ),
                            scaleTo(2.5f, 2.5f, .65f, linear),
                            alpha(0f, .85f, linear)
                    ),
                    run(onComplete),
                    run(() -> {
                      itemBoom.setScale(1f);
                      itemBoom.getColor().a = 1f;
                      itemBoom.remove();
                    })
            )
    );
  }

  public void animItemStar(Vector2 pos, Runnable onComplete) {
    gFlareSkillItem.setVisible(false);
    black.remove();
    gAnimSkill.addActor(itemStar);
    itemStar.setPosition(pos.x - 50, pos.y - 30);
    itemStar.addAction(
            sequence(
                    parallel(
                            Actions.rotateBy(-40, .5f, fastSlow),
                            scaleTo(1.75f, 1.75f, .5f, fastSlow)

                    ),
                    parallel(
                            Actions.rotateBy(60, .35f, swingOut),
                            scaleTo(1f, 1f, .25f, fastSlow),
                            alpha(0f, .5f, linear)

                    ),
                    run(() -> itemStar.setVisible(false)),
                    run(onComplete),
                    run(() -> {
                      itemStar.setVisible(true);
                      itemStar.setRotation(0);
                      itemStar.setScale(1f);
                      itemStar.getColor().a = 1f;
                      itemStar.remove();
                    })
            )
    );
  }

  private void animFlareSkillItem() {
    if (!gFlareSkillItem.isVisible())
      return;
    flare.addAction(
            sequence(
                    Actions.rotateBy(10, .25f, linear),
                    run(this::animFlareSkillItem)
            )
    );
  }

  //--------------------------------------animation game play---------------------------------------

  //--------------------------------------reset-----------------------------------------------------

  public void updateAmountSkill() {
    isChooseBoom           = false;
    isChooseStar           = false;

    gItemBoom.setScale(1f);
    gItemStar.setScale(1f);
    setTouchableItemSkill(Touchable.disabled);
    lbAmountBoom.setText(controller.amountItemBoom);
    lbAmountItemStar.setText(controller.amountItemStar);
  }

  private void resetLbComplete() {
    gLbComplete.getColor().a = 0f;
    gLbComplete.setScale(1f);
    gLbComplete.remove();
  }

  private void resetAnimLbRound() {
    gAnimLbRound.getColor().a = 1f;
    gAnimLbRound.setPosition(-gAnimLbRound.getWidth() - 10, CENTER_Y - gAnimLbRound.getHeight()/2);
    gAnimLbRound.remove();
  }

  public void clearActionAllObject() {
    lbScore.setText("0");
    gAnimLbRound.clearActions();
    resetAnimLbRound();

    gLbComplete.clearActions();
    resetLbComplete();

    pLovely.remove();
    pWonder.remove();
    gBackground.clearActions();
  }

  private void setTxtAndIconPopupAdsSkillWhenShow(boolean isStar) {
    if (isStar) {
      lbAdsSkill.setText(locale.format("ads_reward_skill", Config.ADS_SKILL_STAR));
      imgPopupAdsSkillStar.setVisible(true);
      imgPopupAdsSkillBoom.setVisible(false);
    }
    else {
      lbAdsSkill.setText(locale.format("ads_reward_skill", Config.ADS_SKILL_BOOM));
      imgPopupAdsSkillStar.setVisible(false);
      imgPopupAdsSkillBoom.setVisible(true);
    }
  }

  private void resetSkillItem() {
    isChooseStar      = false;
    isChooseBoom      = false;

    black.remove();
    gFlareSkillItem.setVisible(false);
    itemBoom.remove();
    itemStar.remove();
    gItemStar.setScale(1f);
    gItemBoom.setScale(1f);
    controller.stopAnimZoomAndVibrateOnBoard();
  }

  //--------------------------------------reset-----------------------------------------------------

  //--------------------------------------anim click------------------------------------------------
  private void imgClick(Image btn, Runnable onComplete) {

    btn.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);

        SoundEffects.start("click", Config.CLICK_VOLUME);

        btn.setTouchable(Touchable.disabled);
        animClick(btn, onComplete);
      }
    });

  }

  private void btnClick(Button btn, Runnable onComplete) {

    btn.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);
        btn.setTouchable(Touchable.disabled);
        animClick(btn, onComplete);
      }
    });

  }

  private void animClick(Image btn, Runnable onComplete) {
    btn.addAction(
            sequence(
                    Actions.scaleBy(.1f, .1f, .05f, fastSlow),
                    Actions.scaleBy(-.1f, -.1f, .05f, fastSlow),
                    run(onComplete),
                    run(() -> btn.setTouchable(Touchable.enabled))

            )
    );
  }

  private void animClick(Group btn, Runnable onComplete) {
    btn.addAction(
            sequence(
                    Actions.scaleBy(.1f, .1f, .05f, fastSlow),
                    Actions.scaleBy(-.1f, -.1f, .05f, fastSlow),
                    run(onComplete),
                    run(() -> btn.setTouchable(Touchable.enabled))

            )
    );
  }
  //--------------------------------------anim click------------------------------------------------

  public void setTouchableItemSkill(Touchable touchable) {
    gItemBoom.setTouchable(touchable);
    gItemStar.setTouchable(touchable);
  }

  @Override
  public void act(float delta) {
    if (!controller.isPause) {
      super.act(delta);

      if (!controller.isGameOver) {
        count += delta;
        if (count >= 1f) {
          controller.updateLbTimeLine();
          count = 0f;
        }
      }

    }
  }

}
