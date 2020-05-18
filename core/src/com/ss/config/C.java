package com.ss.config;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.I18NBundle;
import com.ss.GMain;

import java.util.Locale;
import java.util.MissingResourceException;

public class C {

    public static class remote {

//        public static int adsTime = 50;
//        public static long moneyAds = Config.MONEY_ADS;
//        public static long minusMoney = Config.MINUS_MONEY_EXIT_GAME;
//        public static long spinAdsTime = Config.SPIN_TIME_ADS;
//        public static long moneyBigWin = Config.REWARD_BIG_WIN;
//        public static long donate = Config.MONEY_DONATE;
//        public static long adsDonateStart = Config.ADS_DONATE_START;

        static void initRemoteConfig() {

        }
    }

    public static class lang {

      public static I18NBundle locale;

      static void initLocalize() {
        String deviceLang = GMain.platform.GetDefaultLanguage();
        FileHandle specFilehandle = Gdx.files.internal("i18n/lang_" + deviceLang);
//        FileHandle specFilehandle = Gdx.files.internal("i18n/lang_" + "id");
        FileHandle baseFileHandle = Gdx.files.internal("i18n/lang");

        try {
          locale = I18NBundle.createBundle(specFilehandle, new Locale(""));
        }
        catch (MissingResourceException e) {
          locale = I18NBundle.createBundle(baseFileHandle, new Locale(""));
        }

      }
    }

    public static void init() {
        remote.initRemoteConfig();
        lang.initLocalize();
    }
}
