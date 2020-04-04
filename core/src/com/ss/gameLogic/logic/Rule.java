package com.ss.gameLogic.logic;

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

  public boolean chkLieng(List<Card> lsCard) {

    List<Card> lsTempCard = new ArrayList<>(lsCard);
    Collections.sort(lsTempCard, (c1, c2) -> c1.number.ordinal() - c2.number.ordinal());

    int count = 0;
    Card card = lsTempCard.get(0);


    for (int i=1; i<lsTempCard.size(); i++) {
      int c = card.number.ordinal() + i;
      System.out.println(c);
      if (c == lsTempCard.get(i).number.ordinal())
        count++;
    }

    return count == 2;
  }

}
