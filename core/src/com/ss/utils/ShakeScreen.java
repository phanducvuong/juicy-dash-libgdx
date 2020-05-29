package com.ss.utils;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector3;

import java.util.Random;

public class ShakeScreen {

  private float   elapsed, duration;
  private Random  random;
  private float   rndAngle, intensity;

  public ShakeScreen(float intensity, float duration) {
    this.elapsed    = duration;
    this.random     = new Random();
    this.duration   = duration;
    this.intensity  = intensity;
    this.rndAngle   = random.nextFloat() % 360f;
  }

  public void update (float delta, OrthographicCamera camera) {
    if (elapsed < duration) {

      // Calculate the amount of shake based on how long it has been shaking already
      float currentPower = intensity * camera.zoom * ((duration - elapsed) / duration);
      float x            = (random.nextFloat() - 0.5f) * currentPower;
      float y            = (random.nextFloat() - 0.5f) * currentPower;
      camera.translate(-x, -y);

      // Increase the elapsed time by the delta provided.
      elapsed += delta;
    }
  }

  public void reset() {
    elapsed = 0;
  }

}
