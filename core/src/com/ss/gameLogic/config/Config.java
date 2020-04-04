package com.ss.gameLogic.config;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.ss.core.util.GStage;

public class Config {

  public static float ratio = Gdx.graphics.getWidth() / 720;

  public static final Vector2 POS_BOT_0 = new Vector2(GStage.getWorldWidth()/2 - 120, GStage.getWorldHeight() - 300);
  public static final Vector2 POS_BOT_1 = new Vector2(GStage.getWorldWidth() - 300, GStage.getWorldHeight()/2 + 50);
  public static final Vector2 POS_BOT_2 = new Vector2(GStage.getWorldWidth() - 300, GStage.getWorldHeight()/2 - 350);
  public static final Vector2 POS_BOT_3 = new Vector2(GStage.getWorldWidth()/2 - 100, 160);
  public static final Vector2 POS_BOT_4 = new Vector2(120, POS_BOT_2.y);
  public static final Vector2 POS_BOT_5 = new Vector2(POS_BOT_4.x, POS_BOT_1.y);

  public static final float SCL_CARD_INIT = 1.1f;

  //config duration action
  public static final float DUR_DIVIDE_CARD = .5f;
  public static final float DUR_SCL_ROTATE_CARD = .5f;
  public static final float DUR_SPREAD_CARD = .25f;
  public static final float DUR_SCL_CARD_PLAYER = .15f;

}
