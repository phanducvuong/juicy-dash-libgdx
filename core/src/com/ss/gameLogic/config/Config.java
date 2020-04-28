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
  public static String wheelData = GMain.platform.GetConfigStringValue("WHEEL_MINI_GAME", fh1.readString());

  private static final FileHandle fh2 = Gdx.files.internal("other_games.json");
  public static String otherGameData = GMain.platform.GetConfigStringValue("OTHER_GAMES_LINK", fh2.readString());

  public static final int MULTI_RND_MONEY_BET = GMain.platform.GetConfigIntValue("MULTI_RND_MONEY_BET", 10);; //todo: remote config money bet
  public static final int MULTI_INIT_MONEY_BOT = GMain.platform.GetConfigIntValue("MULTI_INIT_MONEY_BOT", 5); //todo: remote config money bot equal money player * 5 (default 5)
  public static final long MONEY_NEWBIE = GMain.platform.GetConfigLongValue("MONEY_NEWBIE", 1000000); //todo: remote config money newbie (default 1000000)
  public static final long MONEY_DONATE = GMain.platform.GetConfigLongValue("MONEY_DONATE", 500000); //todo: remote config donate money when wrong network
  public static final int SPIN_TIME = GMain.platform.GetConfigIntValue("SPIN_TIME", 3); //todo: remote config in firebase (default 3)
  public static final int SPIN_TIME_ADS = GMain.platform.GetConfigIntValue("SPIN_TIME_ADS", 1); //todo: spin time ads
  public static final long MONEY_ADS = GMain.platform.GetConfigLongValue("MONEY_ADS", 1000000); //todo: remote config 1000000 (default 500000)
  public static final int MINUS_MONEY_EXIT_GAME = GMain.platform.GetConfigIntValue("MINUS_MONEY_EXIT_GAME", 2); //todo: remote config minus money when player exit game during game is playing (default 2)
  public static final int REWARD_BIG_WIN = GMain.platform.GetConfigIntValue("REWARD_BIG_WIN", 2); //todo: remote config reward big win (default x5)
  public static final int TIME_TO_SHOW_BIG_WIN_IN_GAME = GMain.platform.GetConfigIntValue("TIME_TO_SHOW_BIG_WIN_IN_GAME", 10); //todo: remote config show big win (default 3)
  public static final long ADS_DONATE_START = GMain.platform.GetConfigLongValue("ADS_DONATE_START", 2000000); //todo: remote config to give 2000000 (default 2000000)
  public static final int SHOW_FULL_SCREEN = GMain.platform.GetConfigIntValue("SHOW_FULL_SCREEN", 5); //todo: remote config show ads fullscreen (default 3)
  public static final long MONEY_NOTIFY = GMain.platform.GetConfigLongValue("MONEY_NOTIFY", 2000000); //todo: remote config money notifycation app (default 2000000)
  public static final long TIME_PLAYER_WIN_IN_GAME = GMain.platform.GetConfigIntValue("TIME_PLAYER_WIN_IN_GAME", 3); //todo: remote config play win in game (default 3)

  public static float SCL_EFFECT_WIN = 3f;
  public static float SCL_EFFECT_ALL_IN = 3f;

  public static final BitmapFont MONEY_FONT = GAssetsManager.getBitmapFont("money_font.fnt");
  public static final BitmapFont BUTTON_FONT = GAssetsManager.getBitmapFont("btn_font.fnt");
  public static final BitmapFont WIN_FONT = GAssetsManager.getBitmapFont("win_font.fnt");
  public static final BitmapFont PLUS_MONEY_FONT = GAssetsManager.getBitmapFont("plus_money_font.fnt");
  public static final BitmapFont ALERT_FONT = GAssetsManager.getBitmapFont("alert_font.fnt");
  public static final BitmapFont RANK_FONT = GAssetsManager.getBitmapFont("rank_font.fnt");

  public static float CENTER_X = 0;
  public static float CENTER_Y = 0;

  public static Vector2 POS_DESK_RESIDUAL = new Vector2(100, 1070);
  public static Vector2 POS_BOT_0 = new Vector2(0,0);
  public static Vector2 POS_BOT_1 = new Vector2(0,0);
  public static Vector2 POS_BOT_2 = new Vector2(0,0);
  public static Vector2 POS_BOT_3 = new Vector2(0,0);
  public static Vector2 POS_BOT_4 = new Vector2(180, POS_BOT_2.y);
  public static Vector2 POS_BOT_5 = new Vector2(POS_BOT_4.x - 45, POS_BOT_1.y);

  public static final float SCL_CARD_INIT = .7f;
  public static final float SCL_SHOW_CARD = .45f;

  //config duration action
  public static final float DUR_DIVIDE_CARD = .5f;
  public static final float DUR_SCL_ROTATE_CARD = .5f;
  public static final float DUR_SPREAD_CARD = .25f;
  public static final float DUR_SCL_CARD_PLAYER = .15f;

}
