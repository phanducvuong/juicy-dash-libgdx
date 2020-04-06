package com.ss.gameLogic.logic;

import com.badlogic.gdx.math.Vector2;
import com.ss.gameLogic.card.Number;
import com.ss.gameLogic.card.Type;
import com.ss.gameLogic.config.Config;
import com.ss.gameLogic.objects.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Logic {

  private static Logic instance;

  public static Logic getInstance() {
      return instance == null ? instance = new Logic() : instance;
  }

  private Logic(){}

  public Card getCardBuyTypeAndNumber(Type type, Number number, List<Card> lsCard) {

    for (Card card : lsCard)
      if (card.number == number && card.type == type)
        return card;
    return null;

  }

  public float rndRotate() {
    return Math.round(Math.random() * 360);
  }

  public Vector2 getPosByIdBot(int id) {

    Vector2 tempV = new Vector2();
    switch (id) {
      case 0:
        tempV.x = Config.POS_BOT_0.x;
        tempV.y = Config.POS_BOT_0.y;
        break;
      case 1:
        tempV.x = Config.POS_BOT_1.x;
        tempV.y = Config.POS_BOT_1.y;
        break;
      case 2:
        tempV.x = Config.POS_BOT_2.x;
        tempV.y = Config.POS_BOT_2.y;
        break;
      case 3:
        tempV.x = Config.POS_BOT_3.x;
        tempV.y = Config.POS_BOT_3.y;
        break;
      case 4:
        tempV.x = Config.POS_BOT_4.x;
        tempV.y = Config.POS_BOT_4.y;
        break;
      case 5:
        tempV.x = Config.POS_BOT_5.x;
        tempV.y = Config.POS_BOT_5.y;
        break;
    }

    return tempV;

  }

  //sap
  public Card getBestCardInLsSap(List<List<Card>> lsCards) {

    Card bestCard = lsCards.get(0).get(0);
    for (int i=1; i < lsCards.size(); i++) {
      Card temp = lsCards.get(i).get(0);
      if (bestCard.number.ordinal() < temp.number.ordinal())
        bestCard = temp;
    }

    return bestCard;

  }

  //lieng
  public Card getBestCardInLsLieng(List<List<Card>> lsCards) {

    List<Card> tempLsBestCard = new ArrayList<>();
    Card bestCard = findCardBestType(lsCards.get(0));

    for (int i=0; i<lsCards.size(); i++) {
      Card temp = findCardBestType(lsCards.get(i));
      if (bestCard.type.ordinal() < temp.type.ordinal()) {
        bestCard = temp;
        tempLsBestCard.clear();
        tempLsBestCard.add(bestCard);
      }
      else if (bestCard.type.ordinal() == temp.type.ordinal())
        tempLsBestCard.add(temp);
    }

    return tempLsBestCard.size() == 1 ? bestCard : findCardBestNumber(tempLsBestCard);

  }

  private Card findBestCardBuyNumberLieng(List<Card> lsCard) {

    List<Card> tempLs = new ArrayList<>(lsCard);
    Collections.sort(tempLs, (c1, c2) -> c1.number.ordinal() - c2.number.ordinal());

    Card card0 = tempLs.get(0);
    Card card1 = tempLs.get(1);
    Card card2 = tempLs.get(2);

    if (card0.number == Number.two && card1.number == Number.three && card2.number == Number.ace)
      return card1;
    else
      return card2;

  }
  //lieng

  //anh
  public Card getBestCardInLsAnh(List<List<Card>> lsCards) {

    List<Card> tempLsCard = new ArrayList<>();
    Card bestCard = findCardBestType(lsCards.get(0));

    for (int i=0; i<lsCards.size(); i++) {
      Card temp = findCardBestType(lsCards.get(i));
      if (bestCard.type.ordinal() < temp.type.ordinal()) {
        bestCard = temp;
        tempLsCard.clear();
        tempLsCard.add(bestCard);
      }
      else if (bestCard.type.ordinal() == temp.type.ordinal())
        tempLsCard.add(temp);
    }

    return tempLsCard.size() == 1 ? bestCard : findCardBestNumber(tempLsCard);

  }

  //point
  public Card getBestCardInLsPoint(List<List<Card>> lsCards) {

    List<List<Card>> tempLsBestPoint = new ArrayList<>();
    int bestPoint = calculatePoint(lsCards.get(0));

    for (int i=0; i<lsCards.size(); i++) {
      int tempPoint = calculatePoint(lsCards.get(i));
      if (bestPoint < tempPoint) {
        bestPoint = tempPoint;
        tempLsBestPoint.clear();
        tempLsBestPoint.add(lsCards.get(i));
      }
      else if (bestPoint == tempPoint)
        tempLsBestPoint.add(lsCards.get(i));
    }

    if (tempLsBestPoint.size() == 1)
      return tempLsBestPoint.get(0).get(0); //return card have best point
    else if (tempLsBestPoint.size() > 1) {

      List<Card> tempLs = new ArrayList<>();
      Card bestType = findCardBestType(tempLsBestPoint.get(0));

      for (int i=0; i<tempLsBestPoint.size(); i++) {
        Card tempCard = findCardBestType(tempLsBestPoint.get(i));
        if (bestType.type.ordinal() < tempCard.type.ordinal()) {
          bestType = tempCard;
          tempLs.clear();
          tempLs.add(bestType);
        }
        else if (bestType.type.ordinal() == tempCard.type.ordinal())
          tempLs.add(tempCard);
      }

      return findCardBestNumber(tempLs);

    }// > 2 card have the same point

    return null;

  }

  private Card findCardBestNumber(List<Card> lsCard) {

    List<Card> temps = new ArrayList<>(lsCard);
    Collections.sort(temps, (c1, c2) -> c1.number.ordinal() - c2.number.ordinal());
    return temps.get(temps.size() - 1);

  }

  private Card findCardBestType(List<Card> lsCard) {

    List<Card> tempLs = new ArrayList<>(lsCard);
    Collections.sort(tempLs, (t1, t2) -> t1.type.ordinal() - t2.type.ordinal());
    return tempLs.get(tempLs.size() - 1);

  }

  private int calculatePoint(List<Card> lsCard) {

    int sum = 0;
    for (Card card : lsCard)
      sum += getPointCard(card.number);
    return sum%10;

  }

  private int getPointCard(Number number) {

    int point = 0;
    switch (number) {
      case ace: point = 1; break;
      case two: point = 2; break;
      case three: point = 3; break;
      case four: point = 4; break;
      case five: point = 5; break;
      case six: point = 6; break;
      case seven: point = 7; break;
      case eight: point = 8; break;
      case nine: point = 9; break;
    }
    return point;

  }

}
