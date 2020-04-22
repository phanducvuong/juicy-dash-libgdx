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

  public List<Chip> lsChip, lsChipReceive;
  public Vector2 posChipOut, posChipReceive;

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
    this.lsChip = new ArrayList<>();
    this.lsChipReceive = new ArrayList<>();
    this.posChipOut = new Vector2();
    this.posChipReceive = new Vector2();

    initAvatar();

  }

  private void initAvatar() {

    Vector2 pos = Logic.getInstance().getPosByIdBot(id);

    bgInfo = GUI.createImage(GMain.liengAtlas, "info_user");
    if (id == 0) {
      bgInfo.setScale(1.3f);
      bgInfo.setPosition(pos.x - 200, pos.y + 100);
    }
    else if (id == 1 || id == 2)
      bgInfo.setPosition(pos.x + 110, pos.y + 70);
    else
      bgInfo.setPosition(pos.x - 100, pos.y + 70);

    avatar = GUI.createImage(GMain.liengAtlas, "avatar");
    avatar.setOrigin(Align.center);
    if (id == 0) {
      avatar.setScale(1.3f);
      avatar.setOrigin(avatar.getWidth()*1.3f/2, avatar.getHeight()*1.3f/2);
      avatar.setPosition(bgInfo.getX() + bgInfo.getWidth()*1.3f/2 - avatar.getWidth()/2,
                            bgInfo.getY() - avatar.getHeight()*1.3f + 20);
    }
    else
      avatar.setPosition(bgInfo.getX() + bgInfo.getWidth()/2 - avatar.getWidth()/2 - 5, bgInfo.getY() - avatar.getHeight());

    //label: money
    lbTotalMoney = new Label("$2M", new Label.LabelStyle(Config.MONEY_FONT, null));
    lbTotalMoney.setAlignment(Align.center);
    if (id == 0) {
      lbTotalMoney.setFontScale(.5f);
      lbTotalMoney.setPosition(bgInfo.getX()+bgInfo.getWidth()*1.3f/2-lbTotalMoney.getWidth()/2 - 5,
              bgInfo.getY()+bgInfo.getHeight()*1.3f-lbTotalMoney.getHeight());
    }
    else {
      lbTotalMoney.setFontScale(.35f);
      lbTotalMoney.setPosition(bgInfo.getX()+bgInfo.getWidth()/2-lbTotalMoney.getWidth()/2 - 5, bgInfo.getY()+bgInfo.getHeight()-lbTotalMoney.getHeight());
    }

    //label: name
    lbNamePlayer = new Label("User", new Label.LabelStyle(Config.MONEY_FONT, Color.WHITE));
    lbNamePlayer.setAlignment(Align.center);
    if (id == 0) {
      lbNamePlayer.setFontScale(.5f);
      lbNamePlayer.setPosition(bgInfo.getX()+bgInfo.getWidth()*1.3f/2-lbNamePlayer.getWidth()/2 - 5, bgInfo.getY());
    }
    else {
      lbNamePlayer.setFontScale(.35f);
      lbNamePlayer.setPosition(bgInfo.getX()+bgInfo.getWidth()/2-lbNamePlayer.getWidth()/2 - 5, bgInfo.getY() - 10);
    }

    //label: bg bet condition, lbConditionBet, lbMoneyBet, lbMoneyChange
    bgBetCondition = GUI.createImage(GMain.liengAtlas, "bet_condition");
    lbConditionBet = new Label("THEO", new Label.LabelStyle(Config.BUTTON_FONT, null));
    lbConditionBet.setFontScale(.7f);
    lbConditionBet.setAlignment(Align.center);
    lbMoneyBet = new Label("999K", new Label.LabelStyle(Config.MONEY_FONT, null));
    lbMoneyBet.setAlignment(Align.center);

    lbMoneyChange = new Label(C.lang.receive+"\n+$12.000", new Label.LabelStyle(Config.PLUS_MONEY_FONT, null));
    lbMoneyChange.setAlignment(Align.center);
    lbMoneyChange.setVisible(false);

    if (id == 0) {
      lbMoneyBet.setFontScale(.5f);
      lbConditionBet.setPosition(pos.x - 30, pos.y - 70);
      bgBetCondition.setScale(1.2f);
      bgBetCondition.setPosition(lbConditionBet.getX() + lbConditionBet.getWidth() + 20,
                                  lbConditionBet.getY() + lbConditionBet.getHeight()/2 - bgBetCondition.getHeight()*1.2f/2);
      lbMoneyBet.setPosition(bgBetCondition.getX() + bgBetCondition.getWidth()*1.2f/2 - lbMoneyBet.getWidth()/2 + 30,
                              bgBetCondition.getY() + bgBetCondition.getHeight()*1.2f/2 - lbMoneyBet.getHeight()/2 - 5);

      lbMoneyChange.setPosition(avatar.getX() + avatar.getWidth()/2 - lbMoneyChange.getWidth()/2 - 30,
                                avatar.getY() + avatar.getHeight()/2 - lbMoneyChange.getHeight()/2);
    }
    else {
      lbConditionBet.setFontScale(.5f);
      lbMoneyBet.setFontScale(.3f, .4f);
      if (id == 1 || id == 2) {
        bgBetCondition.setScaleX(-1);
        bgBetCondition.setPosition(bgInfo.getX() + bgInfo.getWidth()/2 - bgBetCondition.getWidth()/2,
                bgInfo.getY() + bgInfo.getHeight() + bgBetCondition.getHeight()/2 - 40);
        lbMoneyBet.setPosition(bgBetCondition.getX() - bgBetCondition.getWidth()/2 - lbMoneyBet.getWidth()/2 - 15,
                bgBetCondition.getY() + bgBetCondition.getHeight()/2 - lbMoneyBet.getHeight()/2 - 5);
      }
      else {
        bgBetCondition.setPosition(bgInfo.getX() + bgInfo.getWidth()/2 + bgBetCondition.getWidth()/2,
                                bgInfo.getY() + bgInfo.getHeight() + bgBetCondition.getHeight()/2 - 40);
        lbMoneyBet.setPosition(bgBetCondition.getX() + bgBetCondition.getWidth()/2 - lbMoneyBet.getWidth()/2 + 15,
                bgBetCondition.getY() + bgBetCondition.getHeight()/2 - lbMoneyBet.getHeight()/2 - 5);
      }
      lbConditionBet.setPosition(bgInfo.getX() + bgInfo.getWidth()/2 - lbConditionBet.getWidth()/2,
              bgBetCondition.getY() + bgBetCondition.getHeight()/2 - lbConditionBet.getHeight()/2);

      lbMoneyChange.setFontScale(.5f);
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

    if (id == 0)
      Logic.getInstance().saveMoney(totalMoney);

    hideConditionBet();
    showLbMoneyBet(moneyBet);

    logic.getPosChipBuyBot(this);
    logic.calculateChip(lsChip, (moneyBet+moneyOwe), posChipOut);
  }

  public void AllIn() {

    logic.getPosChipBuyBot(this);
    logic.calculateChip(lsChip, totalMoney, posChipOut);

    totalMoneyBet += totalMoney;
    totalMoney = 0;

    if (id == 0)
      Logic.getInstance().saveMoney(totalMoney);

    hideConditionBet();
    lbConditionBet.setText(C.lang.allIn);
    lbConditionBet.setVisible(true);
  }

  public void THEO(long money) {
    totalMoneyBet += money;
    totalMoney -= money;

    if (id == 0)
      Logic.getInstance().saveMoney(totalMoney);

    hideConditionBet();
    lbConditionBet.setText(C.lang.call);
    lbConditionBet.setVisible(true);

    logic.getPosChipBuyBot(this);
    logic.calculateChip(lsChip, money, posChipOut);
  }

  public void UP () {
    isAlive = false;
    eftFold();
  }

  public void eftChipOut(Game game) {
    Effect.getInstance(game).chipOut(lsChip);
  }

  public void chipOutNewRound(Game game, long moneyBet) {
    logic.getPosChipBuyBot(this);
    logic.calculateChip(lsChip, moneyBet, posChipOut);
    eftChipOut(game);
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
