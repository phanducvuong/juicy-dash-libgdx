package com.ss.scenes;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.ss.GMain;
import com.ss.core.util.GAssetsManager;
import com.ss.core.util.GScreen;
import com.ss.gameLogic.config.C;
import com.ss.repository.Leaderboard;

public class LDBFactory {
  private static Leaderboard inst;

  public static Leaderboard getLDB() {
    if (inst == null)
      inst = new Leaderboard(new GService(), new LService(), new FService(), new SService(), new RService());
    return inst;
  }

  private static class GService implements Leaderboard.GUIService {

    BitmapFont whiteFont = GAssetsManager.getBitmapFont("rank_font.fnt");

    @Override
    public Label getWhiteText(String text, Color color, float scale) {
      Label label = new Label(text, new Label.LabelStyle(whiteFont, color));
      label.setFontScale(scale);
      return label;
    }

    @Override
    public BitmapFont whiteFont() {
      return whiteFont;
    }
  }

  private static class LService implements Leaderboard.LocalizeService {

    @Override
    public String getLocalizeText(String key) {
      return C.lang.locale.get(key);
    }

    @Override
    public String formatLocalizeText(String key, Object args) {
      return C.lang.locale.format(key, args);
    }
  }

  private static class FService implements Leaderboard.FirebaseService {

    @Override
    public void crashLog(String message) {
      //TODO implmentation
    }

    @Override
    public void trackScreen(String message) {
      //TODO implmentation
    }
  }

  private static class SService implements Leaderboard.ScreenService {

    @Override
    public GScreen backScreen() {
      return GMain.gameScene;
    }

  }

  private static class RService implements Leaderboard.RankingService {

    @Override
    public int getNRank() {
      return 1;
    }

    @Override
    public long getCurrentScore(int rank) {
      return GMain.pref.getLong("money");
    }

  }
}
