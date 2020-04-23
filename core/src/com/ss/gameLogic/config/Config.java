package com.ss.gameLogic.config;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Vector2;
import com.ss.GMain;
import com.ss.core.util.GAssetsManager;
import com.ss.core.util.GStage;

public class Config {

  public static float ratio = Gdx.graphics.getWidth() / 720;

  private static final FileHandle fh1 = Gdx.files.internal("wheel.json");
  public static String wheelData = GMain.platform.GetConfigStringValue("", fh1.readString());

  private static final FileHandle fh2 = Gdx.files.internal("other_games.json");
  public static String otherGameData = GMain.platform.GetConfigStringValue("", fh2.readString());

  public static final int SPIN_TIME = 3; //todo: remote config in firebase (default 3)
  public static final int SPIN_TIME_ADS = 1; //todo: spin time ads
  public static final int MONEY_ADS = 1000000; //todo: remote config 1000000 (default 500000)
  public static final int MINUS_MONEY_EXIT_GAME = 2; //todo: remote config minus money when player exit game during game is playing (default 2)

  public static float CENTER_X = GStage.getWorldWidth()/2;
  public static float CENTER_Y = GStage.getWorldHeight()/2;

  public static float SCL_EFFECT_WIN = 3f;
  public static float SCL_EFFECT_ALL_IN = 3f;

  public static final BitmapFont MONEY_FONT = GAssetsManager.getBitmapFont("money_font.fnt");
  public static final BitmapFont BUTTON_FONT = GAssetsManager.getBitmapFont("btn_font.fnt");
  public static final BitmapFont WIN_FONT = GAssetsManager.getBitmapFont("win_font.fnt");
  public static final BitmapFont PLUS_MONEY_FONT = GAssetsManager.getBitmapFont("plus_money_font.fnt");
  public static final BitmapFont ALERT_FONT = GAssetsManager.getBitmapFont("alert_font.fnt");
  public static final BitmapFont TUTORIAL = GAssetsManager.getBitmapFont("tutorial_font.fnt");

  public static final Vector2 POS_DESK_RESIDUAL = new Vector2(100, 1070);
  public static final Vector2 POS_BOT_0 = new Vector2(GStage.getWorldWidth()/2 - 170, GStage.getWorldHeight() - 200);
  public static final Vector2 POS_BOT_1 = new Vector2(GStage.getWorldWidth() - 270, GStage.getWorldHeight()/2 + 10);
  public static final Vector2 POS_BOT_2 = new Vector2(GStage.getWorldWidth() - 310, GStage.getWorldHeight()/2 - 200);
  public static final Vector2 POS_BOT_3 = new Vector2(GStage.getWorldWidth()/2 - 30, 30);
  public static final Vector2 POS_BOT_4 = new Vector2(180, POS_BOT_2.y);
  public static final Vector2 POS_BOT_5 = new Vector2(POS_BOT_4.x - 45, POS_BOT_1.y);

  public static final float SCL_CARD_INIT = .7f;
  public static final float SCL_SHOW_CARD = .45f;

  //config duration action
  public static final float DUR_DIVIDE_CARD = .5f;
  public static final float DUR_SCL_ROTATE_CARD = .5f;
  public static final float DUR_SPREAD_CARD = .25f;
  public static final float DUR_SCL_CARD_PLAYER = .15f;

}
