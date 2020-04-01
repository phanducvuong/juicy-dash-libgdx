package com.ss.gameLogic.objects;

public class Bot {

  private boolean isAlive = false;
  public int id;

  public Bot(int id) {

    this.id = id;

  }

  public void setAlive(boolean isAlive) {
    this.isAlive = isAlive;
  }

}
