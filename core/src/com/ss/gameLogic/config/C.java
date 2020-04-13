package com.ss.gameLogic.config;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.I18NBundle;

import java.util.Locale;
import java.util.MissingResourceException;

public class C {

    public static class remote {
        public static int adsTime = 50;
        static void initRemoteConfig() {

        }
    }

    public static class lang {

        private static I18NBundle locale;
        public static String title = "";
        public static String adsTimeLbl = "";
        public static String raise = "";
        public static String call = "";
        public static String fold = "";
        public static String allIn = "";
        public static String winner = "";
        public static String receive = "";

        static void initLocalize() {
            FileHandle specFilehandle = Gdx.files.internal("i18n/lang_" + "id");
            FileHandle baseFileHandle = Gdx.files.internal("i18n/lang");

            try {
                locale = I18NBundle.createBundle(specFilehandle, new Locale(""));
            }
            catch (MissingResourceException e) {
                locale = I18NBundle.createBundle(baseFileHandle, new Locale(""));
            }

            title = locale.get("title");
            adsTimeLbl = locale.format("adsTime", remote.adsTime);

            raise = locale.get("raise");
            call = locale.get("call");
            fold = locale.get("fold");
            allIn = locale.get("all_in");
            winner = locale.get("win");
            receive = locale.get("receive");
        }
    }

    public static void init() {
        remote.initRemoteConfig();
        lang.initLocalize();
    }
}
