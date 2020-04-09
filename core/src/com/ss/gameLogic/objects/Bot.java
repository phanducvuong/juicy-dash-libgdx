package com.ss.gameLogic.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.ss.GMain;
import com.ss.core.util.GUI;
import com.ss.gameLogic.config.Config;
import com.ss.gameLogic.logic.Logic;
import com.ss.gameLogic.logic.Rule;
import java.util.ArrayList;
import java.util.List;

public class Bot {

  private Logic logic = Logic.getInstance();
  private Rule rule = Rule.getInstance();

  private boolean isActive = false, isAlive = false;
  public boolean isStartBet = false;
  public int id; //position bot
  public int idRule = -1; //-1 special else point of desk
  private long totalMoney = 0, totalMoneyBet = 0;

  private Label lbTotalMoney, lbNamePlayer;
  public Image avatar;

  public List<Card> lsCardDown, lsCardUp; //reset lsCardDown, lsCardUp

  public Bot(int id) {

    this.id = id;
    this.lsCardDown = new ArrayList<>();
    this.lsCardUp = new ArrayList<>();

  }

  public void initAvatar(Group group) {

    Vector2 pos = Logic.getInstance().getPosByIdBot(id);

    Image bgInfo = GUI.createImage(GMain.liengAtlas, "info_user");
    if (id == 0) {
      bgInfo.setScale(1.5f);
      bgInfo.setPosition(pos.x - 370, pos.y + 130);
    }
    else if (id == 1 || id == 2)
      bgInfo.setPosition(pos.x + 150, pos.y + 70);
    else
      bgInfo.setPosition(pos.x - 160, pos.y + 70);
    group.addActor(bgInfo);

    avatar = GUI.createImage(GMain.liengAtlas, "avatar");
    if (id == 0) {
      avatar.setScale(1.5f);
      avatar.setPosition(bgInfo.getX() + bgInfo.getWidth()*1.5f/2 - avatar.getWidth()*1.5f/2 - 5, bgInfo.getY() - avatar.getHeight()*1.5f);
    }
    else
      avatar.setPosition(bgInfo.getX() + bgInfo.getWidth()/2 - avatar.getWidth()/2 - 5, bgInfo.getY() - avatar.getHeight());
    group.addActor(avatar);

    //label: money
    lbTotalMoney = new Label("$2M", new Label.LabelStyle(Config.MONEY_FONT, null));
    lbTotalMoney.setAlignment(Align.center);
    if (id == 0) {
      lbTotalMoney.setFontScale(.75f);
      lbTotalMoney.setPosition(bgInfo.getX()+bgInfo.getWidth()*1.5f/2-lbTotalMoney.getWidth()/2 - 5, bgInfo.getY()+bgInfo.getHeight()*1.5f-lbTotalMoney.getHeight() - 10);
    }
    else {
      lbTotalMoney.setFontScale(.5f);
      lbTotalMoney.setPosition(bgInfo.getX()+bgInfo.getWidth()/2-lbTotalMoney.getWidth()/2 - 5, bgInfo.getY()+bgInfo.getHeight()-lbTotalMoney.getHeight());
    }
    group.addActor(lbTotalMoney);

    //label: name
    lbNamePlayer = new Label("User", new Label.LabelStyle(Config.MONEY_FONT, Color.WHITE));
    lbNamePlayer.setAlignment(Align.center);
    if (id == 0) {
      lbNamePlayer.setFontScale(.75f);
      lbNamePlayer.setPosition(bgInfo.getX()+bgInfo.getWidth()*1.5f/2-lbNamePlayer.getWidth()/2 - 5, bgInfo.getY() + 5);
    }
    else {
      lbNamePlayer.setFontScale(.5f);
      lbNamePlayer.setPosition(bgInfo.getX()+bgInfo.getWidth()/2-lbNamePlayer.getWidth()/2 - 5, bgInfo.getY() - 2);
    }
    group.addActor(lbNamePlayer);

  }

  public void TO(long money) {
    totalMoneyBet += money;
    totalMoney -= money;
  }

  public void AllIn() {
    totalMoneyBet += totalMoney;
    totalMoney = 0;
  }

  public void THEO(long money) {
    totalMoneyBet += money;
    totalMoney -= money;
  }

  public void UP () {
    isAlive = false;
  }

  public void reset() {

    isStartBet = false;
    idRule = -1;
    totalMoneyBet = 0;

  }

  public void convertMoneyToString() {
    lbTotalMoney.setText(logic.convertMoney(totalMoney));
  }

  public void findIdRule() {
    idRule = rule.getIdRuleOfDesk(lsCardUp);
  }

  public void setTotalMoney(long money) {
    this.totalMoney = money;
  }

  public void setActive(boolean active) {
    this.isActive = active;
  }

  public void setAlive(boolean alive) {
    this.isAlive = alive;
  }

  public boolean isActive() {
    return isActive;
  }

  public boolean isAlive() {
    return isAlive;
  }

  public long getTotalMoney() {
    return totalMoney;
  }

  public long getTotalMoneyBet() {
    return totalMoneyBet;
  }

  public void setTotalMoneyBet(long totalMoneyBet) {
    this.totalMoneyBet = totalMoneyBet;
  }
}
