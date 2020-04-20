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
  public static TextureAtlas liengAtlas, cardAtlas, startSceneAtlas, wheelAtlas;
  public static float ratioX, ratioY;
  public static Preferences pref;

  public static IPlatform platform;
  public GMain(IPlatform plat){
    platform = plat;
  }

  private void init() {
    float n = 480.0f;
    final boolean b = false;// 0.0f > 1.0f;
    final float n2 = Gdx.graphics.getWidth();
    final float n3 = Gdx.graphics.getHeight();
    final float n4 = n2 / n3;
    float n5;
    float n6;
    if (n4 == 0.0f) {
      n5 = 0.0f;
      n6 = 848.0f;
    }
    else if ((b && n4 > 0.0f) || (!b && n4 < 0.0f)) {
      final float n7 = 848.0f * n4;
      n5 = (n - n7) / 2.0f;
      n = n7;
      n6 = 848.0f;
    }
    else if ((b && n4 < 0.0f) || (!b && n4 > 0.0f)) {
      final float max = Math.max(800.0f, n / n4);
      GMain.screenHeight = (int)(0.5f + max);
      n6 = max;
      n5 = 0.0f;
    }
    else {
      n = n2;
      n6 = n3;
      n5 = 0.0f;
    }

    screenWidth = 1280;
    screenHeight = 720;
    n = 720;
    n6 = 1280;

    GStage.init(n, n6, n5, 0, new StageBorder() {
      @Override
      public void drawHorizontalBorder(Batch spriteBatch, float paramFloat1, float paramFloat2) {

      }

      @Override
      public void drawVerticalBorder(Batch spriteBatch, float paramFloat1, float paramFloat2) {

      }
    });
  }

  private static GScreen menuScreen()
  {
    return new GameScene();
  }

  public void create() {

    initLocalNotification();
    liengAtlas = GAssetsManager.getTextureAtlas("lieng.atlas");
    cardAtlas = GAssetsManager.getTextureAtlas("card.atlas");
    startSceneAtlas = GAssetsManager.getTextureAtlas("start_scene.atlas");
    wheelAtlas = GAssetsManager.getTextureAtlas("wheel.atlas");

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
