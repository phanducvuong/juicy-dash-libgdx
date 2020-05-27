package com.ss.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;

public class Solid {

  public static Texture create(Color color) {
    Pixmap pixmap = new Pixmap(1,1, Pixmap.Format.RGBA8888);
    pixmap.setColor(color);
    pixmap.fillRectangle(0, 0, 1, 1);
    Texture texture = new Texture(pixmap);
    pixmap.dispose();

    return texture;
  }

}
