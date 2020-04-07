package com.ss.gameLogic.logic;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.ss.core.action.exAction.GSimpleAction;
import com.ss.gameLogic.Game;
import com.ss.gameLogic.objects.Bot;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static com.badlogic.gdx.math.Interpolation.*;

public class Bet {

  private Logic logic = Logic.getInstance();
  private Game game;

  public long totalCoinBet = 0;
  private int turn = -1, idBotKeepMaxMoneyTo = -1;
  private Bot botPresent;

  public Bet(Game game) {
    this.game = game;
  }

  public void startBet(Bot bot) {

    botPresent = bot;
    game.gBot.addAction(GSimpleAction.simpleAction(this::actionBet));

  }

  private boolean actionBet(float dt, Actor a) {

    turn = game.lsBotActive.indexOf(botPresent) + 1;
    if (turn >= game.lsBotActive.size())
      turn = 0;

    //todo: if botPre is not alive => next turn

    game.gBot.addAction(sequence(
            run(() -> {
              if (botPresent.isAlive)
                rndBet(botPresent);
            }),
            delay(1f),
            run(() -> nextTurn(turn))
    ));

    return true;

  }

  public void nextTurn(int turn) {

    Bot botNext = game.lsBotActive.get(turn);

    System.out.println(turn);

    if (logic.countBotAlive(game.lsBotActive) == 1 || botNext.id == idBotKeepMaxMoneyTo) {
      //todo: finished bet
      System.out.println("FINISHED BET!");
    }
    else if (turn > 0)
      startBet(botNext);
    else {
      // TODO: 4/7/2020 turn player, show btn bet
    }

  }

  private void TO(Bot bot) {

    //todo: effect to, theo, up

    long moneyOwe = totalCoinBet - bot.totalMoneyBet;
    if (bot.totalMoney <= moneyOwe) {
      bot.totalMoneyBet += bot.totalMoney;
      bot.totalMoney = 0;
    } // all-in
    else {
      long tempMoneyBet = logic.rndMoneyTo(bot.totalMoney);
      bot.totalMoneyBet += tempMoneyBet;
      bot.totalMoney -= tempMoneyBet;
      totalCoinBet += tempMoneyBet;
    }// to them

  }

  private void THEO(Bot bot) {

    long moneyOwe = totalCoinBet - bot.totalMoneyBet;
    if (bot.totalMoney <= moneyOwe) {
      bot.totalMoneyBet += bot.totalMoney;
      bot.totalMoney = 0;
    }// all-in
    else {
      bot.totalMoneyBet += moneyOwe;
      bot.totalMoney -= moneyOwe;
    }

  }

  private void UP(Bot bot) {

    bot.isAlive = false;

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

    botPresent = null;
    totalCoinBet = 0;
    idBotKeepMaxMoneyTo = -1;

  }

}
