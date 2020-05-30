package com.ss.ui;

import static com.badlogic.gdx.math.Interpolation.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;
import com.ss.GMain;
import com.ss.config.C;
import com.ss.config.Config;
import com.ss.controller.GameUIController;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import com.ss.gameLogic.effects.Particle;
import com.ss.utils.Button;
import com.ss.utils.Clipping;

public class GamePlayUI extends Group {

  public final float CENTER_X = GStage.getWorldWidth()/2;
  public final float CENTER_Y = GStage.getWorldHeight()/2;

  private       GameUIController controller;
  private       I18NBundle       locale = C.lang.locale;

  public        Group            gBackground, gItem, gAnimLb, gAnimSkill, gPopup;
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

  private Particle  pWonder, pLovely;

  public GamePlayUI(GameUIController controller) {

    //label: init layer
    this.gBackground  = new Group();
    this.gItem        = new Group();
    this.gAnimSkill   = new Group();
    this.gAnimLb      = new Group();
    this.gPopup       = new Group();

    this.controller   = controller;

    this.pWonder      = new Particle(gAnimSkill, Config.WONDER, GMain.particleAtlas);
    this.pWonder.initLsEmitter();
    this.pLovely      = new Particle(gAnimSkill, Config.LOVELY, GMain.particleAtlas);

    this.addActor(gBackground);
    this.addActor(gItem);
    this.addActor(gAnimLb);
    this.addActor(gAnimSkill);
    this.addActor(gPopup);

    initBg();
    initTime();
    initScore();
    initIcon();
    initLbLvUp();
    initPopupAdsTime();
    initPopupGameOver();
    initAnimLbRound();

  }

  private void initAnimLbRound() {
    gAnimLbRound = new Group();
    Image bgRound = GUI.createImage(GMain.bgAtlas, "bg_round");
    gAnimLbRound.setSize(bgRound.getWidth(), bgRound.getHeight());
//    gAnimLbRound.setPosition(CENTER_X - gAnimLbRound.getWidth()/2, -gAnimLbRound.getHeight() - 10);
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

    bgTable = GUI.createImage(GMain.bgAtlas, "bg_table");
    bgTable.setPosition(CENTER_X, CENTER_Y + Config.OFFSET_Y_BG_TABLE, Align.center);
    gBackground.addActor(bgTable);

    lbRound = new Label(locale.get("txt_round"), new Label.LabelStyle(Config.whiteFont, null));
    lbRound.setFontScale(1.2f);
    lbRound.setPosition(bgTable.getX(), bgTable.getY() - lbRound.getHeight() - 48);
    gBackground.addActor(lbRound);

  }

  private void initIcon() {
    iPause = GUI.createImage(GMain.bgAtlas, "icon_pause");
    iPause.setOrigin(Align.center);
    iPause.setPosition(lbRound.getX() + lbRound.getWidth()/2 - iPause.getWidth()/2 + 20,
                       lbRound.getY() - iPause.getHeight() * 1.25f);
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
    lbAdsTime.setPosition(bg.getX() + bg.getWidth()/2 - lbAdsTime.getWidth()/2,
                          bg.getY() + bg.getHeight()/2 - 100);
    gPopupAdsTime.addActor(lbAdsTime);

    Button btnOkAdsTime = new Button(GMain.popupAtlas, "btn_ok_ads_time",
                                     locale.get("txt_ads_time"), Config.greenFont);
    btnOkAdsTime.setPosition(bg.getX() + bg.getWidth()/2 - btnOkAdsTime.getWidth()/2,
                             bg.getY() + bg.getHeight()/2 + btnOkAdsTime.getHeight()*2);
    btnOkAdsTime.movebyLb(0, -15);
    btnOkAdsTime.setFontScale(.85f, .85f);
    gPopupAdsTime.addActor(btnOkAdsTime);

    Image btnX = GUI.createImage(GMain.bgAtlas, "icon_exit");
    btnX.setScale(.9f);
    btnX.setOrigin(Align.center);
    btnX.setPosition(bg.getX() + bg.getWidth() - btnX.getWidth()/2,bg.getX() - btnX.getHeight()/2);
    gPopupAdsTime.addActor(btnX);

    gPopupAdsTime.setScale(0f);

    //label: even click
    btnClick(btnOkAdsTime, () -> {
      //todo: get free 30s when ads success
      hidePopupAdsTime();
      controller.continueWhenWatchAdsGetTime();
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

  private void initPopupGameOver() {
    gPopupGameOver = new Group();
    Image bg       = GUI.createImage(GMain.popupAtlas, "popup_game_over");
    gPopupGameOver.setSize(bg.getWidth(), bg.getHeight());
    gPopupGameOver.setOrigin(Align.center);
    gPopupGameOver.setPosition(CENTER_X - gPopupGameOver.getWidth()/2, CENTER_Y - gPopupGameOver.getHeight()/2);
    gPopupGameOver.addActor(bg);

    Label lbGameOver = new Label(locale.get("game_over"), new Label.LabelStyle(Config.brownFont, null));
    lbGameOver.setAlignment(Align.center);
    lbGameOver.setPosition(bg.getX() + bg.getWidth()/2 - lbGameOver.getWidth()/2,
                           bg.getY() + 80);
    gPopupGameOver.addActor(lbGameOver);

    Label lbRound = new Label(locale.get("txt_round"), new Label.LabelStyle(Config.greenFont, null));
    lbRound.setFontScale(.6f);
    lbRound.setPosition(bg.getX() + 60, bg.getY() + 270);
    gPopupGameOver.addActor(lbRound);

    lbRoundGameOver = new Label(controller.round+"", new Label.LabelStyle(Config.greenFont, null));
    lbRoundGameOver.setFontScale(.6f);
    lbRoundGameOver.setAlignment(Align.right);
    lbRoundGameOver.setPosition(lbRound.getX() + bg.getWidth() - 120 - lbRoundGameOver.getWidth(), lbRound.getY());
    gPopupGameOver.addActor(lbRoundGameOver);

    Label lbScore = new Label(locale.get("txt_score") + ":", new Label.LabelStyle(Config.greenFont, null));
    lbScore.setFontScale(.6f);
    lbScore.setPosition(lbRound.getX(), lbRound.getY() + 80);
    gPopupGameOver.addActor(lbScore);

    lbScoreGameOver = new Label("15000000", new Label.LabelStyle(Config.greenFont, null));
    lbScoreGameOver.setFontScale(.6f);
    lbScoreGameOver.setAlignment(Align.right);
    lbScoreGameOver.setPosition(lbScore.getX() + bg.getWidth() - 120 - lbScoreGameOver.getWidth(), lbScore.getY());
    gPopupGameOver.addActor(lbScoreGameOver);

    Label lbGoal = new Label(locale.get("txt_goal") + ":", new Label.LabelStyle(Config.greenFont, null));
    lbGoal.setFontScale(.6f);
    lbGoal.setPosition(lbScore.getX(), lbScore.getY() + 80);
    gPopupGameOver.addActor(lbGoal);

    lbGoalGameOver = new Label("15000000", new Label.LabelStyle(Config.greenFont, null));
    lbGoalGameOver.setFontScale(.6f);
    lbGoalGameOver.setAlignment(Align.right);
    lbGoalGameOver.setPosition(lbGoal.getX() + bg.getWidth() - 120 - lbGoalGameOver.getWidth(), lbGoal.getY());
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
      hidePopupGameOver();
      controller.newGame();
    });

    imgClick(btnHome, () -> {
      //todo: setScreen homeScreen
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
  //--------------------------------------show/hide popup-------------------------------------------

  //--------------------------------------animation game play---------------------------------------

  public void animComplete(Runnable onNextLv) {
    gAnimLb.addActor(gLbComplete);
    float x = gLbComplete.getX() + gLbComplete.getWidth()/2;
    float y = gLbComplete.getY() + gLbComplete.getHeight()/2;
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
    gAnimLb.addActor(gAnimLbRound);
    gAnimLbRound.addAction(
            sequence(
                    moveTo(CENTER_X - gAnimLbRound.getWidth()/2,
                           CENTER_Y - gAnimLbRound.getHeight()/2, .5f, fastSlow),
                    delay(1f),
                    moveTo(CENTER_X*2 + gAnimLbRound.getWidth()/2 + 10,
                           CENTER_Y - gAnimLbRound.getHeight()/2, .35f, swingIn),
                    run(onNextLv),
                    run(this::resetAnimLbRound)
            )
    );
  }

  public void showPWonder(int idWonder) { //idWonder => sprite tương ứng với mỗi điểm mỗi khi ăn được
    pWonder.changeSprite(idWonder);
    pWonder.start(CENTER_X, CENTER_Y, 2.5f);
  }

  public void showLovely() {
    pLovely.start(CENTER_X, CENTER_Y, 2.5f);
  }

  //--------------------------------------animation game play---------------------------------------

  //--------------------------------------reset-----------------------------------------------------

  private void resetLbComplete() {
    gLbComplete.getColor().a = 0f;
    gLbComplete.setScale(1f);
    gLbComplete.remove();
  }

  private void resetAnimLbRound() {
    gAnimLbRound.setPosition(-gAnimLbRound.getWidth()/2 - 10, CENTER_Y - gAnimLbRound.getHeight()/2);
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

  //--------------------------------------reset-----------------------------------------------------

  //--------------------------------------anim click------------------------------------------------
  private void imgClick(Image btn, Runnable onComplete) {

    btn.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);
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
