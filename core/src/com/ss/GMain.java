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
import com.ss.scenes.GameScene;

public class GMain extends GDirectedGame {

  public static boolean isDebug = false;
  public static final boolean isTest = false;
  public static int screenHeight = 0;
  public static int screenWidth = 0;
  public static final int testType = 2;
  public static TextureAtlas liengAtlas, cardAtlas, startSceneAtlas, wheelAtlas, particleAtlas;
  public static float ratioX, ratioY;
  public static Preferences pref;
  public static GameScene gameScene;

  public static IPlatform platform;
  public static GMain inst;
  public GMain(IPlatform plat){
    platform = plat;
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

    initLocalNotification();
    liengAtlas = GAssetsManager.getTextureAtlas("lieng.atlas");
    cardAtlas = GAssetsManager.getTextureAtlas("card.atlas");
    startSceneAtlas = GAssetsManager.getTextureAtlas("start_scene.atlas");
    wheelAtlas = GAssetsManager.getTextureAtlas("wheel.atlas");
    particleAtlas = GAssetsManager.getTextureAtlas("particle.atlas");

    pref = Gdx.app.getPreferences("lieng");

    if (!pref.getBoolean("isNewbie")) {

      pref.putBoolean("isNewbie", true);
      pref.putLong("money", 1000000);
      pref.flush();

    }

    this.init();
    SoundEffects.initSound();
    C.init();
    this.setScreen(menuScreen());

  }

  private void initLocalNotification(){
    platform.SetDailyNotification(1, "Lieng 2020", "Bam vao nhan duoc bao nhieu tien", 1, 18);
    //platform.SetDailyNotification(3, "Lieng 2020", "Bam vao nhan duoc bao nhieu tien", 3, 18);
    //platform.SetDailyNotification(7, "Lieng 2020", "Bam vao nhan duoc bao nhieu tien", 7, 18);

    int noId = platform.GetNotifyId();
    if(noId==-1){
      //binhthuong
    } else if(noId == 1){
      //thuong
    }
  }
  public void dispose() {
    GMain.platform.log("############## gmain dispose");
    GParticleSystem.saveAllFreeMin();
    super.dispose();
  }
}
