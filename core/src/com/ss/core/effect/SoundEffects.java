package com.ss.core.effect;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.ss.config.Config;

import java.util.HashMap;

public class SoundEffects {

  public  static HashMap<String, Sound> sounds;

  private static FileHandle bgMusic       = Gdx.files.internal("sound/music.mp3");
  private static FileHandle click         = Gdx.files.internal("sound/click_juice.mp3");
  private static FileHandle laser_off     = Gdx.files.internal("sound/laser_off.mp3");
  private static FileHandle chew_1        = Gdx.files.internal("sound/chew_1.mp3");
  private static FileHandle chew_2        = Gdx.files.internal("sound/chew_2.mp3");
  private static FileHandle chew_3        = Gdx.files.internal("sound/chew_3.mp3");
  private static FileHandle drop_1        = Gdx.files.internal("sound/drop_1.mp3");
  private static FileHandle drop_2        = Gdx.files.internal("sound/drop_2.mp3");
  private static FileHandle drop_3        = Gdx.files.internal("sound/drop_3.mp3");
  private static FileHandle alarm         = Gdx.files.internal("sound/alarm.mp3");
  private static FileHandle amazing       = Gdx.files.internal("sound/amazing.mp3");
  private static FileHandle awesome       = Gdx.files.internal("sound/awesome.mp3");
  private static FileHandle fantastic     = Gdx.files.internal("sound/fantastic.mp3");
  private static FileHandle lovely        = Gdx.files.internal("sound/lovely.mp3");
  private static FileHandle clock         = Gdx.files.internal("sound/clock.mp3");
  private static FileHandle exchange      = Gdx.files.internal("sound/exchange.mp3");
  private static FileHandle firework      = Gdx.files.internal("sound/firework.mp3");
  private static FileHandle freezing      = Gdx.files.internal("sound/freezing.mp3");
  private static FileHandle glass_juice   = Gdx.files.internal("sound/glass_juice.mp3");
  private static FileHandle ice_explode   = Gdx.files.internal("sound/ice_explode.mp3");
  private static FileHandle jam           = Gdx.files.internal("sound/jam.mp3");
  private static FileHandle line          = Gdx.files.internal("sound/line.mp3");
  private static FileHandle win           = Gdx.files.internal("sound/win.mp3");
  private static FileHandle stamp         = Gdx.files.internal("sound/stamp.mp3");
  private static FileHandle lose          = Gdx.files.internal("sound/lose.mp3");

  public static boolean isMuteSound = false;
  public static boolean isMuteMusic = false;

  public static Music music;

  public static void initSound() {

    music = Gdx.audio.newMusic(bgMusic);
    music.setVolume(Config.MUSIC_VOLUME);
    music.setLooping(true);

    sounds = new HashMap<>();
    sounds.put("click", Gdx.audio.newSound(click));
    sounds.put("laser_off", Gdx.audio.newSound(laser_off));
    sounds.put("chew_1", Gdx.audio.newSound(chew_1));
    sounds.put("chew_2", Gdx.audio.newSound(chew_2));
    sounds.put("chew_3", Gdx.audio.newSound(chew_3));
    sounds.put("drop_1", Gdx.audio.newSound(drop_1));
    sounds.put("drop_2", Gdx.audio.newSound(drop_2));
    sounds.put("drop_3", Gdx.audio.newSound(drop_3));
    sounds.put("alarm", Gdx.audio.newSound(alarm));
    sounds.put("amazing", Gdx.audio.newSound(amazing));
    sounds.put("awesome", Gdx.audio.newSound(awesome));
    sounds.put("fantastic", Gdx.audio.newSound(fantastic));
    sounds.put("lovely", Gdx.audio.newSound(lovely));
    sounds.put("clock", Gdx.audio.newSound(clock));
    sounds.put("exchange", Gdx.audio.newSound(exchange));
    sounds.put("firework", Gdx.audio.newSound(firework));
    sounds.put("freezing", Gdx.audio.newSound(freezing));
    sounds.put("glass_juice", Gdx.audio.newSound(glass_juice));
    sounds.put("ice_explode", Gdx.audio.newSound(ice_explode));
    sounds.put("jam", Gdx.audio.newSound(jam));
    sounds.put("line", Gdx.audio.newSound(line));
    sounds.put("win", Gdx.audio.newSound(win));
    sounds.put("stamp", Gdx.audio.newSound(stamp));
    sounds.put("lose", Gdx.audio.newSound(lose));

  }

  public static void startMusic() {
    if (!isMuteMusic)
      music.play();
    else
      music.stop();
  }

  public static void start(String sound, float volume) {
    sounds.get(sound).stop();
    if (!isMuteSound)
      sounds.get(sound).play(volume);
  }

}
