package com.ss.gameLogic.objects;

import com.ss.gameLogic.logic.Logic;
import com.ss.gameLogic.logic.Rule;

import java.util.ArrayList;
import java.util.List;

public class Bot {

  private Logic logic = Logic.getInstance();
  private Rule rule = Rule.getInstance();

  public boolean isActive = false, isAlive = false;
  public int id; //position bot
  public int idRule = -1; //-1 special else point of desk
  public long totalMoney = 0, totalMoneyBet = 0;

  public List<Card> lsCardDown, lsCardUp; //reset lsCardDown, lsCardUp

  public Bot(int id) {

    this.id = id;
    this.lsCardDown = new ArrayList<>();
    this.lsCardUp = new ArrayList<>();

  }

  public void reset() {

    idRule = -1;
    totalMoneyBet = 0;

  }

  public void findIdRule() {
    idRule = rule.getIdRuleOfDesk(lsCardUp);
  }

}
