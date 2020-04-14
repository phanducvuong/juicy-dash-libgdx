package com.ss.gameLogic.objects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.ss.GMain;
import com.ss.core.util.GUI;
import com.ss.gameLogic.Game;
import com.ss.gameLogic.config.C;
import com.ss.gameLogic.config.Config;
import com.ss.gameLogic.effects.Effect;
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

  private Group gBot, gEffect;
  private Label lbTotalMoney, lbNamePlayer;
  public Image avatar, bgInfo;

  private Image bgBetCondition;
  private Label lbConditionBet, lbMoneyBet, lbMoneyChange;

  public List<Card> lsCardDown, lsCardUp; //reset lsCardDown, lsCardUp

  public Bot(Group gBot, Group gEffect, int id) {

    this.gBot = gBot;
    this.gEffect = gEffect;
    this.id = id;
    this.lsCardDown = new ArrayList<>();
    this.lsCardUp = new ArrayList<>();

    initAvatar();

  }

  private void initAvatar() {

    Vector2 pos = Logic.getInstance().getPosByIdBot(id);

    bgInfo = GUI.createImage(GMain.liengAtlas, "info_user");
    if (id == 0) {
      bgInfo.setScale(1.5f);
      bgInfo.setPosition(pos.x - 370, pos.y + 130);
    }
    else if (id == 1 || id == 2)
      bgInfo.setPosition(pos.x + 150, pos.y + 70);
    else
      bgInfo.setPosition(pos.x - 160, pos.y + 70);

    avatar = GUI.createImage(GMain.liengAtlas, "avatar");
    avatar.setOrigin(Align.center);
    if (id == 0) {
      avatar.setScale(1.5f);
      avatar.setOrigin(avatar.getWidth()*1.5f/2, avatar.getHeight()*1.5f/2);
      avatar.setPosition(bgInfo.getX() + bgInfo.getWidth()*1.5f/2 - avatar.getWidth()/2 + 10,
                            bgInfo.getY() - avatar.getHeight()*1.5f + 50);
    }
    else
      avatar.setPosition(bgInfo.getX() + bgInfo.getWidth()/2 - avatar.getWidth()/2 - 5, bgInfo.getY() - avatar.getHeight());

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

    //label: bg bet condition, lbConditionBet, lbMoneyBet, lbMoneyChange
    bgBetCondition = GUI.createImage(GMain.liengAtlas, "bet_condition");
    lbConditionBet = new Label("THEO", new Label.LabelStyle(Config.BUTTON_FONT, null));
    lbConditionBet.setAlignment(Align.center);
    lbMoneyBet = new Label("999K", new Label.LabelStyle(Config.MONEY_FONT, null));
    lbMoneyBet.setAlignment(Align.center);

    lbMoneyChange = new Label(C.lang.receive+"\n+$12.000", new Label.LabelStyle(Config.PLUS_MONEY_FONT, null));
    lbMoneyChange.setAlignment(Align.center);
    lbMoneyChange.setVisible(false);

    if (id == 0) {
      lbMoneyBet.setFontScale(.8f);
      lbConditionBet.setPosition(pos.x - 30, pos.y - 170);
      bgBetCondition.setScale(1.3f);
      bgBetCondition.setPosition(lbConditionBet.getX() + lbConditionBet.getWidth() + 20,
                                  lbConditionBet.getY() + lbConditionBet.getHeight()/2 - bgBetCondition.getHeight()*1.3f/2);
      lbMoneyBet.setPosition(bgBetCondition.getX() + bgBetCondition.getWidth()*1.3f/2 - lbMoneyBet.getWidth()/2 + 30,
                              bgBetCondition.getY() + bgBetCondition.getHeight()*1.3f/2 - lbMoneyBet.getHeight()/2 - 5);

      lbMoneyChange.setPosition(avatar.getX() + avatar.getWidth()/2 - lbMoneyChange.getWidth()/2 - 30,
                                avatar.getY() + avatar.getHeight()/2 - lbMoneyChange.getHeight()/2);
    }
    else {
      lbConditionBet.setFontScale(.8f);
      lbMoneyBet.setFontScale(.6f);
      if (id == 1 || id == 2) {
        bgBetCondition.setScaleX(-1);
        bgBetCondition.setPosition(bgInfo.getX() + bgInfo.getWidth()/2 - bgBetCondition.getWidth()/2,
                bgInfo.getY() + bgInfo.getHeight() + bgBetCondition.getHeight()/2 - 40);
        lbMoneyBet.setPosition(bgBetCondition.getX() - bgBetCondition.getWidth()/2 - lbMoneyBet.getWidth()/2 - 27,
                bgBetCondition.getY() + bgBetCondition.getHeight()/2 - lbMoneyBet.getHeight()/2 - 5);
      }
      else {
        bgBetCondition.setPosition(bgInfo.getX() + bgInfo.getWidth()/2 + bgBetCondition.getWidth()/2,
                                bgInfo.getY() + bgInfo.getHeight() + bgBetCondition.getHeight()/2 - 40);
        lbMoneyBet.setPosition(bgBetCondition.getX() + bgBetCondition.getWidth()/2 - lbMoneyBet.getWidth()/2 + 27,
                bgBetCondition.getY() + bgBetCondition.getHeight()/2 - lbMoneyBet.getHeight()/2 - 5);
      }
      lbConditionBet.setPosition(bgInfo.getX() + bgInfo.getWidth()/2 - lbConditionBet.getWidth()/2,
              bgBetCondition.getY() + bgBetCondition.getHeight()/2 - lbConditionBet.getHeight()/2);

      lbMoneyChange.setFontScale(.8f);
      lbMoneyChange.setPosition(avatar.getX() + avatar.getWidth()/2 - lbMoneyChange.getWidth()/2 - 10,
              avatar.getY() + avatar.getHeight()/2 - lbMoneyChange.getHeight()/2);
    }

  }

  public void eftLbMoneyChange(Game game, long moneyChange) {

    totalMoney += moneyChange;
    lbTotalMoney.setText(logic.convertMoneyBot(totalMoney));
    lbMoneyChange.setText(C.lang.receive + "\n+" + logic.convertMoneyBet(moneyChange));
    lbMoneyChange.setVisible(true);
    Effect.getInstance(game).moneyChange(lbMoneyChange);

  }

  private void resetLbMoneyChange() {

    if (id == 0)
      lbMoneyChange.setPosition(avatar.getX() + avatar.getWidth()/2 - lbMoneyChange.getWidth()/2 - 30,
              avatar.getY() + avatar.getHeight()/2 - lbMoneyChange.getHeight()/2);
    else
      lbMoneyChange.setPosition(avatar.getX() + avatar.getWidth()/2 - lbMoneyChange.getWidth()/2 - 10,
              avatar.getY() + avatar.getHeight()/2 - lbMoneyChange.getHeight()/2);

    lbMoneyChange.clearActions();
    lbMoneyChange.getColor().a = 1f;
    lbMoneyChange.setVisible(false);

  }

  public void eftMoneyWinner(Game game, long moneyWin) {

    totalMoney += moneyWin;
    System.out.println("MONEY WIN: " + moneyWin);
    lbTotalMoney.setText(logic.convertMoneyBot(totalMoney));
    lbMoneyChange.setText("+" + logic.convertMoneyBet(moneyWin));
    lbMoneyChange.setVisible(true);
    Effect.getInstance(game).moneyChange(lbMoneyChange);

  }

  public void addToScene() {

    gBot.addActor(bgInfo);
    gBot.addActor(avatar);
    gBot.addActor(lbTotalMoney);
    gBot.addActor(lbNamePlayer);

    bgBetCondition.setVisible(false);
    lbConditionBet.setVisible(false);
    lbMoneyBet.setVisible(false);
    gBot.addActor(bgBetCondition);
    gBot.addActor(lbConditionBet);
    gBot.addActor(lbMoneyBet);
    gEffect.addActor(lbMoneyChange);

  }

  public void removeActor() {

    bgInfo.remove();
    avatar.remove();
    lbTotalMoney.remove();
    lbNamePlayer.remove();
    lbMoneyChange.remove();

  }

  public void TO(long moneyBet, long moneyOwe) {
    totalMoneyBet += (moneyBet + moneyOwe);
    totalMoney -= (moneyBet + moneyOwe);

    hideConditionBet();
    showLbMoneyBet(moneyBet);
  }

  public void AllIn() {
    totalMoneyBet += totalMoney;
    totalMoney = 0;

    hideConditionBet();
    lbConditionBet.setText(C.lang.allIn);
    lbConditionBet.setVisible(true);
  }

  public void THEO(long money) {
    totalMoneyBet += money;
    totalMoney -= money;

    hideConditionBet();
    lbConditionBet.setText(C.lang.call);
    lbConditionBet.setVisible(true);
  }

  public void UP () {
    isAlive = false;
    eftFold();
  }

  private void eftFold() {

    hideConditionBet();
    bgInfo.setColor(Color.GRAY);
    avatar.setColor(Color.GRAY);
    lbNamePlayer.setColor(Color.GRAY);
    lbTotalMoney.setColor(Color.GRAY);

    for (Card card : lsCardDown) {
      card.setColor(Color.GRAY);
      lsCardUp.get(lsCardDown.indexOf(card)).setColor(Color.GRAY);
    }

  }

  public void reset() {

    isStartBet = false;
    isAlive = true;
    idRule = -1;
    totalMoneyBet = 0;
    lsCardDown.clear();
    lsCardUp.clear();

    hideConditionBet();
    resetLbMoneyChange();

    bgInfo.setColor(Color.WHITE);
    avatar.setColor(Color.WHITE);
    lbNamePlayer.setColor(Color.WHITE);
    lbTotalMoney.setColor(Color.WHITE);

  }

  private void showLbMoneyBet(long moneyBet) {
    bgBetCondition.setVisible(true);

    lbConditionBet.setText(C.lang.raise);
    lbConditionBet.setVisible(true);

    lbMoneyBet.setText(logic.convertMoneyBot(moneyBet));
    lbMoneyBet.setVisible(true);
  }

  public void hideConditionBet() {
    bgBetCondition.setVisible(false);
    lbMoneyBet.setVisible(false);
    lbConditionBet.setVisible(false);
  }

  public void convertTotalMoneyToString() {
    lbTotalMoney.setText(logic.convertMoneyBot(totalMoney));
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
