package com.ss.gameLogic.logic;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.ss.core.action.exAction.GSimpleAction;
import com.ss.core.util.GStage;
import com.ss.gameLogic.Game;
import com.ss.gameLogic.config.Config;
import com.ss.gameLogic.effects.Effect;
import com.ss.gameLogic.objects.Bot;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class Bet {

  private Logic logic = Logic.getInstance();
  private Effect effect;
  private Game game;

  private long totalMoneyBet = 10000;
  public long totalMoney = 0; //money all player bet
  private int turn = -1, idBotKeepMaxMoneyTo = -1;
  private Bot botPresent;
  private boolean isResetBotStartBet = true, isMoneyBotZero = false;

  public Bet(Game game) {
    this.game = game;
    this.effect = Effect.getInstance(game);
  }

  public void startBet(Bot bot) {

    botPresent = bot;
    if (game.lsBotActive.indexOf(bot) != 0)
      game.gBot.addAction(GSimpleAction.simpleAction(this::actionBet));

  }

  private boolean actionBet(float dt, Actor a) {

    turn = game.lsBotActive.indexOf(botPresent) + 1;
    if (turn >= game.lsBotActive.size())
      turn = 0;

    //todo: if botPre is not alive => next turn

    game.gBot.addAction(sequence(
            run(() -> {
              if (botPresent.isAlive() && botPresent.getTotalMoney() > 0) {
                isMoneyBotZero = false;
                effect.myTurn(botPresent);
                rndBet(botPresent);
              }
              else
                isMoneyBotZero = true;
            }),
            delay(turn != 0 && !isMoneyBotZero ? logic.timeDelayToNextTurnBet() : 0),
            run(() -> nextTurn(turn))
    ));

    return true;

  }

  public void nextTurn(int turn) {

    if (game.lsBotActive.size() > 0) {

      Bot botNext = game.lsBotActive.get(turn);

      if (logic.countBotAlive(game.lsBotActive) == 1) {
        //todo: finished bet
        game.getWinner();
        System.out.println("CASE 1");
//      System.out.println("size lsBotActive == 1 " + game.getWinner());
      }
      else if (idBotKeepMaxMoneyTo == -1 && botNext.isStartBet) {
        game.findWinner();
        System.out.println("CASE 2");
      }
      else if (botNext.id == idBotKeepMaxMoneyTo) {
        //todo: find winner
        game.findWinner();
        System.out.println("CASE 3");
//      System.out.println("Winner  " + game.findWinner().id);
      }
      else if (turn > 0) {
        startBet(botNext);
        System.out.println("CASE 4");
      }
      else if (!botNext.isAlive() || botNext.getTotalMoney() <= 0) {
        //if player is "UP" => next turn (botNext is player)
        Bot tempBotNext = game.lsBotActive.get(1);
        if (tempBotNext.id == idBotKeepMaxMoneyTo || tempBotNext.isStartBet) {
          game.findWinner();
          System.out.println("CASE 5");
//        System.out.println("FIND WINNER  " + game.findWinner().id);
        }
        else {
          startBet(tempBotNext);
          System.out.println("CASE 6");
        }
      }
      else {
        game.gamePlayUI.showBtnBet();
        // todo: turn player, show btn bet
        System.out.println("TURN PLAYER ----------------------------");
      }

    }

  }

  public void TO(Bot bot) {

    //todo: effect to, theo, up

    long moneyOwe = totalMoneyBet - bot.getTotalMoneyBet();
    if (bot.getTotalMoney() <= moneyOwe) { // all-in
      totalMoney += bot.getTotalMoney();
      game.gamePlayUI.eftLbTotalMoney(bot.getTotalMoney());

      game.gamePlayUI.pAllIn.start(GStage.getWorldWidth()/2,
                                GStage.getWorldHeight()/2 - 50,
                                   Config.SCL_EFFECT_ALL_IN);
      bot.AllIn();

      System.out.println(bot.id + "  TO ALL-IN    MONEY  " + bot.getTotalMoney());
    }
    else {
      long tempMoneyBet = logic.rndMoneyTo(bot.getTotalMoney() - moneyOwe)/1000;
      tempMoneyBet = tempMoneyBet * 1000;

      System.out.println("CONVERT: " + tempMoneyBet);

      if (tempMoneyBet >= game.moneyBet) {
        totalMoneyBet += tempMoneyBet;
        totalMoney = totalMoney + tempMoneyBet + moneyOwe;
        bot.TO(tempMoneyBet, moneyOwe);

        game.gamePlayUI.eftLbTotalMoney(tempMoneyBet + moneyOwe);
        System.out.println(bot.id + "  MONEY  " + bot.getTotalMoney() + "   TO:  " + tempMoneyBet);

        idBotKeepMaxMoneyTo = bot.id;
        if (isResetBotStartBet) {
          logic.resetIsStartBetInBot(game.lsBotActive);
          isResetBotStartBet = false;
        }
      }
      else
        THEO(bot);
    }// to them
    bot.convertTotalMoneyToString();
    bot.eftChipOut(game);

  }

  public void TO(Bot player, long moneyBet) {

    long moneyOwe = totalMoneyBet - player.getTotalMoneyBet();
    System.out.println("MONEY OWE: " + moneyOwe);
    totalMoneyBet += moneyBet;
    totalMoney = totalMoney + moneyBet + moneyOwe;

    if (moneyBet == (player.getTotalMoney() - moneyOwe)) {
      game.gamePlayUI.pAllIn.start(GStage.getWorldWidth()/2,
                                GStage.getWorldHeight()/2 - 50,
                                   Config.SCL_EFFECT_ALL_IN);
      player.AllIn();
    }
    else
      player.TO(moneyBet, moneyOwe);
    player.convertTotalMoneyToString();

    game.gamePlayUI.eftLbTotalMoney(moneyBet + moneyOwe);
    idBotKeepMaxMoneyTo = player.id;
    if (isResetBotStartBet) {
      logic.resetIsStartBetInBot(game.lsBotActive);
      isResetBotStartBet = false;
    }

    player.eftChipOut(game);

  }

  public void THEO(Bot bot) {

    long moneyOwe = totalMoneyBet - bot.getTotalMoneyBet();
    if (bot.getTotalMoney() <= moneyOwe) { // all-in
      totalMoney += bot.getTotalMoney();
      game.gamePlayUI.eftLbTotalMoney(bot.getTotalMoney());

      if (bot.getTotalMoney() == 0)
        bot.THEO(0);
      else {
        game.gamePlayUI.pAllIn.start(GStage.getWorldWidth()/2,
                                  GStage.getWorldHeight()/2 - 50,
                                     Config.SCL_EFFECT_ALL_IN);
        bot.AllIn();
      }
    }
    else {
      totalMoney += moneyOwe;
      bot.THEO(moneyOwe);

      game.gamePlayUI.eftLbTotalMoney(moneyOwe);
    }
    bot.convertTotalMoneyToString();
    bot.eftChipOut(game);

    System.out.println(bot.id + "  THEO    MONEY    " + bot.getTotalMoney());

  }

  public void UP(Bot bot) {
    System.out.println(bot.id + "  UP");
    bot.UP();
  }

  private void rndBet(Bot bot) {

    int rnd = (int) Math.round(Math.random() * 10);
    if (bot.isStartBet) {
      if (rnd <= 5)
        THEO(bot);
      else
        TO(bot);
    }
    else if (!game.lsBotActive.get(0).isAlive()) {
      THEO(bot);
    }
    else {
      switch (bot.idRule) {

        case -1:
          TO(bot);
          break;
        case 0: case 1: case 2:

          if (rnd <= 5)
            UP(bot);
          else
            THEO(bot);

          break;
        case 3: case 4: case 5: case 6:

          if (rnd <= 2)
            UP(bot);
          else if (rnd <= 7)
            THEO(bot);
          else
            TO(bot);

          break;
        case 7: case 8: case 9:

          if (rnd <= 1)
            UP(bot);
          else if (rnd <= 7)
            THEO(bot);
          else
            TO(bot);
          break;
      }
    }

  }

  public void reset() {

    isMoneyBotZero = false;
    isResetBotStartBet = true;
    botPresent = null;
    totalMoney = 0;
    idBotKeepMaxMoneyTo = -1;
    turn = -1;
    game.gBot.clearActions();

  }

  public void setTotalMoneyBet(long money) {
    totalMoneyBet = money;
  }

  public long getTotalMoneyBet() {
    return totalMoneyBet;
  }

}
