package com.ss.config;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.ss.core.util.GAssetsManager;

public class Config {

  //label: init font
  public static final BitmapFont whiteFont                = GAssetsManager.getBitmapFont("white_font.fnt");
  public static final BitmapFont redFont                  = GAssetsManager.getBitmapFont("red_font.fnt");
  public static final BitmapFont greenFont                = GAssetsManager.getBitmapFont("green_font.fnt");

  //label: file particle
  public static final FileHandle ICE_PARTICLE             = Gdx.files.internal("particles/ice.p");
  public static final FileHandle BURN_JAM                 = Gdx.files.internal("particles/burn_jam.p");
  public static final FileHandle BURN_ALL                 = Gdx.files.internal("particles/burn_all.p");
  public static final FileHandle EXPLODE                  = Gdx.files.internal("particles/explode.p");

  //label: amount item
  public static final int     AMOUNT_ITEM_CREATE          = 49;
  public static final int     AMOUNT_ITEM                 = 10;
  public static final int     SCORE_FRUIT                 = 40;
  public static final int     NEXT_LEVEL                  = 2;
  public static final long    TARGET                      = 3000;
  public static final long    TARGET_INCREASE             = 1000;
  public static final int     ADD_SECOND                  = 15;
  public static final int     TIME_START_GAME             = 120;

  //label: game play ui
  public static final float   OFFSET_Y_BGTABLE            = 150;

  public static final int     COL                         = 7,
                              ROW                         = 7;

  public static final float   OFFSET_X_PIECE              = 10,
                              OFFSET_Y_PIECE              = 8,
                              WIDTH_PIECE                 = 97,
                              HEIGHT_PIECE                = 97;

  //label: time action item
  public static final float   TIME_ADD_ITEM               = .75f;
  public static final float   WRAP_ITEM                   = .25f;
  public static final float   TIME_DELAY_TO_CHECK_ALL     = .35f;
  public static final float   TIME_SLIDE                  = .3f;

  //label: score item
//  public static final int     SCORE_FRUIT               = 40;
  public static final int     SCORE_GLASS_JUICE           = 60;
  public static final int     SCORE_JAM                   = 70;
  public static final int     SCORE_CLOCK                 = 15;
  public static final int     SCORE_SKILL_JAM             = 120;
  public static final int     SCORE_SKILL_GLASS_JUICE     = 100;

}
