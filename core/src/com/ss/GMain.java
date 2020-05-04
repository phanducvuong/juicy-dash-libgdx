package com.ss;

import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.platform.IPlatform;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.ss.core.effect.SoundEffects;
import com.ss.core.exSprite.particle.GParticleSystem;
import com.ss.core.util.GAssetsManager;
import com.ss.core.util.GDirectedGame;
import com.ss.core.util.GScreen;
import com.ss.core.util.GStage;
import com.ss.core.util.GStage.StageBorder;
import com.ss.gameLogic.config.C;
import com.ss.gameLogic.config.Config;
import com.ss.scenes.GameScene;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class GMain extends GDirectedGame {

  public static int screenHeight = 0;
  public static int screenWidth = 0;
  public static TextureAtlas liengAtlas, cardAtlas, startSceneAtlas, wheelAtlas, particleAtlas;
  public static Preferences pref;
  public static GameScene gameScene;

  public static IPlatform platform;
  public static GMain inst;

  public GMain(IPlatform plat) {
    platform = plat;
    inst = this;
  }

  private void init() {
    screenWidth = 1280;
    screenHeight = 720;
    GStage.init(screenWidth, screenHeight, 0, 0, new StageBorder() {
      @Override
      public void drawHorizontalBorder(Batch spriteBatch, float paramFloat1, float paramFloat2) {

      }

      @Override
      public void drawVerticalBorder(Batch spriteBatch, float paramFloat1, float paramFloat2) {

      }
    });
  }

  private static GScreen menuScreen() {
    return gameScene = new GameScene();
  }

  public void create() {

//    if (!pref.getBoolean("isNewbie")) {

      SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.US);
      String day = formatter.format(Calendar.getInstance().getTime());

//      pref.putBoolean("isNewbie", true);
//      pref.putLong("money", Config.MONEY_NEWBIE);
//      pref.putString("day", day);
//      pref.putInteger("spin", Config.SPIN_TIME);
//      pref.flush();

//    }

    initLocalNotification();

    SoundEffects.initSound();

    this.init();

    SoundEffects.initSound();
    C.init();
    this.setScreen(menuScreen());

  }

  private void initLocalNotification(){

//    platform.SetDailyNotification(2, "Liêng 2020", "Chơi ngay để nhận miễn phí $" + Config.MONEY_NOTIFY, 2, 19);
//    platform.SetDailyNotification(7, "Lieng 2020", "Chơi ngay để nhận miễn phí $" + Config.MONEY_NOTIFY, 7, 19);

    int noId = platform.GetNotifyId();
    if(noId==-1) {
      //binhthuong
    } else if(noId == 1) {
//      long money = pref.getLong("money") + Config.MONEY_NOTIFY;
//      Logic.getInstance().saveMoney(money);
    }

  }

  public void dispose() {
    GMain.platform.log("############## gmain dispose");
    GParticleSystem.saveAllFreeMin();
    super.dispose();
  }
}
