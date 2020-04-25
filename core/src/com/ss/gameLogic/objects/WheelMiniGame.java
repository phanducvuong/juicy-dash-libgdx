package com.ss.gameLogic.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
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

    FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Alter_Gothic.ttf"));
    FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
    parameter.size = 50;
//    parameter.spaceX = 1;
    parameter.genMipMaps = true;
    parameter.minFilter = Texture.TextureFilter.MipMapLinearNearest;
    parameter.magFilter = Texture.TextureFilter.Linear;
    parameter.borderStraight = true;
    parameter.borderColor = Color.BLACK;
    parameter.shadowColor = Color.GRAY;
    parameter.borderWidth = 2.6f;
    BitmapFont font12 = generator.generateFont(parameter); // font size 12 pixels
    generator.dispose(); // don't forget to dispose to avoid memory leaks!

    Wheel.wheelTex = GMain.wheelAtlas.findRegion("wheel");
    Wheel.wheelTex.flip(false, true);

    Wheel.wheelTick = Gdx.audio.newSound(Gdx.files.internal("sound/wheel_sound.mp3"));//GMain.wheelAtlas.getSound("Wheel_sound");
    Wheel.pointer = GMain.wheelAtlas.findRegion("pointer");
    Wheel.pointer.flip(false, true);
    Wheel.wheelDot = GMain.wheelAtlas.findRegion("dot");
    Wheel.lightDot = GMain.wheelAtlas.findRegion("lightdot");
    Wheel.wheelText = font12;
    Wheel.TEXT_SPACE = 6f;
    Wheel.PARTITION = 12;

    for (WheelData d : data)
      Wheel.wheelItems.add(Wheel.WheelItem.newInst(GMain.wheelAtlas.findRegion(d.getRegion()),
              d.getId(),
              d.getQty(),
              d.getQtyText(),
              d.getPercent())
      );

    Wheel.inst().setWheelListener(new Wheel.EventListener() {
      @Override
      public boolean start() {
        return false;
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

  public float getWidth() {
    return Wheel.inst().getWidth()*Wheel.inst().getScaleX();
  }

  public float getHeight() {
    return Wheel.inst().getHeight()*Wheel.inst().getScaleY();
  }
  
  public void setPosition(float x, float y) {
    Wheel.inst().setPosition(x, y);
  }

  public void setScale(float x, float y) {
    Wheel.inst().setScale(x, y);
  }

  public void addToScene(Group gParent) {
    gParent.addActor(Wheel.inst());
  }

  public void setColor(Color color) {
    Wheel.inst().setColor(color);
  }

  public void addListener(Wheel.EventListener eventListener) {
    Wheel.inst().setWheelListener(eventListener);
  }
  
}
