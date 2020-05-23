package com.ss.gameLogic.effects;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.ss.core.effect.ParticleEffects;

public class Particle {

  private ParticleEffects particle;

  public Particle(Group group, FileHandle fileParticle, TextureAtlas atlas) {
    ParticleEffect pe = new ParticleEffect();
    pe.load(fileParticle, atlas);

    ParticleEffectPool pep = new ParticleEffectPool(pe, 0, 100);
    particle = new ParticleEffects(group, pep, pe);
  }

  public void start(float x, float y, float sclEffect) {
    particle.start(x, y,sclEffect);
  }

  public boolean isActive() {
    return particle.isActive;
  }

  public void remove() {
    particle.remove();
  }

  public void setActive(boolean isActive) {
    particle.isActive = isActive;
  }

}
