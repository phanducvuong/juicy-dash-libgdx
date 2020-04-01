package com.ss.gameLogic.logic;

import com.badlogic.gdx.math.Vector2;
import com.ss.gameLogic.card.Number;
import com.ss.gameLogic.card.Type;
import com.ss.gameLogic.config.Config;
import com.ss.gameLogic.objects.Card;
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

}
