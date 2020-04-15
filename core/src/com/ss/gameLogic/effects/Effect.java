package com.ss.gameLogic.effects;

import static com.badlogic.gdx.math.Interpolation.*;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.ss.core.action.exAction.GTemporalAction;
import com.ss.gameLogic.Game;
import com.ss.gameLogic.config.Config;
import com.ss.gameLogic.logic.Logic;
import com.ss.gameLogic.objects.Bot;
import com.ss.gameLogic.objects.Button;
import com.ss.gameLogic.objects.Card;
import com.ss.gameLogic.objects.Chip;

import static com.ss.gameLogic.config.Config.*;

import java.util.Collections;
import java.util.List;

public class Effect {

  private Logic logic = Logic.getInstance();
  private static Effect instance = null;
  private Game game;

  private Effect(Game game) {
    this.game = game;
  }

  public static Effect getInstance(Game game) {
    return instance == null ? instance = new Effect(game) : instance;
  }

  public void divide(Image card, float x, float y) {

    card.addAction(
            parallel(
                    moveTo(x, y, DUR_DIVIDE_CARD, fastSlow),
                    scaleTo(.7f, .7f, DUR_DIVIDE_CARD, fastSlow),
                    rotateTo(logic.rndRotate(), DUR_DIVIDE_CARD, linear)
            )
    );

  }

  public void formatCardDown(List<Bot> lsBot) {

    for (Bot bot : lsBot) {
      int indexBot = lsBot.indexOf(bot);

      for (Card cardDown : bot.lsCardDown) {
        int index = bot.lsCardDown.indexOf(cardDown);
        Card cardUp = bot.lsCardUp.get(index);
        rotateAndSpreadCard(cardDown, cardUp, index, indexBot);
      }
    }

  }

  public void myTurn(Bot bot) {
    if (bot.isAlive())
      bot.avatar.addAction(
              sequence(
                      scaleTo(-1f, 1, .25f, fastSlow),
                      scaleTo(1f, 1, .25f, fastSlow)
              )
      );
  }

  private void rotateAndSpreadCard(Card cardDown, Card cardUp, int indexCard, int indexBot) {

    Runnable run = () -> {
      cardUp.setPosition(cardDown.getCard().getX(), cardDown.getCard().getY());
      cardUp.addCardToScene(game.gCard);
      cardDown.setZindex(1000);

      if (indexBot == 0) {
        cardDown.setiClickCard(game.gamePlayUI);
        cardDown.addListener(cardUp);
        cardUp.setScale(0, 1.8f);
      }
      else
        cardUp.setScale(0, SCL_CARD_INIT);

      cardUp.setRotate(logic.getDegreeBuyIndex(indexCard));

    };

    ParallelAction par;
    if (indexBot == 0)
      par = parallel(
              scaleTo(1.8f, 1.8f, DUR_SCL_ROTATE_CARD, fastSlow),
              sequence(
                      rotateTo(logic.getDegreeBuyIndex(indexCard), DUR_SCL_ROTATE_CARD, fastSlow),
                      moveBy(60*indexCard, 0, DUR_SPREAD_CARD, fastSlow),
                      run(run)
              ));
    else
      par = sequence(
              rotateTo(logic.getDegreeBuyIndex(indexCard), DUR_SCL_ROTATE_CARD, fastSlow),
              moveBy(28*indexCard, 0, DUR_SPREAD_CARD, fastSlow),
              run(run)
      );


    cardDown.addAction(par);

  }

  public void winnerIsBot(Bot bot) {

    float x = bot.avatar.getX() + bot.avatar.getWidth()/2;
    float y = bot.avatar.getY() + bot.avatar.getHeight()/2 - 70;

    for (Card cardDown : bot.lsCardDown) {
      int index = bot.lsCardDown.size() - 1 - bot.lsCardDown.indexOf(cardDown);
      Card cardUp = bot.lsCardUp.get(bot.lsCardDown.indexOf(cardDown));

      cardDown.addAction(
              sequence(
                      parallel(
                              scaleTo(1f, 1f, .5f, fastSlow),
                              moveTo(x - cardDown.getWidth()/2 - index*28 + 30, y - 10, .5f, fastSlow)
                      ),
                      run(() -> cardUp.setPosition(cardDown.getX(), cardDown.getY())),
                      run(() -> showAllCard(cardDown, cardUp, 1f, 1f)),
                      run(() -> {
                        int i = bot.lsCardDown.indexOf(cardDown);
                        if (i == bot.lsCardDown.size() - 1)
                          bot.eftMoneyWinner(game, game.bet.totalMoney);
                      })
              )
      );
    }

  }

  public void flipCard(Card cardDown, Card cardUp) {

    cardDown.addAction(sequence(
            scaleTo(0, 1.8f, DUR_SCL_CARD_PLAYER, linear),
            run(() -> cardUp.addAction(scaleTo(1.8f, 1.8f, DUR_SCL_CARD_PLAYER, linear)))
    ));

  }

  public void showAllCard(Card cardDown, Card cardUp, float sclX, float sclY) {

    cardDown.addAction(sequence(
            scaleTo(0, sclY, DUR_SCL_CARD_PLAYER, linear),
            run(() -> cardUp.addAction(scaleTo(sclX, sclY, DUR_SCL_CARD_PLAYER, linear)))
    ));

  }

  public void moneyChange(Label lb) {

    SequenceAction seq = sequence(
            parallel(
                    moveBy(0, -100f, 5f, linear),
                    alpha(0f, 10f, linear)
            ),
            run(() -> {
              lb.setVisible(false);
              lb.getColor().a = 1f;
              lb.moveBy(0, 100f);
            })
    );

    lb.clearActions();
    lb.addAction(seq);

  }

  public void alphaLabel(Label lb) {

    SequenceAction seq = sequence(
            parallel(
                    alpha(0f, 1.5f, linear),
                    moveBy(0, -100, 1.5f, linear)
            ),
            run(lb::remove)
    );

    lb.clearActions();
    lb.getColor().a = 1f;
    lb.addAction(seq);

  }

  public void click(Button btn, Runnable onComplete) {

    btn.getGroup().addAction(sequence(
            scaleTo(.9f, .9f, .05f, fastSlow),
            scaleTo(1f, 1f, .05f, fastSlow),
            run(onComplete)
    ));

  }

  public void click(Image btn, Runnable onComplete) {

    btn.addAction(sequence(
            scaleTo(.9f, .9f, .05f, fastSlow),
            scaleTo(1f, 1f, .05f, fastSlow),
            run(onComplete)
    ));

  }

  public void moveCardResidual(Card card, int index, Runnable onComplete) {
    card.getCard().addAction(
            parallel(
                    rotateTo(360, .15f, fastSlow),
                    moveTo(POS_DESK_RESIDUAL.x + index/2, POS_DESK_RESIDUAL.y + index/2, .15f, fastSlow),
                    run(onComplete)
            )
    );
  }

  public void moveCardResidual(Card card, int index) {
    card.getCard().addAction(
            parallel(
                    moveTo(POS_DESK_RESIDUAL.x + index/2, POS_DESK_RESIDUAL.y + index/2, .65f, fastSlow),
                    rotateTo(-360, .65f, fastSlow)
            )
    );
  }

  public void raiseMoney(Label lbMoney, long money) {

    final long tempMoney = game.bet.totalMoney - money;
    lbMoney.addAction(GTemporalAction.add(.5f, (dt,a) -> {

      long temp = (long) (tempMoney + money*dt);
      lbMoney.setText(logic.convertMoneyBet(temp));

    }));

  }

  public void lightBtn(Image light) {

    SequenceAction seq = sequence(
            alpha(1f, .75f, linear),
            alpha(0f, .75f, linear),
//            alpha(1f, .1f, linear),
//            alpha(0f, .1f, linear),
            delay(.25f),
            run(() -> lightBtn(light))
    );

    light.getColor().a = 0;
    light.addAction(seq);

  }

  public void chipOut(List<Chip> lsChip) {

    for (Chip chip : lsChip) {

      float rndX = Math.round(Math.random() * 100);
      float rndY = Math.round(Math.random() * 100);
      float rndPlusOrMinus = Math.round(Math.random() * 1);

      if (rndPlusOrMinus == 0) {
        rndX *= -1;
        rndY *= -1;
      }

      chip.addToScene(game.gChip);
      chip.addAction(
              moveTo(CENTER_X + rndX, CENTER_Y + rndY, .5f, smooth)
      );
    }

    game.gamePlayUI.lsAllChip.addAll(lsChip);
    lsChip.clear();

  }

  public void arrangeLsChip(List<Chip> lsChip, Bot winner) {

    Collections.sort(lsChip, (c1, c2) -> c1.idChip - c2.idChip);
    for (Chip chip : lsChip) {
      int index = lsChip.indexOf(chip);
      chip.setZindex(1000);
      chip.addAction(
              sequence(
                      moveTo(CENTER_X, CENTER_Y - index*5, .75f, fastSlow),
                      run(() -> {
                        if (index == lsChip.size()-1)
                          moveChipToWinner(lsChip, winner, 0);
                      })
              )
      );
    }

  }

  private void moveChipToWinner(List<Chip> lsChip, Bot winner, int turn) {

    if (turn < lsChip.size()) {
      Chip chip = lsChip.get(turn);
      chip.addAction(
              parallel(
                      moveTo(winner.posChipReceive.x, winner.posChipReceive.y - turn*5, .35f, fastSlow),
                      sequence(
                              delay(.025f),
                              run(() -> moveChipToWinner(lsChip, winner, turn+1)),
                              run(() -> {
                                if (turn == lsChip.size()-1)
                                  game.gamePlayUI.showBtnNewRound();
                              })
                      )
              )
      );
    }

  }

  public void sclMinToMax(Group group) {
    group.addAction(
            parallel(
                    scaleTo(1f, 1f, .5f, linear),
                    rotateTo(360, .5f, linear)
            )
    );
  }

  public void sclMaxToMin(Group group, Runnable onComplete) {
    group.addAction(
            sequence(
                    parallel(
                            scaleTo(0f, 0f, .25f, linear),
                            rotateTo(-360, .25f, linear)
                    ),
                    run(onComplete)
            )
    );
  }

}
