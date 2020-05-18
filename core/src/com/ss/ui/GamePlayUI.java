package com.ss.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
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
import com.ss.objects.Item;
import com.ss.utils.Clipping;
import com.ss.utils.Pause;

public class GamePlayUI extends Group {

  private final float CENTER_X = GStage.getWorldWidth()/2;
  private final float CENTER_Y = GStage.getWorldHeight()/2;

  private GameUIController controller;
  public Group gBackground, gItem;
  public Image bgTable;
  private Label lbRound;

  public Image iStart, iPause;
  public boolean isPause = false;

  private Group gTime;
  public Clipping timeLine;
  private Image bgBar;
  public Label lbTime;
  private float count = 0f;

  private Group gScore;
  private Image bgScore;
  private Label lbScore, lbGoal;
  public Clipping scoreLine;

  public GamePlayUI(GameUIController controller) {

    //label: init layer
    this.gBackground = new Group();
    this.gItem = new Group();

    this.controller = controller;

    this.addActor(gBackground);
    this.addActor(gItem);

    setWidth(CENTER_X*2);
    setHeight(CENTER_Y*2);

    initBg();
    initTime();
    initScore();
    initIcon();

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

    lbTime = new Label("2:30", new Label.LabelStyle(Config.whiteFont, null));
    lbTime.setAlignment(Align.center);
    lbTime.setPosition(bgBar.getX() + bgBar.getWidth()/2 - lbTime.getWidth()/2,
            bgBar.getY() + bgBar.getHeight()/2 - lbTime.getHeight()/2 - 8);
    gTime.addActor(lbTime);

    Image clock = GUI.createImage(GMain.itemAtlas, "item_clock");
    clock.setScale(.5f);
    clock.setPosition(bgBar.getX() + bgBar.getWidth()/2 - clock.getWidth()*clock.getScaleX() - 30,
            bgBar.getY() - clock.getHeight()*clock.getScaleY());
    if (C.lang.locale.get("id_country").equals("vn"))
      clock.moveBy(-30, 0);
    gTime.addActor(clock);

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
        isPause = !isPause;

        Pause pause = new Pause(new Color(128/255f, 213/255f, 181/255f, .3f));
        GamePlayUI.this.addActor(pause);

      }
    });

    iPause = GUI.createImage(GMain.bgAtlas, "icon_pause");
    iPause.setPosition(bgTable.getX() + 10, bgTable.getY() - iPause.getHeight() - 80);
    gBackground.addActor(iPause);

  }

  public void addToGItem(Item item) {
    gItem.addActor(item);
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
  }

  public void addTimeLine(float time) {
    timeLine.clipBy(-time, 0f);
  }

  @Override
  public void act(float delta) {
    super.act(delta);

    if (!isPause) {
      count += delta;
      if (count >= 1f) {
        controller.updateTime();
        count = 0f;
      }
    }

  }

}
