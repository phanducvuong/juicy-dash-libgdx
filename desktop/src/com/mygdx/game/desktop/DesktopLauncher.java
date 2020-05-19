package com.mygdx.game.desktop;

import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.platform.IPlatform;
import com.ss.GMain;

public class DesktopLauncher {
  public static void main (String[] arg) {
    LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
    config.width = 720/2;
    config.height = 1280/2;
    config.samples = 2;
    config.x = 1000;
    GMain game = new GMain(new IPlatform() {
      @Override
      public void log(String str) {

      }

      @Override
      public String GetDefaultLanguage() {
        return "en";
      }

      @Override
      public boolean isVideoRewardReady() {
        return false;
      }

      @Override
      public void ShowVideoReward(OnVideoRewardClosed callback) {

      }

      @Override
      public void ShowFullscreen() {

      }

      @Override
      public void ShowBanner(boolean visible) {

      }

      @Override
      public int GetConfigIntValue(String name, int defaultValue) {
        return defaultValue;
      }

      @Override
      public long GetConfigLongValue(String name, int defaultValue) {
        return defaultValue;
      }

      @Override
      public String GetConfigStringValue(String name, String defaultValue) {
        return defaultValue;
      }

      @Override
      public void CrashKey(String key, String value) {

      }

      @Override
      public void CrashLog(String log) {

      }

      @Override
      public void TrackCustomEvent(String event) {

      }

      @Override
      public void TrackLevelInfo(String event, int mode, int difficult, int level) {

      }

      @Override
      public void TrackPlayerInfo(String event, int mode, int difficult, int level) {

      }

      @Override
      public void TrackPlaneInfo(String event, int planeid, int level) {

      }

      @Override
      public void TrackVideoReward(String type) {

      }

      @Override
      public void TrackPlayerDead(String event, int mode, int difficult, int level, int parentModel, int shooterModel, boolean isBoss) {

      }

      @Override
      public void ReportScore(long score) {

      }

      @Override
      public void ShowLeaderboard() {

      }

      @Override
      public void Restart() {

      }

      @Override
      public int GetNotifyId() {
        return 0;
      }

      @Override
      public void SetDailyNotification(int id, String header, String content, int days, int hours) {

      }

      @Override
      public void CancelDailyNotification(int id) {

      }
    });
    GMain.inst = game;
    new LwjglApplication(game, config);
  }
}
