package com.ss.config;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.ss.GMain;
import com.ss.core.util.GAssetsManager;

public class Config {

  public static final String itemTutorial[][] = {
          {"apple", "orange", "banana", "strawberry", "grape", "apple", "orange"},
          {"orange", "banana", "strawberry", "grape", "apple", "orange", "banana"},
          {"grape", "apple", "grape", "banana", "strawberry", "banana", "strawberry"},
          {"orange", "banana", "orange", "apple", "apple", "grape", "grape"},
          {"strawberry", "grape", "apple", "orange", "banana", "orange", "apple"},
          {"apple", "orange", "banana", "strawberry", "orange", "apple", "orange"},
          {"banana", "strawberry", "grape", "apple", "grape", "banana", "strawberry"}
  };

  //label: init font
  public static final BitmapFont whiteFont                = GAssetsManager.getBitmapFont("white_font.fnt");
  public static final BitmapFont greenFont                = GAssetsManager.getBitmapFont("green_font.fnt");
  public static final BitmapFont brownFont                = GAssetsManager.getBitmapFont("font_brown.fnt");

  //label: file particle
  public static final FileHandle ICE_PARTICLE             = Gdx.files.internal("particles/ice.p");
  public static final FileHandle BURN_JAM                 = Gdx.files.internal("particles/burn_jam.p");
  public static final FileHandle BURN_ALL                 = Gdx.files.internal("particles/burn_all.p");
  public static final FileHandle EXPLODE                  = Gdx.files.internal("particles/explode.p");
  public static final FileHandle WONDER                   = Gdx.files.internal("particles/wonder.p");
  public static final FileHandle LOVELY                   = Gdx.files.internal("particles/lovely.p");
  public static final FileHandle NEW_ROUND                = Gdx.files.internal("particles/new_round.p");
  public static final FileHandle WIND                     = Gdx.files.internal("particles/wind.p");
  public static final FileHandle FALL_LEAF                = Gdx.files.internal("particles/fall_leaf.p");
  public static final FileHandle BOOM                     = Gdx.files.internal("particles/boom.p");
  public static final FileHandle SKILL_STAR               = Gdx.files.internal("particles/skill_star.p");

  //label: load json
  private static final FileHandle JSON_GAMES              = Gdx.files.internal("other_games.json");
  public  static final String     OTHER_GAME_STRING       = GMain.platform.GetConfigStringValue("", JSON_GAMES.readString());

  //label: count item match to show wonder
  public static final int     WONDER_LOVELY               = 8;
  public static final int     WONDER_FANTASTIC            = 12;
  public static final int     WONDER_AWESOME              = 15;
  public static final int     WONDER_AMAZING              = 20;

  //label: amount item
  public static final int     AMOUNT_ITEM_CREATE          = 49;
  public static final int     AMOUNT_ITEM                 = 13;
  public static final int     SCORE_FRUIT                 = 40;
  public static final int     NEXT_LEVEL                  = 2;
  public static final long    TARGET                      = 3000;
  public static final long    TARGET_INCREASE             = 1000;
  public static final long    MAX_TARGET_INCREASE         = 10000;
  public static final int     ADD_SECOND                  = 15;
  public static final int     TIME_START_GAME             = 3600;
  public static final int     TIME_DECREASE               = 10;
  public static final int     TIME_WATCH_ADS              = 30;
  public static final int     MIN_TIME_EXPIRED            = 70;
  public static final int     TIME_MINUS                  = 10;

  //label: game play ui
  public static final float   OFFSET_Y_BG_TABLE           = 110;
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
  public static final int     AMOUNT_SKILL_STAR           = 5;
  public static final int     AMOUNT_SKILL_BOOM           = 5;

  //label: watch ads to get item
  public static final int     ADS_SKILL_STAR              = 2;
  public static final int     ADS_SKILL_BOOM              = 2;

  //label: score item
//  public static final int     SCORE_FRUIT               = 40;
  public static final int     SCORE_GLASS_JUICE           = 60;
  public static final int     SCORE_JAM                   = 80;
  public static final int     SCORE_CLOCK                 = 70;
  public static final int     SCORE_SKILL_JAM             = 120;
  public static final int     SCORE_SKILL_GLASS_JUICE     = 100;

  //label: volume sound
  public static final float   MUSIC_VOLUME                = .8f;
  public static final float   CLICK_VOLUME                = 1f;
  public static final float   LASER_OFF_VOLUME            = 1f;
  public static final float   FREEZING_VOLUME             = .4f;
  public static final float   ICE_EXPLODE_VOLUME          = 1f;
  public static final float   CHEW_VOLUME                 = 1f;
  public static final float   DROP_VOLUME                 = 1f;
  public static final float   JAM_VOLUME                  = 1f;
  public static final float   ALARM_VOLUME                = 1f;
  public static final float   AMAZING_VOLUME              = .5f;
  public static final float   AWESOME_VOLUME              = .5f;
  public static final float   FANTASTIC_VOLUME            = .5f;
  public static final float   LOVELY_VOLUME               = .5f;
  public static final float   CLOCK_VOLUME                = 1f;
  public static final float   EXCHANGE_VOLUME             = 1f;
  public static final float   FIREWORK_VOLUME             = 1f;
  public static final float   GLASS_JUICE_VOLUME          = 1f;
  public static final float   LINE_VOLUME                 = 1f;
  public static final float   WIN_VOLUME                  = 1f;
  public static final float   STAMP_VOLUME                = 1f;
  public static final float   LOSE_VOLUME                 = 1f;
  public static final float   SKILL_BOOM_VOLUME           = 1f;
  public static final float   SKILL_STAR_VOLUME           = 1f;

}
