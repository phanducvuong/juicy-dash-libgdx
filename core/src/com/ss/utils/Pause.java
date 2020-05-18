package com.ss.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.ss.core.util.GStage;

public class Pause extends Group {

  private ShapeRenderer sr;
  private Color color;

  public Pause(Color color) {
    this.color = color;
    sr = new ShapeRenderer();
    setSize(GStage.getWorldWidth(), GStage.getWorldHeight());
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    super.draw(batch, parentAlpha);

    sr.setProjectionMatrix(GStage.getCamera().combined);
    sr.begin(ShapeRenderer.ShapeType.Filled);
    sr.setColor(color);
    sr.rect(0,0, GStage.getWorldWidth(), GStage.getWorldHeight());
    sr.end();

  }
}
