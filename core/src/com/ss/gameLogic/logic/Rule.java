package com.ss.gameLogic.logic;

import com.ss.gameLogic.card.Number;
import com.ss.gameLogic.objects.Bot;
import com.ss.gameLogic.objects.Card;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Rule {

  private static Rule instance;
  private Logic logic = Logic.getInstance();

  private Rule() {}

  public static Rule getInstance() {
    return instance == null ? instance = new Rule() : instance;
  }

  private boolean chkSap(List<Card> lsCard) {

    int count = 0;
    Card tempCard = lsCard.get(0);
    for (Card card : lsCard)
      if (card.number.ordinal() == tempCard.number.ordinal())
        count++;

    return count == 3;

  }

  private boolean chkLieng(List<Card> lsCard) {

    List<Card> lsTempCard = new ArrayList<>(lsCard);
    Collections.sort(lsTempCard, (c1, c2) -> c1.number.ordinal() - c2.number.ordinal());

    if (chkLiengHaveAce(lsTempCard))
      return true;
    else {

      int count = 0;
      Card card = lsTempCard.get(0);

      for (int i=1; i<lsTempCard.size(); i++) {
        int c = card.number.ordinal() + i;
        if (c == lsTempCard.get(i).number.ordinal())
          count++;
      }

      return count == 2;
    }
  }

  private boolean chkLiengHaveAce(List<Card> lsCard) {

    Card card0 = lsCard.get(0);
    Card card1 = lsCard.get(1);
    Card card2 = lsCard.get(2);

    return card0.number == Number.two && card1.number == Number.three && card2.number == Number.ace;

  }

  public boolean chkAnh(List<Card> lsCard) {

    int count = 0;
    for (Card card : lsCard)
      if (card.number.ordinal() > Number.ten.ordinal() && card.number.ordinal() < Number.ace.ordinal())
        count++;

    return count == 3;

  }

  private Card getBestCardInLsSap(List<List<Card>> lsCards) {

    if (lsCards.size() == 1)
      return lsCards.get(0).get(0);
    else if (lsCards.size() > 1)
      return logic.getBestCardInLsSap(lsCards);
    return null;

  }

  private Card getBestCardInLsLieng(List<List<Card>> lsCards) {

    if (lsCards.size() == 1)
      return lsCards.get(0).get(0);
    else if (lsCards.size() > 1)
      return logic.getBestCardInLsLieng(lsCards);
    return null;

  }

  private Card getBestCardInLsAnh(List<List<Card>> lsCards) {

    if (lsCards.size() == 1)
      return lsCards.get(0).get(0);
    else if (lsCards.size() > 1)
      return logic.getBestCardInLsAnh(lsCards);
    return null;

  }

  private Card getBestCardInLsPoint(List<List<Card>> lsCards) {

    if (lsCards.size() == 1)
      return lsCards.get(0).get(0);
    else if (lsCards.size() > 1)
      return logic.getBestCardInLsPoint(lsCards);
    return null;

  }

  public Bot getBotWinner(List<Bot> lsBotActive) {

    List<List<Card>> lsSap = new ArrayList<>();
    List<List<Card>> lsLieng = new ArrayList<>();
    List<List<Card>> lsAnh = new ArrayList<>();
    List<List<Card>> lsPoint = new ArrayList<>();

    //find bots have the same best desk card
    for (Bot bot : lsBotActive) {
      if (chkSap(bot.lsCardUp))
        lsSap.add(bot.lsCardUp);
      else if (chkLieng(bot.lsCardUp))
        lsLieng.add(bot.lsCardUp);
      else if (chkAnh(bot.lsCardUp))
        lsAnh.add(bot.lsCardUp);
      else
        lsPoint.add(bot.lsCardUp);
    }

    Card bestCardInLsSap = getBestCardInLsSap(lsSap);
    Card bestCardInLsLieng = getBestCardInLsLieng(lsLieng);
    Card bestCardInLsAnh = getBestCardInLsAnh(lsAnh);
    Card bestCardInLsPoint = getBestCardInLsPoint(lsPoint);

    if (bestCardInLsSap != null)
      return lsBotActive.get(bestCardInLsSap.getIdBot());
    else if (bestCardInLsLieng != null)
      return lsBotActive.get(bestCardInLsLieng.getIdBot());
    else if (bestCardInLsAnh != null)
      return lsBotActive.get(bestCardInLsAnh.getIdBot());
    else if (bestCardInLsPoint != null)
      return lsBotActive.get(bestCardInLsPoint.getIdBot());
    else {
      System.out.println("null winner");
      return lsBotActive.get(0);
    }

  }

  //-1: special else point
  public int getIdRuleOfDesk(List<Card> lsCard) {

    if (chkSap(lsCard) || chkLieng(lsCard) || chkAnh(lsCard))
      return -1;
    else
      return logic.calculatePoint(lsCard);

  }

}
