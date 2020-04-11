package com.ss.gameLogic.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.ss.GMain;
import com.ss.core.util.GUI;
import com.ss.gameLogic.config.Config;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

public class Button {

  private Image btn;
  private Label lbTxt;
  public List<Image> pointBlur;
  private Group group;

  public Button(String region, String txt) {

    this.group = new Group();
    pointBlur = new ArrayList<>();
    btn = GUI.createImage(GMain.liengAtlas, region);

    lbTxt = new Label(txt, new Label.LabelStyle(Config.BUTTON_FONT, null));
    lbTxt.setAlignment(Align.center);
    lbTxt.setPosition(btn.getX() + btn.getWidth()/2 - lbTxt.getWidth()/2, btn.getY() + btn.getHeight()/2 - lbTxt.getHeight()/2);

    group.setSize(btn.getWidth(), btn.getHeight());
    group.setOrigin(btn.getWidth()/2, btn.getHeight()/2);
    group.addActor(btn);
    group.addActor(lbTxt);

  }

  public void addListener(ClickListener clickListener) {
    group.addListener(clickListener);
  }

  public void setPosition(float x, float y) {
    group.setPosition(x, y);
  }

  public void setScale(float x, float y) {
    group.setScale(x, y);
  }

  public void setColor(Color color) {
    btn.setColor(color);
    lbTxt.setColor(color);
  }

  public void setOriginCenter() {
    group.setOrigin(btn.getWidth()*btn.getScaleX()/2, btn.getHeight()*btn.getScaleY()/2);
  }

  public void setTouchable(Touchable touchable) {
    group.setTouchable(touchable);
  }

  public void addToGroup(Group gParent) {
    gParent.addActor(group);
  }

  public void setFontScale(float x, float y) {
    lbTxt.setFontScale(x, y);
  }

  public void remove() {
    group.remove();
  }

  public Group getGroup() {
    return group;
  }

  public void setVisible(boolean visible) {
    group.setVisible(visible);
  }

  public float getX() {
    return group.getX();
  }

  public float getY() {
    return group.getY();
  }

  public float getWidth() {
    return group.getWidth();
  }

  public float getHeight() {
    return group.getHeight();
  }

  public void debug() {
    group.debug();
  }
}
