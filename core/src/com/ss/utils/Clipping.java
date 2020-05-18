package com.ss.utils;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;
import com.ss.core.util.GStage;

public class Clipping extends Group {

  private Rectangle scissors;
  private Rectangle clipBounds;
  private TextureRegion textureRegion;
  private float w, h, x, y;
  private float clipX = 1, clipY = 1;

  public Clipping(float x, float y, TextureAtlas atlas, String key) {

    textureRegion = atlas.findRegion(key);
    w = textureRegion.getRegionWidth();
    h = textureRegion.getRegionHeight();
    this.x = x;
    this.y = y;
    scissors = new Rectangle();
    clipBounds = new Rectangle(x, y, w, h);
  }

  @Override
  public void draw(Batch batch, float parentAlpha) {
    super.draw(batch, parentAlpha);

    ScissorStack.calculateScissors(GStage.getCamera(),
            batch.getTransformMatrix(),
            clipBounds, scissors);
    if (ScissorStack.pushScissors(scissors)) {
      batch.draw(textureRegion, x, y);
      batch.flush();
      ScissorStack.popScissors();
    }

  }

  public void clipBy(float clipX, float clipY) {
    this.clipX -= clipX;
    this.clipY -= clipY;
    clipBounds.set(x, y, w*this.clipX, h*this.clipY);
  }

  public void clipTo(float clipX, float clipY) {
    this.clipX = clipX;
    this.clipY = clipY;
    clipBounds.set(x, y, w*clipX, h*clipY);
  }

  public void reset(float clipX, float clipY) {
    this.clipX = clipX;
    this.clipY = clipY;
    clipBounds.set(x, y, w*this.clipX, h*this.clipY);
  }

}
