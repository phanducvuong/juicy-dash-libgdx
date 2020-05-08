package com.ss.gameLogic.effects;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.ss.GMain;
import com.ss.core.effect.ParticleEffects;

public class Particle {

  public boolean isAlive = false;
  private ParticleEffects particle;

  public Particle(Group group, FileHandle fileHandle) {

    if (GMain.particleAtlas != null) {
      ParticleEffect pe = new ParticleEffect();
      pe.load(fileHandle, GMain.particleAtlas);

      ParticleEffectPool pep = new ParticleEffectPool(pe, 0, 100);
      particle = new ParticleEffects(group, pep, pe);
    }


  }

  public void start(float x, float y, float sclEffect) {

    isAlive = true;
    particle.start(x, y,sclEffect);

  }

  public void remove() {
    particle.remove();
  }

}
