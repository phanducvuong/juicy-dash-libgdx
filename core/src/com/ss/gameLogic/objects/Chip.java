package com.ss.gameLogic.objects;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.ss.GMain;
import com.ss.core.util.GUI;

public class Chip {

  private Group gChip;
  private Image imgChip;

  public Chip(String region) {

    imgChip = GUI.createImage(GMain.liengAtlas, region);

  }

  public void addToScene(Group group, float x, float y) {
    group.addActor(imgChip);
    imgChip.setPosition(x, y);
  }

}
