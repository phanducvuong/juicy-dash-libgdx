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
  public        Group            gBackground, gItem, gAnimLb, gAnimSkill, gPopup;
  public static Image            bgTable;
  private       Label            lbRound;

  public        Image iStart, iPause;

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

  private       Group     gPopupAdsTime;

  private Particle  pComplete;
  private Group     gLbComplete;
  private Label     lbComplete;
  private Label     lbTimeIsUp;

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

    setWidth(CENTER_X*2);
    setHeight(CENTER_Y*2);

    initBg();
    initTime();
    initScore();
    initIcon();
    initLbLvUp();
    initPopupAdsTime();
    initAnimLbRound();

  }

  private void initAnimLbRound() {
    gAnimLbRound = new Group();
    Image bgRound = GUI.createImage(GMain.bgAtlas, "bg_round");
    gAnimLbRound.setSize(bgRound.getWidth(), bgRound.getHeight());
//    gAnimLbRound.setPosition(CENTER_X - gAnimLbRound.getWidth()/2, -gAnimLbRound.getHeight() - 10);
    gAnimLbRound.setPosition(-gAnimLbRound.getWidth() - 10, CENTER_Y - gAnimLbRound.getHeight()/2);
    gAnimLbRound.addActor(bgRound);

    animLbRound = new Label(C.lang.locale.get("round") + " 3", new Label.LabelStyle(Config.whiteFont, null));
    animLbRound.setAlignment(Align.center);
    animLbRound.setFontScale(1.7f);
    animLbRound.setPosition(bgRound.getX() + bgRound.getWidth()/2 - animLbRound.getWidth()/2,
            bgRound.getY() + bgRound.getHeight()/2 - animLbRound.getHeight()/2 - 10);
    gAnimLbRound.addActor(animLbRound);
  }

  private void initLbLvUp() {
    gLbComplete = new Group();
    lbComplete  = new Label(C.lang.locale.get("complete"), new Label.LabelStyle(Config.greenFont, null));
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
            bgTable.getY() - bgBar.getHeight() - 15);
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
    if (C.lang.locale.get("id_country").equals("vn"))
      clock.moveBy(-30, 0);
    gTime.addActor(clock);

    posCLock   = new Vector2();
    posCLock.x = gTime.getX() + clock.getX();
    posCLock.y = gTime.getY() - clock.getHeight()*clock.getScaleY();

    Label lbTxtTime = new Label(C.lang.locale.get("txt_time"), new Label.LabelStyle(Config.whiteFont, null));
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

    Label lbTxtScore = new Label(C.lang.locale.get("txt_score"),
            new Label.LabelStyle(Config.whiteFont, null));
    lbTxtScore.setPosition(bgScore.getX(), bgScore.getY() - lbTxtScore.getHeight() - 10);
    gScore.addActor(lbTxtScore);

    lbScore = new Label("0", new Label.LabelStyle(Config.whiteFont, null));
    lbScore.setAlignment(Align.left);
    lbScore.setPosition(bgScore.getX() + 5,
            bgScore.getY() + bgScore.getHeight()/2 - lbScore.getHeight()/2 - 8);
    gScore.addActor(lbScore);

    Label lbTxtGoal = new Label(C.lang.locale.get("txt_goal"),
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

    float height = 0;

    Image bgPrev = GUI.createImage(GMain.bgAtlas, "bg_piece");
    bgPrev.setWidth(CENTER_X*2);
    bgPrev.setPosition(0, 0);
    gBackground.addActor(bgPrev);

    while (true) {

      Image bg = GUI.createImage(GMain.bgAtlas, "bg_piece");
      bg.setWidth(CENTER_X*2);
      gBackground.addActor(bg);

      bg.setPosition(bgPrev.getX(), bgPrev.getY() + bgPrev.getHeight() - 1);
      bgPrev = bg;

      height += bg.getHeight();
      if (height > CENTER_Y*2 + 20)
        break;

    }

    bgTable = GUI.createImage(GMain.bgAtlas, "bg_table");
    bgTable.setPosition(CENTER_X, CENTER_Y + Config.OFFSET_Y_BGTABLE, Align.center);
    gBackground.addActor(bgTable);

    lbRound = new Label(C.lang.locale.get("txt_round"), new Label.LabelStyle(Config.whiteFont, null));
    lbRound.setFontScale(1.2f);
    lbRound.setPosition(bgTable.getX(), bgTable.getY() - lbRound.getHeight() - 35);
    gBackground.addActor(lbRound);

  }

  private void initIcon() {

    iStart = GUI.createImage(GMain.bgAtlas, "icon_start");
    gBackground.addActor(iStart);
    iStart.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);

//          System.out.println("CLICK!");
//          controller.filterAll();
//          controller.updateArrPiece();

//        controller.addTimeLine(15);
//        isPause = !isPause;
//
//        Pause pause = new Pause(new Color(128/255f, 213/255f, 181/255f, .3f));
//        GamePlayUI.this.addActor(pause);

      }
    });

    iPause = GUI.createImage(GMain.bgAtlas, "icon_pause");
    iPause.setPosition(bgTable.getX() + 10, bgTable.getY() - iPause.getHeight() - 80);
    gBackground.addActor(iPause);

  }

  private void initPopupAdsTime() {
    gPopupAdsTime = new Group();
    Image bg      = GUI.createImage(GMain.popupAtlas, "pop_ads_time");
    gPopupAdsTime.setSize(bg.getWidth(), bg.getHeight());
    gPopupAdsTime.setOrigin(Align.center);
    gPopupAdsTime.setPosition(CENTER_X - gPopupAdsTime.getWidth()/2, CENTER_Y - gPopupAdsTime.getHeight()/2);
    gPopupAdsTime.addActor(bg);

    Label lbAdsTime = new Label(C.lang.locale.get("quote_ads_time"), new Label.LabelStyle(Config.whiteFont, null));
    lbAdsTime.setAlignment(Align.center);
    lbAdsTime.setFontScale(1.1f, 1.2f);
    lbAdsTime.setPosition(bg.getX() + bg.getWidth()/2 - lbAdsTime.getWidth()/2,
                          bg.getY() + bg.getHeight()/2 - 100);
    gPopupAdsTime.addActor(lbAdsTime);

    Button btnOkAdsTime = new Button(GMain.popupAtlas, "btn_ok_ads_time",
                                     C.lang.locale.get("txt_ads_time"), Config.greenFont);
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
    });

    btnX.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);
        gPopupAdsTime.setScale(0);
        gPopupAdsTime.remove();
        showPopupGameOver();
      }
    });

    gPopup.addActor(gPopupAdsTime);
  }

  public void updateScore(long score) {
    lbScore.setText(score+"");
  }

  public void updateGoal(long target) {
    lbGoal.setText(target+"");
  }

  public void updateRound(int round) {
    String s = C.lang.locale.get("txt_round");
    lbRound.setText(s + " " + round);
    animLbRound.setText(C.lang.locale.get("round") + " " + round);
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
    gPopup.addActor(gPopupAdsTime);
    gPopupAdsTime.addAction(
            scaleTo(1f, 1f, .25f, bounceOut)
    );
  }

  private void showPopupGameOver() {

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
