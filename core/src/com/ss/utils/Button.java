package com.ss.utils;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.ss.core.util.GUI;

public class Button extends Group {

  private Label lb;
  private Image btn;

  public Button(TextureAtlas atlas, String region, String text, BitmapFont bitmap) {

    btn = GUI.createImage(atlas, region);
    lb  = new Label(text, new Label.LabelStyle(bitmap, null));

    setSize(btn.getWidth(), btn.getHeight());
    this.setOrigin(Align.center);
    this.addActor(btn);
    this.addActor(lb);

    lb.setAlignment(Align.center);
    lb.setPosition(btn.getX() + btn.getWidth()/2 - lb.getWidth()/2,
            btn.getY() + btn.getHeight()/2 - lb.getHeight()/2);

  }

  public void movebyLb(float x, float y) {
    lb.moveBy(x, y);
  }

  public void setFontScale(float sclX, float sclY) {
    lb.setFontScale(sclX, sclY);
  }

}
