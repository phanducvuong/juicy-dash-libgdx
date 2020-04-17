package com.ss.gameLogic.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.ss.GMain;
import com.ss.core.util.GUI;
import com.ss.gameLogic.Game;
import com.ss.gameLogic.config.Config;
import com.ss.gameLogic.effects.Effect;

import java.util.ArrayList;
import java.util.List;

public class Button {

  private Image btn;
  private Label lbTxt;
  private List<Vector2> posLight;
  private List<Image> lsLight;
  private Group group;
  public boolean isLightStart = false;

  public Button(TextureAtlas atlas,String region, String txt, BitmapFont fontStyle) {

    this.group = new Group();
    posLight = new ArrayList<>();
    lsLight = new ArrayList<>();
    btn = GUI.createImage(atlas, region);

    lbTxt = new Label(txt, new Label.LabelStyle(fontStyle, null));
    lbTxt.setAlignment(Align.center);
    lbTxt.setPosition(btn.getX() + btn.getWidth()/2 - lbTxt.getWidth()/2, btn.getY() + btn.getHeight()/2 - lbTxt.getHeight()/2);

    group.setSize(btn.getWidth(), btn.getHeight());
    group.setOrigin(btn.getWidth()/2, btn.getHeight()/2);
    group.addActor(btn);
    group.addActor(lbTxt);

//    initPosBlur();

  }

  public void startEftLight(Game game) {
    if (isLightStart)
      for (Image light : lsLight)
        Effect.getInstance(game).lightBtn(light);

    isLightStart = false;
  }

  public void stopEftLight() {
    for (Image light : lsLight) {
      light.getColor().a = 0;
      light.clearActions();
    }
  }

  private void initPosBlur() {

    posLight.add(new Vector2(btn.getX() + btn.getWidth()/2 - 142, btn.getY() + btn.getHeight()/2 - 76));
    posLight.add(new Vector2(btn.getX() + btn.getWidth()/2 - 65, posLight.get(0).y));
    posLight.add(new Vector2(btn.getX() + btn.getWidth()/2 + 12, posLight.get(0).y));
    posLight.add(new Vector2(btn.getX() + btn.getWidth()/2 + 88, posLight.get(0).y));
    posLight.add(new Vector2(btn.getX() + btn.getWidth()/2 + 124, btn.getY() + btn.getHeight()/2 - 60));
    posLight.add(new Vector2(posLight.get(4).x, btn.getY() + btn.getHeight()/2 + 5));
    posLight.add(new Vector2(posLight.get(3).x, btn.getY() + btn.getHeight()/2 + 24));
    posLight.add(new Vector2(posLight.get(2).x, posLight.get(6).y));
    posLight.add(new Vector2(posLight.get(1).x, posLight.get(6).y));
    posLight.add(new Vector2(posLight.get(0).x, posLight.get(6).y));
    posLight.add(new Vector2(btn.getX() + btn.getWidth()/2 - 177, posLight.get(5).y));
    posLight.add(new Vector2(posLight.get(10).x, posLight.get(4).y));

    lsLight.add(GUI.createImage(GMain.liengAtlas, "white_blur"));
    lsLight.get(0).setPosition(posLight.get(0).x, posLight.get(0).y);
    group.addActor(lsLight.get(0));

    lsLight.add(GUI.createImage(GMain.liengAtlas, "white_blur"));
    lsLight.get(1).setPosition(posLight.get(1).x, posLight.get(1).y);
    group.addActor(lsLight.get(1));

    lsLight.add(GUI.createImage(GMain.liengAtlas, "white_blur"));
    lsLight.get(2).setPosition(posLight.get(2).x, posLight.get(2).y);
    group.addActor(lsLight.get(2));

    lsLight.add(GUI.createImage(GMain.liengAtlas, "white_blur"));
    lsLight.get(3).setPosition(posLight.get(3).x, posLight.get(3).y);
    group.addActor(lsLight.get(3));

    lsLight.add(GUI.createImage(GMain.liengAtlas, "white_blur"));
    lsLight.get(4).setPosition(posLight.get(4).x, posLight.get(4).y);
    group.addActor(lsLight.get(4));

    lsLight.add(GUI.createImage(GMain.liengAtlas, "white_blur"));
    lsLight.get(5).setPosition(posLight.get(5).x, posLight.get(5).y);
    group.addActor(lsLight.get(5));

    lsLight.add(GUI.createImage(GMain.liengAtlas, "white_blur"));
    lsLight.get(6).setPosition(posLight.get(6).x, posLight.get(6).y);
    group.addActor(lsLight.get(6));

    lsLight.add(GUI.createImage(GMain.liengAtlas, "white_blur"));
    lsLight.get(7).setPosition(posLight.get(7).x, posLight.get(7).y);
    group.addActor(lsLight.get(7));

    lsLight.add(GUI.createImage(GMain.liengAtlas, "white_blur"));
    lsLight.get(8).setPosition(posLight.get(8).x, posLight.get(8).y);
    group.addActor(lsLight.get(8));

    lsLight.add(GUI.createImage(GMain.liengAtlas, "white_blur"));
    lsLight.get(9).setPosition(posLight.get(9).x, posLight.get(9).y);
    group.addActor(lsLight.get(9));

    lsLight.add(GUI.createImage(GMain.liengAtlas, "white_blur"));
    lsLight.get(10).setPosition(posLight.get(10).x, posLight.get(10).y);
    group.addActor(lsLight.get(10));

    lsLight.add(GUI.createImage(GMain.liengAtlas, "white_blur"));
    lsLight.get(11).setPosition(posLight.get(11).x, posLight.get(11).y);
    group.addActor(lsLight.get(11));

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

  public void moveByLb(float x, float y) {
    lbTxt.moveBy(x, y);
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
    lbTxt.setPosition(btn.getX() + btn.getWidth()/2 - lbTxt.getWidth()/2, btn.getY() + btn.getHeight()/2 - lbTxt.getHeight()/2);
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

  public float getScaleX() {
    return group.getScaleX();
  }

  public float getScaleY() {
    return group.getScaleY();
  }

  public void debug() {
    group.debug();
  }

}
