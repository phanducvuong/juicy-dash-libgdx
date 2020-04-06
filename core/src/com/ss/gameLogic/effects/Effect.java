package com.ss.gameLogic.effects;

import static com.badlogic.gdx.math.Interpolation.*;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import com.badlogic.gdx.scenes.scene2d.actions.ParallelAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.ss.gameLogic.Game;
import com.ss.gameLogic.objects.Bot;
import com.ss.gameLogic.objects.Card;
import static com.ss.gameLogic.config.Config.*;

import java.util.List;

public class Effect {

  private static Effect instance = null;
  private Game game;

  private Effect(Game game) {
    this.game = game;
  }

  public static Effect getInstance(Game game) {
    return instance == null ? instance = new Effect(game) : instance;
  }

  public void moveCardTo(Image card, float x, float y) {

    card.addAction(moveTo(x, y, DUR_DIVIDE_CARD, fastSlow));

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

    };

    ParallelAction par;
    if (indexBot == 0)
      par = parallel(
              scaleTo(1.8f, 1.8f, DUR_SCL_ROTATE_CARD, fastSlow),
              sequence(
                      rotateTo(0, DUR_SCL_ROTATE_CARD, fastSlow),
                      moveBy(120*indexCard, 0, DUR_SPREAD_CARD, fastSlow),
                      run(run)
              ));
    else
      par = sequence(
              rotateTo(0, DUR_SCL_ROTATE_CARD, fastSlow),
              moveBy(50*indexCard, 0, DUR_SPREAD_CARD, fastSlow),
              run(run)
      );


    cardDown.addAction(par);

  }

  public void flipCard(Card cardDown, Card cardUp) {

    cardDown.addAction(sequence(
            scaleTo(0, 1.8f, DUR_SCL_CARD_PLAYER, linear),
            run(() -> cardUp.addAction(scaleTo(1.8f, 1.8f, DUR_SCL_CARD_PLAYER, linear)))
    ));

  }

  public void showAllCard(Card cardDown, Card cardUp) {

    cardDown.addAction(sequence(
            scaleTo(0, SCL_CARD_INIT, DUR_SCL_CARD_PLAYER, linear),
            run(() -> cardUp.addAction(scaleTo(SCL_CARD_INIT, SCL_CARD_INIT, DUR_SCL_CARD_PLAYER, linear)))
    ));

  }

}
