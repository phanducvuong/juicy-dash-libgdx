package com.ss.gameLogic.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.ss.core.effect.ParticleEffects;

public class Particle {

  public boolean isAlive = false;
  private ParticleEffects particle;

  public Particle(Group group, FileHandle fileHandle, String path) {

    ParticleEffect pe = new ParticleEffect();
    pe.load(fileHandle, Gdx.files.internal(path));

    ParticleEffectPool pep = new ParticleEffectPool(pe, 0, 100);
    particle = new ParticleEffects(group, pep, pe);

  }

  public void start(float x, float y) {

    isAlive = true;
    particle.start(x, y);

  }

  public void setScale(float scl) {
    particle.setScaleEffect(scl);
  }

}
