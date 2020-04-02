package com.ss.gameLogic.objects;

import java.util.ArrayList;
import java.util.List;

public class Bot {

  private boolean isAlive = false;
  public int id;

  public List<Card> lsCardDown, lsCardUp; //reset lsCardDown, lsCardUp

  public Bot(int id) {

    this.id = id;
    this.lsCardDown = new ArrayList<>();
    this.lsCardUp = new ArrayList<>();

  }

  public void setAlive(boolean isAlive) {
    this.isAlive = isAlive;
  }

}
