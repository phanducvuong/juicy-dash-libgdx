package com.ss.ui;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.ss.GMain;
import com.ss.config.Config;
import com.ss.controller.GameUIController;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import com.ss.objects.Item;

public class GamePlayUI extends Group {

  private final float CENTER_X = GStage.getWorldWidth()/2;
  private final float CENTER_Y = GStage.getWorldHeight()/2;

  private GameUIController controller;
  public Group gBackground, gItem;
  public Image bgTable;

  public Image iStart;

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

      }
    });

  }

  public void addToGItem(Item item) {
    gItem.addActor(item);
  }

}
