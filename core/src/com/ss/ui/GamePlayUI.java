package com.ss.ui;

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

public class GamePlayUI extends Group {

  private final float CENTER_X = GStage.getWorldWidth()/2;
  private final float CENTER_Y = GStage.getWorldHeight()/2;

  private GameUIController controller;
  public Group gBackground, gItem;
  public Image bgTable;

  public Image iStart;

  private Group gTime;
  public Clipping timeLine;
  private Image bgBar;
  public Label lbTime;
  private float sclXTime = 1f;
  private float count = 0f;

  private Group gScore;
  private Image bgScore, scoreBar;
  private Label lbScore, lbGoal;

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
    initIcon();
    initTime();
    initScore();

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

    scoreBar = GUI.createImage(GMain.bgAtlas, "line_score");
    scoreBar.setPosition(bgScore.getX() + 6,bgScore.getY() + 4);
    gScore.addActor(scoreBar);

    Label lbTxtScore = new Label(C.lang.locale.get("txt_score"),
            new Label.LabelStyle(Config.whiteFont, null));
    lbTxtScore.setPosition(scoreBar.getX(), scoreBar.getY() - lbTxtScore.getHeight() - 15);
    gScore.addActor(lbTxtScore);

    lbScore = new Label("00092", new Label.LabelStyle(Config.whiteFont, null));
    lbScore.setAlignment(Align.left);
    lbScore.setPosition(scoreBar.getX(),
            scoreBar.getY() + scoreBar.getHeight()/2 - lbScore.getHeight()/2 - 5);
    gScore.addActor(lbScore);

    Label lbTxtGoal = new Label(C.lang.locale.get("txt_goal"),
            new Label.LabelStyle(Config.whiteFont, null));
    lbTxtGoal.setPosition(scoreBar.getX() + scoreBar.getWidth() - lbTxtGoal.getWidth(),
            scoreBar.getY() - lbTxtGoal.getHeight() - 15);
    gScore.addActor(lbTxtGoal);

    lbGoal = new Label("3000", new Label.LabelStyle(Config.whiteFont, null));
    lbGoal.setAlignment(Align.right);
    lbGoal.setPosition(scoreBar.getX() + scoreBar.getWidth() - lbGoal.getWidth(),
            scoreBar.getY() + scoreBar.getHeight()/2 - lbGoal.getHeight()/2 - 5);
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

  }

  private void initIcon() {

    iStart = GUI.createImage(GMain.bgAtlas, "icon_start");
    gBackground.addActor(iStart);
    iStart.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);

//          System.out.println("CLICK!");
          controller.filterAll();
          controller.updateArrPiece();

          sclXTime -= 0.01;
          timeLine.clip(sclXTime, 1f);

      }
    });

  }

  public void addToGItem(Item item) {
    gItem.addActor(item);
  }

  @Override
  public void act(float delta) {
    super.act(delta);

    count += delta;
    if (count >= 1f) {
      controller.updateTime();
      count = 0f;
    }

  }

}
