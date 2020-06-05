package com.ss;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.platform.IPlatform;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.ss.config.Config;
import com.ss.core.effect.SoundEffects;
import com.ss.core.exSprite.particle.GParticleSystem;
import com.ss.core.util.GAssetsManager;
import com.ss.core.util.GDirectedGame;
import com.ss.core.util.GStage;
import com.ss.core.util.GStage.StageBorder;
import com.ss.scenes.GameScene;
import com.ss.config.C;
import com.ss.scenes.StartScene;

public class GMain extends GDirectedGame {

  public static int          screenHeight = 0;
  public static int          screenWidth  = 0;
  public static TextureAtlas bgAtlas,
                             itemAtlas,
                             particleAtlas,
                             popupAtlas;
  public static Preferences  pref;

  public static IPlatform    platform;
  public static GMain        inst;
  public StartScene          startScene;
  public GameScene           gameScene;

  public GMain(IPlatform plat) {
    platform = plat;
    inst     = this;
  }

  private void init() {

    screenWidth   = 720;
    screenHeight  = 1280;

    GStage.init(screenWidth, screenHeight, 0, 0, new StageBorder() {
      @Override
      public void drawHorizontalBorder(Batch spriteBatch, float paramFloat1, float paramFloat2) {

      }

      @Override
      public void drawVerticalBorder(Batch spriteBatch, float paramFloat1, float paramFloat2) {

      }
    });

  }

  public void create() {

    pref = Gdx.app.getPreferences("juice_dash");
    if (!pref.getBoolean("isNewbie")) {

      pref.putBoolean("isNewbie", true);
      pref.putInteger("amount_item_star", Config.AMOUNT_SKILL_STAR);
      pref.putInteger("amount_item_boom", Config.AMOUNT_SKILL_BOOM);
      pref.putBoolean("is_tutorial", true);
      pref.flush();

    }

    initLocalNotification();
    SoundEffects.initSound();
    C.init();

    bgAtlas       = GAssetsManager.getTextureAtlas("bg.atlas");
    popupAtlas    = GAssetsManager.getTextureAtlas("popup.atlas");
    itemAtlas     = GAssetsManager.getTextureAtlas("item.atlas");
    particleAtlas = GAssetsManager.getTextureAtlas("particle.atlas");
    GAssetsManager.finishLoading();

    this.init();
    gameScene   = new GameScene();
    startScene  = new StartScene();

    this.setScreen(startScene);

  }

  private void initLocalNotification(){

//    platform.SetDailyNotification(2, "Liêng 2020", "Chơi ngay để nhận miễn phí $" + Config.MONEY_NOTIFY, 2, 19);
//    platform.SetDailyNotification(7, "Lieng 2020", "Chơi ngay để nhận miễn phí $" + Config.MONEY_NOTIFY, 7, 19);

    int noId = platform.GetNotifyId();
    if(noId==-1) {
      //binhthuong
    } else if(noId == 1) {
//      long money = pref.getLong("money") + Config.MONEY_NOTIFY;
//      Util.getInstance().saveMoney(money);
    }

  }

  public void dispose() {
    GMain.platform.log("############## gmain dispose");
    GParticleSystem.saveAllFreeMin();
    super.dispose();
  }
}
