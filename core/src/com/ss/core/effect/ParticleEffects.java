package com.ss.core.effect;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;

public class ParticleEffects extends Actor {

  private Group group;
  private ParticleEffectPool.PooledEffect poolEffect;
  private ParticleEffectPool pep;
  private ParticleEffect pe;
  public boolean isActive = false;

  public ParticleEffects(Group group, ParticleEffectPool pep, ParticleEffect pe) {
    this.group = group;
    this.pep = pep;
    this.pe = pe;
  }

  @Override
  public boolean remove() {
    if (poolEffect != null)
      poolEffect.free();
    isActive = false;
    return super.remove();
  }

  @Override
  public void act(float delta) {
    super.act(delta);
    pe.setPosition(getX(), getY());
    pe.update(delta);

    if (pe.isComplete())
      remove();

  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    super.draw(batch, parentAlpha);
    pe.draw(batch);
  }

  public void start(float x, float y, float scl) {
    pe.reset();
    poolEffect = pep.obtain();
    setX(x);
    setY(y);
    pe.scaleEffect(scl);
    group.addActor(this);
  }

  public void setRotate(float degree) {
    for (ParticleEmitter emitter : pe.getEmitters()) {
      ParticleEmitter.ScaledNumericValue val = emitter.getAngle();
      val.setHigh(degree);
    }
  }

}
