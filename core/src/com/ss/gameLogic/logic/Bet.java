package com.ss.gameLogic.logic;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.ss.core.action.exAction.GSimpleAction;
import com.ss.gameLogic.Game;
import com.ss.gameLogic.objects.Bot;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

public class Bet {

  private Logic logic = Logic.getInstance();
  private Game game;

  private long totalMoneyBet = 10000;
  public long totalMoney = 0; //money all player bet
  private int turn = -1, idBotKeepMaxMoneyTo = -1;
  private Bot botPresent;
  private boolean isResetBotStartBet = true;

  public Bet(Game game) {
    this.game = game;
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
                rndBet(botPresent);
              }
            }),
            delay(turn != 0 ? logic.timeDelayToNextTurnBet() : 0),
            run(() -> nextTurn(turn))
    ));

    return true;

  }

  private void nextTurn(int turn) {

    Bot botNext = game.lsBotActive.get(turn);
    if (logic.countBotAlive(game.lsBotActive) == 1) {
      //todo: finished bet
      Bot winner = game.getWinner();
      System.out.println("size lsBotActive == 1 " + winner.id);
    }
    else if (botNext.id == idBotKeepMaxMoneyTo || botNext.isStartBet) {
      //todo: find winner
      Bot winner = game.findWinner();
      System.out.println("Winner  " + winner.id);
    }
    else if (turn > 0)
      startBet(botNext);
    else if (!botNext.isAlive()) {
      //if player is "UP" => next turn (botNext is player)
      Bot tempBotNext = game.lsBotActive.get(1);
      if (tempBotNext.id == idBotKeepMaxMoneyTo || tempBotNext.isStartBet) {
        Bot winner = game.findWinner();
        System.out.println("FIND WINNER  " + winner.id);
      }
      else
        startBet(tempBotNext);
    }
    else {
      // todo: turn player, show btn bet
      System.out.println("TURN PLAYER ----------------------------");
    }

  }

  public void TO(Bot bot) {

    //todo: effect to, theo, up

    long moneyOwe = totalMoneyBet - bot.getTotalMoneyBet();
    if (bot.getTotalMoney() <= moneyOwe) { // all-in
      bot.AllIn();
      totalMoney += bot.getTotalMoney();

      game.gamePlayUI.eftLbTotalMoney(bot.getTotalMoney());
      System.out.println(bot.id + "  TO    MONEY  " + bot.getTotalMoney());
    }
    else {
      long tempMoneyBet = logic.rndMoneyTo(bot.getTotalMoney() - moneyOwe);
      totalMoneyBet += tempMoneyBet;
      totalMoney = totalMoney + tempMoneyBet + moneyOwe;
      bot.TO(tempMoneyBet + moneyOwe);

      game.gamePlayUI.eftLbTotalMoney(tempMoneyBet + moneyOwe);
      System.out.println(bot.id + "  TO    MONEY  " + bot.getTotalMoney() + "   TO:  " + tempMoneyBet);

      idBotKeepMaxMoneyTo = bot.id;
      if (isResetBotStartBet) {
        logic.resetIsStartBetInBot(game.lsBotActive);
        isResetBotStartBet = false;
      }
    }// to them

  }

  public void THEO(Bot bot) {

    long moneyOwe = totalMoneyBet - bot.getTotalMoneyBet();
    if (bot.getTotalMoney() <= moneyOwe) { // all-in
      bot.AllIn();
      totalMoney += bot.getTotalMoney();

      game.gamePlayUI.eftLbTotalMoney(bot.getTotalMoney());
    }
    else {
      bot.THEO(moneyOwe);
      totalMoney += moneyOwe;

      game.gamePlayUI.eftLbTotalMoney(moneyOwe);
    }

    System.out.println(bot.id + "  THEO    MONEY    " + bot.getTotalMoney());

  }

  public void UP(Bot bot) {
    System.out.println(bot.id + "  UP");
    bot.UP();
  }

  private void rndBet(Bot bot) {

    int rnd = (int) Math.round(Math.random() * 10);
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






  public void reset() {

    isResetBotStartBet = true;
    botPresent = null;
    totalMoney = 0;
    idBotKeepMaxMoneyTo = -1;

  }

  public void setTotalMoneyBet(long money) {
    totalMoneyBet = money;
  }

}
