package com.ss.gameLogic.logic;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.ss.core.action.exAction.GSimpleAction;
import com.ss.core.effect.SoundEffects;
import com.ss.core.util.GStage;
import com.ss.gameLogic.Game;
import com.ss.gameLogic.config.Config;
import com.ss.gameLogic.effects.Effect;
import com.ss.gameLogic.objects.Bot;
import com.ss.gameLogic.objects.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.delay;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.run;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.sequence;

public class DivideCard {

  private Game game;
  private Logic logic = Logic.getInstance();
  private Rule rule = Rule.getInstance();
  private Effect effect;

  private HashMap<String, List<List<Card>>> hmSpecialCardBot, hmSpecialCardPlayer;

  private int turn = 0, turnCardDown = -1, countTurn = -1; //reset when new game
  private Bot botPresent;

  public DivideCard(Game game) {

    this.game = game;
    this.hmSpecialCardBot = new HashMap<>();
    this.hmSpecialCardPlayer = new HashMap<>();
    effect = Effect.getInstance(game);

    loadSpecialCard();

  }

  private void loadSpecialCard() {

    FileHandle fhSpecialCardBot = Gdx.files.internal("special_deck_bot.json");
    FileHandle fhSpecialCardPlayer = Gdx.files.internal("special_deck_player.json");

    initLsSpecialDesk(hmSpecialCardBot, fhSpecialCardBot);
    initLsSpecialDesk(hmSpecialCardPlayer, fhSpecialCardPlayer);

    //label: test
//    Card card = hmSpecialCardPlayer.get("sap").get(0).get(0);
//    System.out.println(card.getName());

  }

  private void startDivide(Card cardDown) {

    countTurn++; // count number of cardDown in each player (maximum is 3)
    if (countTurn < game.numOfPlayer*3) {
      botPresent.lsCardDown.add(cardDown);

      cardDown.setActive(true);
//      Card cardUp = game.lsCardUp.get(turnCardDown);
//      botPresent.lsCardUp.add(cardUp);

      cardDown.getCard().addAction(GSimpleAction.simpleAction(this::divide));
    }
    else {

      moveCardResidual();
      game.gCard.addAction(sequence(
              delay(.25f),
              run(() -> {
                effect.formatCardDown(game.lsBotActive);
                logic.findIdRuleOfLsBot(game.lsBotActive);
              }),
              delay(1f),
              run(() -> game.startBet())
      ));

    }

  }

  private boolean divide(float dt, Actor a) {

    turn += 1;
    Image card = (Image) a;
    card.setZIndex(1000);
    Vector2 v = logic.getPosByIdBot(botPresent.id);

    SoundEffects.startSound("divide_card");

    effect.divide(card, v.x, v.y);
    game.gCard.addAction(sequence(
            delay(.1f),
            run(this::nextTurn)
    ));

    return true;
  }

  public void chkTimePlayerWinInGame(List<Card> lsCardUp) {

    if (game.countPlayWinInGame >= Config.TIME_PLAYER_WIN_IN_GAME) {

      String key = logic.getKeySpecialDeck(hmSpecialCardBot);
      int indexOfLsCard = logic.getIndexOfSpecialDeck(hmSpecialCardBot.get(key));

      Bot bot = logic.getBotRnd(game.lsBot);
      bot.lsCardUp.addAll(hmSpecialCardBot.get(key).get(indexOfLsCard));

      Bot player = game.lsBotActive.get(0);
      player.lsCardUp.addAll(hmSpecialCardPlayer.get(key).get(indexOfLsCard));

      game.countPlayWinInGame = 0;
    }
    else
      setCardUpForBot(lsCardUp);

  }

  private void initLsSpecialDesk(HashMap<String, List<List<Card>>> hmDeck, FileHandle fileHandle) {

    JsonReader jsonReader = new JsonReader();
    JsonValue jsonValue = jsonReader.parse(fileHandle.readString());

    for (JsonValue value : jsonValue) {

      List<List<Card>> lsTemp = new ArrayList<>();
      for (JsonValue j : value) {

        List<Card> temp = new ArrayList<>();
        for (int i=0; i<j.size; i++)
          temp.add(logic.getCardByName(j.get(0).asString(), game.lsCardUp));
        lsTemp.add(temp);

      }

      hmDeck.put(value.name, lsTemp);

    }

  }

  private void setCardUpForBot(List<Card> lsCard) {

    Collections.shuffle(lsCard, new Random());
    int count = 0;
    for (Bot bot : game.lsBotActive) {
      bot.lsCardUp.add(lsCard.get(count));
      bot.lsCardUp.add(lsCard.get(count+1));
      bot.lsCardUp.add(lsCard.get(count+2));

      count += 3;
    }

  }

  public void nextTurn() {

//    turn++;
    turnCardDown++;
    if (turn >= game.numOfPlayer)
      turn = 0;
    if (game.lsBotActive.size() > 0) {
      botPresent = game.lsBotActive.get(turn);
      startDivide(game.lsCardDown.get(turnCardDown));
    }

  }

  private void resetDesk() {

    for (int i = game.lsCardDown.size()-1; i>=0; i--) {
      Card cardDown = game.lsCardDown.get(i);
      int offset = (game.lsCardDown.size() - i)/2;
      cardDown.setPosition(GStage.getWorldWidth()/2 - cardDown.getWidth()/2 - offset,
                            GStage.getWorldHeight()/2 - cardDown.getHeight()/2 + 20 - offset);
      cardDown.addCardToScene(game.gCard);
    }

  }

  private void moveCardResidual() {

    for (int i=game.lsCardDown.size()-1; i>=0; i--) {
      Card card = game.lsCardDown.get(i);
      if (!card.isActive())
        effect.moveCardResidual(card, i);
    }

  }

  private void shuffleLsCardUp() {
    Collections.shuffle(game.lsCardUp, new Random());
  }

  public void reset() {

    turn = 0;
    turnCardDown = -1;
    countTurn = -1;
    botPresent = null;

    for (int i=0; i<game.lsCardDown.size(); i++) {
      game.lsCardDown.get(i).reset();
      game.lsCardUp.get(i).reset();
    }

    resetDesk();
//    shuffleLsCardUp();

  }

  public void setTurn(int turn) {
    this.turn = turn;
  }

}
