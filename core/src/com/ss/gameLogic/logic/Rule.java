package com.ss.gameLogic.logic;

import com.ss.gameLogic.card.Number;
import com.ss.gameLogic.objects.Bot;
import com.ss.gameLogic.objects.Card;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Rule {

  private static Rule instance;

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

//  private Card getBestCardInSap(List<List<Card>> lsCards)

  public int getIdWinner(List<Bot> lsBot) {



  }

}
