package com.ss.gameLogic.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Align;
import com.ss.GMain;
import com.ss.data.WheelData;
import com.ss.gameLogic.config.Config;
import com.ss.minigames.Wheel;

import java.util.List;

public class WheelMiniGame {

  private static WheelMiniGame instance;

  private WheelMiniGame(List<WheelData> data) {

    Wheel.wheelTex = GMain.wheelAtlas.findRegion("wheel");
    Wheel.wheelTick = Gdx.audio.newSound(Gdx.files.internal("sound/Wheel_sound.mp3"));//GMain.wheelAtlas.getSound("Wheel_sound");
    Wheel.pointer = GMain.wheelAtlas.findRegion("pointer");
    Wheel.wheelDot = GMain.wheelAtlas.findRegion("dot");
    Wheel.lightDot = GMain.wheelAtlas.findRegion("lightdot");
    Wheel.wheelText = Config.ALERT_FONT;
    Wheel.TEXT_SPACE = 5f;
    Wheel.PARTITION = 12;

    for (WheelData d : data)
      Wheel.wheelItems.add(Wheel.WheelItem.newInst(GMain.wheelAtlas.findRegion(d.getRegion()),
              d.getId(),
              d.getQty(),
              d.getQtyText(),
              d.getPercent())
      );

//    Wheel.wheelItems.add(Wheel.WheelItem.newInst(GMain.wheelAtlas.findRegion("SoVang"), 0, 2, "800", 800));
//    Wheel.wheelItems.add(Wheel.WheelItem.newInst(GMain.wheelAtlas.findRegion("buff_item_10000_dis"), 1, 2, "103", 103));
//    Wheel.wheelItems.add(Wheel.WheelItem.newInst(GMain.wheelAtlas.findRegion("buff_item_10001_dis"), 2, 2, "1054", 1054));
//    Wheel.wheelItems.add(Wheel.WheelItem.newInst(GMain.wheelAtlas.findRegion("SoXam"), 3, 2, "929", 929));
//    Wheel.wheelItems.add(Wheel.WheelItem.newInst(GMain.wheelAtlas.findRegion("buff_item_10001_dis"), 4, 2, "505", 505));
//    Wheel.wheelItems.add(Wheel.WheelItem.newInst(GMain.wheelAtlas.findRegion("buff_item_10001_dis"), 5, 2, "351", 351));
//    Wheel.wheelItems.add(Wheel.WheelItem.newInst(GMain.wheelAtlas.findRegion("SoVang"), 6, 2, "981", 981));
//    Wheel.wheelItems.add(Wheel.WheelItem.newInst(GMain.wheelAtlas.findRegion("buff_item_10002_dis"), 7, 2, "1182", 1182));
//    Wheel.wheelItems.add(Wheel.WheelItem.newInst(GMain.wheelAtlas.findRegion("buff_item_10000_dis"), 8, 2, "1281", 1281));
//    Wheel.wheelItems.add(Wheel.WheelItem.newInst(GMain.wheelAtlas.findRegion("SoXam"), 9, 2, "503", 503));
//    Wheel.wheelItems.add(Wheel.WheelItem.newInst(GMain.wheelAtlas.findRegion("coins"), 10, 2, "1367", 1367));
//    Wheel.wheelItems.add(Wheel.WheelItem.newInst(GMain.wheelAtlas.findRegion("buff_item_10001_dis"), 11, 2, "944", 944));

    Wheel.inst().setWheelListener(new Wheel.EventListener() {
      @Override
      public boolean start() {
        return true;
      }

      @Override
      public void end(Wheel.WheelItem item) {

      }

      @Override
      public void error(String msg) {

      }
    });

    Wheel.inst().init();

  }

  public static WheelMiniGame getInstance(List<WheelData> data) {
    return instance == null ? instance = new WheelMiniGame(data) : instance;
  }
  
  public void setPosition(float x, float y) {
    Wheel.inst().setPosition(x, y);
  }

  public void setScale(float x, float y) {
    Wheel.inst().setScale(x, y);
  }

  public float getWidth() {
    return Wheel.inst().getWidth();
  }

  public float getHeight() {
    return Wheel.inst().getHeight();
  }

  public void addToScene(Group gParent) {
    gParent.addActor(Wheel.inst());
  }

  public void addListener(Wheel.EventListener eventListener) {
    Wheel.inst().setWheelListener(eventListener);
  }
  
}
