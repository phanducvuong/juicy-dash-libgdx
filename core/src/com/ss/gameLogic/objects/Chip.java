package com.ss.gameLogic.objects;

import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.ss.GMain;
import com.ss.core.util.GUI;

public class Chip {

  private Group gChip;
  private Image imgChip;
  private String name;
  public int idChip;

  public Chip(String region, int idChip) {

    this.name = region;
    this.idChip = idChip;
    imgChip = GUI.createImage(GMain.liengAtlas, region);

  }

  public void addToScene(Group group) {
    group.addActor(imgChip);
  }

  public void setZindex(int zindex) {
    imgChip.setZIndex(zindex);
  }

  public void setPosition(float x, float y) {
    imgChip.setPosition(x, y);
  }

  public void remove() {
    imgChip.remove();
  }

  public void log() {
    System.out.println(name);
  }

  public void addAction(SequenceAction seq) {
    imgChip.addAction(seq);
  }

  public void addAction(ParallelAction par) {
    imgChip.addAction(par);
  }

  public void addAction(Action action) {
    imgChip.addAction(action);
  }

}
