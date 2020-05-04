package com.ss.core.effect;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;

import java.util.HashMap;

public class SoundEffects {

  public static HashMap<String, Sound> sounds;
//  private static FileHandle bgMusic = Gdx.files.internal("sound/bg_music.mp3");
//  private static FileHandle btnClick = Gdx.files.internal("sound/btn_click.mp3");
//  private static FileHandle chipOut = Gdx.files.internal("sound/chip_out.mp3");
//  private static FileHandle divideCard = Gdx.files.internal("sound/divide_card.mp3");
//  private static FileHandle showAllCards = Gdx.files.internal("sound/show_all_cards.mp3");
//  private static FileHandle win = Gdx.files.internal("sound/win.mp3");
//  private static FileHandle doorBell = Gdx.files.internal("sound/doorbell.mp3");
//  private static FileHandle aww = Gdx.files.internal("sound/aww.mp3");
//  private static FileHandle laserOff = Gdx.files.internal("sound/laseroff.mp3");
//  private static FileHandle theo = Gdx.files.internal("sound/theo.mp3");

  public static boolean isMuteSound = false;
  public static boolean isMuteMusic = false;

  private static Music music;

  public static void initSound() {

//    music = Gdx.audio.newMusic(bgMusic);
//    music.setVolume(.5f);
//    music.setLooping(true);
//
//    sounds = new HashMap<>();
//    sounds.put("btn_click", Gdx.audio.newSound(btnClick));
//    sounds.put("chip_out", Gdx.audio.newSound(chipOut));
//    sounds.put("divide_card", Gdx.audio.newSound(divideCard));
//    sounds.put("show_all_card", Gdx.audio.newSound(showAllCards));
//    sounds.put("win", Gdx.audio.newSound(win));
//    sounds.put("doorbell", Gdx.audio.newSound(doorBell));
//    sounds.put("aww", Gdx.audio.newSound(aww));
//    sounds.put("laser_off", Gdx.audio.newSound(laserOff));
//    sounds.put("theo", Gdx.audio.newSound(theo));

  }

  public static void startMusic() {
    if (!isMuteMusic)
      music.play();
    else
      music.stop();
  }

  public static void startSound(String sound) {
    sounds.get(sound).stop();
    if (!isMuteSound)
      sounds.get(sound).play();
  }

}
