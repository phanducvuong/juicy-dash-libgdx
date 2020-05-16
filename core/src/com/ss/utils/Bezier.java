package com.ss.utils;

import com.badlogic.gdx.math.Vector2;

public class Bezier {

  private Bezier() {}

  public static Vector2 quadraticBeizer(Vector2 p0, Vector2 p1, Vector2 p2, float t) {
    Vector2 tmp = new Vector2();
    tmp.x = (float) (Math.pow(1 - t, 2) * p0.x +
                (1 - t) * 2 * t * p1.x +
                t * t * p2.x);
    tmp.y = (float) (Math.pow(1 - t, 2) * p0.y +
                (1 - t) * 2 * t * p1.y +
                t * t * p2.y);
    return tmp;
  }

  public static Vector2 cubicBezier(Vector2 p0, Vector2 p1, Vector2 p2, Vector2 p3, float t) {
    Vector2 tmp = new Vector2();
    tmp.x = (float) (Math.pow(1 - t, 3) * p0.x +
                Math.pow(1 - t, 2) * 3 * t * p1.x +
                (1 - t) * 3 * t * t * p2.x +
                t * t * t * p3.x);
    tmp.y = (float) (Math.pow(1 - t, 3) * p0.y +
                Math.pow(1 - t, 2) * 3 * t * p1.y +
                (1 - t) * 3 * t * t * p2.y +
                t * t * t * p3.y);
    return tmp;
  }

}
