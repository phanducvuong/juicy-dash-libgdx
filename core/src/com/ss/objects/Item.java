package com.ss.objects;

import static com.badlogic.gdx.math.Interpolation.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Align;
import com.ss.GMain;
import com.ss.config.Type;
import com.ss.core.effect.SoundEffects;
import com.ss.core.util.GUI;
import com.ss.gameLogic.effects.Particle;
import com.ss.ui.GamePlayUI;
import com.ss.utils.Util;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static com.ss.config.Config.*;

public class Item extends Group {

  private Group     gLbScore;

  private Image     fruit, animFruitL, animFruitR;
  private Image     flare, glassL1, glassL2, glassR1, glassR2;
  public  Image     ice, iAnimClock;
  private Label     lbScore;

  private Group     gAnimFruit;
  public  Type      type;
  public  String    name;
  public  boolean   isAlive                  = false,
                    isStopAnimZoomAndVibrate = true;
  private float     timeAnimZoomAndVibrate   = 0;

  public Item(String region, Type type, Group gLbScore) {

    this.gLbScore     = gLbScore;
    this.name         = region;
    this.type         = type;

    fruit = GUI.createImage(GMain.itemAtlas, region);
    setSize(fruit.getWidth(), fruit.getHeight());
    this.setOrigin(Align.center);
    this.addActor(fruit);

    createLbScore();
    if (chkRegion(type)) {
      createAnimFruit();
      createAnimIce();
    }
    else
      createAnimSpecialItem();

  }

  private void createLbScore() {
    lbScore = new Label("+" + SCORE_FRUIT, new Label.LabelStyle(whiteFont, null));
    lbScore.setAlignment(Align.center);

    if (type == Type.glass_fruit)
      lbScore.setText("+" + SCORE_GLASS_JUICE);
    else if (type == Type.jam)
      lbScore.setText("+" + SCORE_JAM);
    else if (type == Type.clock)
      lbScore.setText("+" + SCORE_CLOCK);

  }

  private void createAnimFruit() {
    gAnimFruit = new Group();
    animFruitL = GUI.createImage(GMain.itemAtlas, type.name()+"_l");
    animFruitR = GUI.createImage(GMain.itemAtlas, type.name()+"_r");

    if (animFruitL.getWidth() >= animFruitR.getWidth())
      gAnimFruit.setWidth(animFruitL.getWidth());
    else
      gAnimFruit.setWidth(animFruitR.getWidth());

    if (animFruitL.getHeight() >= animFruitR.getHeight())
      gAnimFruit.setHeight(animFruitL.getHeight());
    else
      gAnimFruit.setHeight(animFruitR.getHeight());

    animFruitL.setOrigin(Align.center);
    animFruitR.setOrigin(Align.center);

    gAnimFruit.addActor(animFruitR);
    gAnimFruit.addActor(animFruitL);
    setPosAnimFruit();
  }

  private void createAnimIce() {
    ice = GUI.createImage(GMain.itemAtlas, "ice_" + type.name());
    ice.getColor().a = 0f;
    ice.setPosition(fruit.getX() + fruit.getWidth()/2 - ice.getWidth()/2,
                         fruit.getY() + fruit.getHeight()/2 - ice.getHeight()/2);

  }

  private void createAnimSpecialItem() {

    flare = GUI.createImage(GMain.itemAtlas, "flare");
    flare.setPosition(fruit.getX() + fruit.getWidth()/2 - flare.getWidth()/2,
            fruit.getY() + fruit.getHeight()/2 - flare.getHeight()/2);
    flare.setOrigin(Align.center);
    this.addActor(flare);
    fruit.setZIndex(1000);
    animFlare();

    switch (type) {
      case glass_fruit:
        glassL1 = GUI.createImage(GMain.itemAtlas, "anim_glass_juice");
        glassR1 = GUI.createImage(GMain.itemAtlas, "anim_glass_juice");

        glassL2 = GUI.createImage(GMain.itemAtlas, "anim_glass_juice");
        glassR2 = GUI.createImage(GMain.itemAtlas, "anim_glass_juice");

        glassL1.setOrigin(Align.center);
        glassR1.setOrigin(Align.center);
        glassL2.setOrigin(Align.center);
        glassR2.setOrigin(Align.center);
        break;
      case clock:
        iAnimClock = GUI.createImage(GMain.itemAtlas, "item_clock");
        iAnimClock.setScale(.4f);
        break;
    }
  }

  public void setPosition(Vector2 pos) {
    float x = pos.x + WIDTH_PIECE/2;
    float y = pos.y + HEIGHT_PIECE/2;
    this.setPosition(x, y, Align.center);
  }

  public void setPosStart(Vector2 pos) {
    float x = pos.x + WIDTH_PIECE/2;
    float y = -HEIGHT_PIECE - 20;
    this.setPosition(x, y, Align.center);
  }

  public void moveToPos(Vector2 pos, float duration) {
    this.addAction(
            sequence(
                    Actions.moveBy(0, -2, .1f, linear),
                    moveTo(pos.x, pos.y, duration, linear),
                    run(this::anim0)
            )
    );
  }

  public Vector2 getPos() {
    return new Vector2(this.getX(), this.getY());
  }

  private boolean chkRegion(Type type) {
    return  type != Type.glass_fruit &&
            type != Type.jam         &&
            type != Type.clock       &&
            type != Type.walnut;
  }

  private void setPosAnimFruit() {
    switch (name) {
      case "item_apple":
        animFruitL.setPosition(-3, -10);
        animFruitR.setPosition(0, 7);
        break;
      case "item_strawberry":
        animFruitL.setPosition(0, -17);
        animFruitR.setPosition(-2, 7);
        break;
      case "item_orange":
        animFruitR.setZIndex(1000);
        animFruitR.setPosition(0, 20);
        animFruitL.setPosition(-1, -6);
        break;
      case "item_grape":
        animFruitL.setPosition(-10, 0);
        animFruitR.setPosition(5, 40);
        break;
      case "item_kiwi":
        animFruitR.setZIndex(1000);
        animFruitR.setPosition(-2, 16);
        animFruitL.setPosition(-15, 0);
        break;
      case "item_banana":
        animFruitL.setPosition(42, -3);
        animFruitR.setPosition(0, 20);
        break;
    }
  }

  //label: anim fruit
  public void startAnimFruit(boolean isSpecialItem) {
    if (!isSpecialItem)
      SoundEffects.start(Util.inst().rndSoundChew(), CHEW_VOLUME);

    animLbScore();
    fruit.setVisible(false);
    this.addActor(gAnimFruit);
    rndAnimFruit();
  }

  public void animLvSuccess() {
    fruit.setVisible(false);
    this.addActor(gAnimFruit);
    rndAnimFruit();
  }

  private void rndAnimFruit() {
    int rnd = (int) Math.round(Math.random() * 3) + 1;
    switch (rnd) {
      case 1: animFruit1(); break;
      case 2: animFruit2(); break;
      case 3: animFruit3(); break;
      case 4: animFruit4(); break;
    }
  }

  //label: anim normal item
  public void anim0() {
    fruit.setOrigin(Align.topLeft);
    fruit.addAction(
            sequence(
                    Actions.scaleBy(0, -.1f, .15f, fastSlow),
                    Actions.scaleBy(0, .1f, .15f, fastSlow)
            )
    );
  }

  private void animFruit1() {

    Runnable reset = () -> {
      animFruitL.moveBy(25, 7);
      animFruitL.moveBy(0, -70);
      animFruitL.setRotation(0);
      animFruitL.getColor().a = 1f;

      animFruitR.moveBy(-25, 7);
      animFruitR.moveBy(0, -70);
      animFruitR.setRotation(0);
      animFruitR.getColor().a = 1f;

      fruit.setVisible(true);
      isAlive = false;
      gAnimFruit.remove();
      this.remove();
    };

    SequenceAction seqL = sequence(
            Actions.moveBy(0, -7, .1f, linear),
            parallel(
                    Actions.moveBy(-25, 0, .25f, linear),
                    rotateTo(270, .65f, linear),
                    Actions.moveBy(0, 70, .5f, linear),
                    alpha(0f, .5f)
            )
    );

    SequenceAction seqR = sequence(
            Actions.moveBy(0, -7, .1f, linear),
            parallel(
                    Actions.moveBy(25, 0, .25f, linear),
                    rotateTo(-270, .65f, linear),
                    Actions.moveBy(0, 70, .5f, linear),
                    alpha(0f, .5f)
            ),
            run(reset)
    );

    animFruitL.addAction(seqL);
    animFruitR.addAction(seqR);

  }

  private void animFruit2() {

    Runnable reset = () -> {
      animFruitL.moveBy(-100, -110);
      animFruitL.setRotation(0);
      animFruitL.getColor().a = 1f;

      animFruitR.moveBy(110, -70);
      animFruitR.setRotation(0);
      animFruitR.getColor().a = 1f;

      fruit.setVisible(true);
      isAlive = false;
      gAnimFruit.remove();
      this.remove();
    };

    SequenceAction seqL = sequence(
            sequence(
                    Actions.moveBy(50, -40, .25f, linear),
                    parallel(
                            Actions.moveBy(50, 150, 1f, linear),
                            rotateTo(180, 1f, linear),
                            alpha(0f, .45f, linear)
                    )
            ),
            run(reset)
    );

    SequenceAction seqR = sequence(
            sequence(
                    Actions.moveBy(-20, -10, .135f, linear),
                    parallel(
                            Actions.moveBy(-90, 80, .5f, linear),
                            rotateTo(180, .75f, linear),
                            alpha(0f, .5f, linear)
                    )
            )
    );

    animFruitL.addAction(seqL);
    animFruitR.addAction(seqR);

  }

  private void animFruit3() {

    Runnable reset = () -> {
      animFruitL.moveBy(-45, -90);
      animFruitL.setRotation(0);
      animFruitL.getColor().a = 1f;

      animFruitR.moveBy(45, -90);
      animFruitR.setRotation(0);
      animFruitR.getColor().a = 1f;

      fruit.setVisible(true);
      isAlive = false;
      gAnimFruit.remove();
      this.remove();
    };

    SequenceAction seqL = sequence(
            Actions.moveBy(15, -10, .1f, linear),
            parallel(
                    Actions.moveBy(30, 100, .75f, linear),
                    rotateTo(-180, .75f, linear),
                    alpha(0f, .65f, linear)
            ),
            run(reset)
    );

    SequenceAction seqR = sequence(
            Actions.moveBy(-15, -10, .1f, linear),
            parallel(
                    Actions.moveBy(-30, 100, .75f, linear),
                    rotateTo(-180, .75f, linear),
                    alpha(0f, .65f, linear)
            )
    );

    animFruitL.addAction(seqL);
    animFruitR.addAction(seqR);

  }

  private void animFruit4() {

    Runnable reset = () -> {
      animFruitL.moveBy(-80, -100);
      animFruitL.setRotation(0);
      animFruitL.getColor().a = 1f;

      animFruitR.moveBy(130, -132);
      animFruitR.setRotation(0);
      animFruitR.getColor().a = 1f;

      fruit.setVisible(true);
      isAlive = false;
      gAnimFruit.remove();
      this.remove();
    };

    SequenceAction seqL = sequence(
            parallel(
                    Actions.moveBy(80, 100, .75f, linear),
                    rotateTo(90, .75f, linear),
                    alpha(0f, .65f, linear)
            )
    );

    SequenceAction seqR = sequence(
            parallel(
                    Actions.moveBy(-30, -18, .25f, linear),
                    rotateTo(-50, .25f, linear)
            ),
            parallel(
                    Actions.moveBy(-100, 150, 1f, linear),
                    rotateTo(-180, .75f, linear),
                    alpha(0f, .65f, linear)
            ),
            run(reset)
    );

    animFruitL.addAction(seqL);
    animFruitR.addAction(seqR);

  }

  private void animFlare() {
    flare.addAction(
            sequence(
                    Actions.rotateBy(-10, .25f, linear),
                    run(this::animFlare)
            )
    );
  }

  //label: anim ice
  public void addAnimIce(Particle pIce) {
    this.addActor(ice);
    ice.addAction(
            sequence(
                    alpha(1f, 1f, linear),
                    run(() -> {
                      SoundEffects.start("ice_explode", ICE_EXPLODE_VOLUME);
                      this.addActor(gAnimFruit);
                      startAnimFruit(true);
                      resetAnimIce();
                      if (pIce != null) {
                        SoundEffects.start("glass_juice", GLASS_JUICE_VOLUME);
                        pIce.start(this.getX() + this.getWidth()/2,
                                   this.getY() + this.getHeight()/2, 1f);
                      }
                    })
            )
    );

    fruit.addAction(alpha(0f, 1f, linear));
  }

  private void resetAnimIce() {
    fruit.getColor().a = 1f;
    ice.getColor().a = 0f;
    ice.remove();
  }

  //label: anim glass juice
  public void animGlassJuice(boolean hor, Runnable onUpdateBoard) {

    Runnable reset = () -> {
      isAlive = false;
      glassL1.setRotation(0);
      glassR1.setRotation(0);
      glassL2.setRotation(0);
      glassR2.setRotation(0);

      glassL1.remove();
      glassR1.remove();
      glassL2.remove();
      glassR2.remove();

      fruit.setVisible(true);
      flare.setVisible(true);
      this.remove();
    };

    if (hor) {
      float moveLeft = this.getX() - GamePlayUI.bgTable.getX();
      float moveRight   = GamePlayUI.bgTable.getX() + GamePlayUI.bgTable.getWidth() - this.getX() - glassR1.getWidth();
      glassL1.addAction(
              sequence(
                      delay(1f),
                      run(() -> {
                        fruit.setVisible(false);
                        flare.setVisible(false);
                      }),
                      parallel(
                              moveTo(-moveLeft, glassL1.getY(), .25f, slowFast),
                              run(() -> glassL1.getColor().a = 1f)
                      )
              )
      );
      glassR1.addAction(
              sequence(
                      delay(1f),
                      run(() -> {
                        fruit.setVisible(false);
                        flare.setVisible(false);
                      }),
                      parallel(
                              moveTo(moveRight, glassR1.getY(), .25f, slowFast),
                              run(() -> glassR1.getColor().a = 1f)
                      ),
                      parallel(
                              run(reset),
                              run(onUpdateBoard)
                      )
              )
      );

      float moveDown = this.getY() - GamePlayUI.bgTable.getY();
      float moveUp   = GamePlayUI.bgTable.getY() + GamePlayUI.bgTable.getHeight() - this.getY() - glassR2.getHeight();
      glassL2.addAction(
              sequence(
                      delay(1f),
                      run(() -> {
                        fruit.setVisible(false);
                        flare.setVisible(false);
                      }),
                      parallel(
                              moveTo(glassL2.getX(), -moveDown, .25f, slowFast),
                              run(() -> glassL2.getColor().a = 1f)
                      )
              )
      );
      glassR2.addAction(
              sequence(
                      delay(1f),
                      run(() -> {
                        fruit.setVisible(false);
                        flare.setVisible(false);
                      }),
                      parallel(
                              moveTo(glassR2.getX(), moveUp, .25f, slowFast),
                              run(() -> glassR2.getColor().a = 1f)
                      )
              )
      );
    }
    else {
      float moveDown = this.getY() - GamePlayUI.bgTable.getY();
      float moveUp   = GamePlayUI.bgTable.getY() + GamePlayUI.bgTable.getHeight() - this.getY() - glassR1.getHeight();
      glassL1.addAction(
              sequence(
                      delay(1f),
                      run(() -> {
                        fruit.setVisible(false);
                        flare.setVisible(false);
                      }),
                      parallel(
                              moveTo(glassL1.getX(), -moveDown, .25f, slowFast),
                              run(() -> glassL1.getColor().a = 1f)
                      )
              )
      );
      glassR1.addAction(
              sequence(
                      delay(1f),
                      run(() -> {
                        fruit.setVisible(false);
                        flare.setVisible(false);
                      }),
                      parallel(
                              moveTo(glassR1.getX(), moveUp, .25f, slowFast),
                              run(() -> glassR1.getColor().a = 1f)
                      ),
                      parallel(
                              run(reset),
                              run(onUpdateBoard)
                      )
              )
      );

      float moveLeft    = this.getX() - GamePlayUI.bgTable.getX();
      float moveRight   = GamePlayUI.bgTable.getX() + GamePlayUI.bgTable.getWidth() - this.getX() - glassR2.getWidth();
      glassL2.addAction(
              sequence(
                      delay(1f),
                      run(() -> {
                        fruit.setVisible(false);
                        flare.setVisible(false);
                      }),
                      parallel(
                              moveTo(-moveLeft, glassL2.getY(), .25f, slowFast),
                              run(() -> glassL2.getColor().a = 1f)
                      )
              )
      );
      glassR2.addAction(
              sequence(
                      delay(1f),
                      run(() -> {
                        fruit.setVisible(false);
                        flare.setVisible(false);
                      }),
                      parallel(
                              moveTo(moveRight, glassR2.getY(), .25f, slowFast),
                              run(() -> glassR2.getColor().a = 1f)
                      )
              )
      );
    }
  }

  public void setPosAnimGlassJuice(boolean hor) {
    if (hor) {
      glassL1.setRotation(180);

      glassL1.setPosition(fruit.getX() + fruit.getWidth()/2 - glassL1.getWidth()/2 - 20,
              fruit.getY() + fruit.getHeight()/2 - glassL1.getHeight()/2);
      glassR1.setPosition(fruit.getX() + fruit.getWidth()/2 - glassL1.getWidth()/2 + 20,
              fruit.getY() + fruit.getHeight()/2 - glassL1.getHeight()/2);

      glassL2.setRotation(-90);
      glassR2.setRotation(90);

      glassL2.setPosition(fruit.getX() + fruit.getWidth()/2 - glassL2.getWidth()/2,
              fruit.getY() + fruit.getHeight()/2 - glassL2.getHeight()/2 - 20);
      glassR2.setPosition(fruit.getX() + fruit.getWidth()/2 - glassL2.getWidth()/2,
              fruit.getY() + fruit.getHeight()/2 - glassL2.getHeight()/2 + 20);
    }
    else {
      glassL1.setRotation(-90);
      glassR1.setRotation(90);

      glassL1.setPosition(fruit.getX() + fruit.getWidth()/2 - glassL1.getWidth()/2,
              fruit.getY() + fruit.getHeight()/2 - glassL1.getHeight()/2 - 20);
      glassR1.setPosition(fruit.getX() + fruit.getWidth()/2 - glassL1.getWidth()/2,
              fruit.getY() + fruit.getHeight()/2 - glassL1.getHeight()/2 + 20);

      glassL2.setRotation(180);
      glassL2.setPosition(fruit.getX() + fruit.getWidth()/2 - glassL2.getWidth()/2 - 20,
              fruit.getY() + fruit.getHeight()/2 - glassL2.getHeight()/2);
      glassR2.setPosition(fruit.getX() + fruit.getWidth()/2 - glassL2.getWidth()/2 + 20,
              fruit.getY() + fruit.getHeight()/2 - glassL2.getHeight()/2);
    }
  }

  public void addAnimGlassToScene(boolean isBoth) {
    if (isBoth) {
      glassL1.getColor().a = 0f;
      glassR1.getColor().a = 0f;
      glassL2.getColor().a = 0f;
      glassR2.getColor().a = 0f;

      this.addActor(glassL1);
      this.addActor(glassR1);
      this.addActor(glassL2);
      this.addActor(glassR2);
    }
    else {
      glassL1.getColor().a = 0f;
      glassR1.getColor().a = 0f;

      this.addActor(glassL1);
      this.addActor(glassR1);
    }
  }

  //label: anim clock
  public void animClock(Runnable onComplete) {
    fruit.setVisible(false);
    flare.setVisible(false);
    this.addAction(
            sequence(
                    delay(.25f),
                    run(onComplete),
                    run(() -> {
                      isAlive = false;
                      fruit.setVisible(true);
                      flare.setVisible(true);
                      this.remove();
                    })
            )
    );
  }

  //label: anim zoom in and vibrate
  public void animZoomAndVibration() {
    isStopAnimZoomAndVibrate = false;
    timeAnimZoomAndVibrate   = 1f;
    this.setScale(1.1f);
  }

  public void animJam(Runnable onComplete) {
    fruit.setVisible(false);
    flare.setVisible(false);
    this.addAction(
            sequence(
                    delay(.25f),
                    run(onComplete),
                    run(() -> {
                      isAlive = false;
                      fruit.setVisible(true);
                      flare.setVisible(true);
                      this.remove();
                    })
            )
    );
  }

  //label: anim lbScore
  public void animLbScore() {
    lbScore.clearActions();
    lbScore.getColor().a = 1f;
    lbScore.setPosition(this.getX() + this.getWidth()/2 - lbScore.getWidth()/2,
                        this.getY() + this.getHeight()/2 - lbScore.getHeight()/2);
    gLbScore.addActor(lbScore);

    if (iAnimClock != null) { //for item clock
      iAnimClock.clearActions();
      iAnimClock.getColor().a = 1f;
      iAnimClock.setPosition(lbScore.getX() - 35, lbScore.getY() + 5);
      gLbScore.addActor(iAnimClock);

      iAnimClock.addAction(
              sequence(
                      parallel(
                              alpha(0f, 1.75f, linear),
                              Actions.moveBy(0, -100, 1.5f, linear)
                      ),
                      run(() -> {
                        iAnimClock.remove();
                        iAnimClock.moveBy(0, 100);
                        iAnimClock.getColor().a = 1f;
                      })
              )
      );
    }

    lbScore.addAction(
            sequence(
                    parallel(
                            alpha(0f, 1.75f, linear),
                            Actions.moveBy(0, -100, 1.5f, linear)
                    ),
                    run(() -> {
                      resetLbScore();
                      lbScore.remove();
                      lbScore.moveBy(0, 100);
                      lbScore.getColor().a = 1f;
                    })
            )
    );
  }

  public void refactoryItem() {
    isAlive = false;
    fruit.clearActions();
    fruit.getColor().a = 1f;
    fruit.setVisible(true);

    if (iAnimClock != null) {
      iAnimClock.clearActions();
      iAnimClock.getColor().a = 1f;
      iAnimClock.remove();
    }

    if (ice != null) {
      ice.clearActions();
      resetAnimIce();
    }

    if (flare != null)
      flare.setVisible(true);

    if (glassL1 != null && glassR1 != null && glassL2 != null && glassR2 != null) {
      glassL1.setRotation(0);
      glassR1.setRotation(0);
      glassL2.setRotation(0);
      glassR2.setRotation(0);

      glassL1.clearActions();
      glassR1.clearActions();
      glassL2.clearActions();
      glassR2.clearActions();

      glassL1.remove();
      glassR1.remove();
      glassL2.remove();
      glassR2.remove();
    }

    if (gAnimFruit != null) {
      animFruitL.clearActions();
      animFruitR.clearActions();

      animFruitL.setRotation(0);
      animFruitR.setRotation(0);
      animFruitL.getColor().a = 1f;
      animFruitR.getColor().a = 1f;

      setPosAnimFruit();
      gAnimFruit.remove();
    }

    lbScore.clearActions();
    lbScore.getColor().a = 1f;
    lbScore.remove();

    resetLbScore();
    this.clearActions();
    this.remove();
  }

  private void resetLbScore() {
    if (chkRegion(type))
      lbScore.setText("+" + SCORE_FRUIT);
    else if (type == Type.glass_fruit)
      lbScore.setText("+" + SCORE_GLASS_JUICE);
    else if (type == Type.jam)
      lbScore.setText("+" + SCORE_JAM);
    else if (type == Type.clock)
      lbScore.setText("+" + SCORE_CLOCK);
  }

  public void setScoreLb(int score) {
    lbScore.setText("+" + score);
  }

  @Override
  public void act(float delta) {
    super.act(delta);

    if (!isStopAnimZoomAndVibrate && timeAnimZoomAndVibrate > 0) {
      timeAnimZoomAndVibrate = 0;
      this.setOrigin(Align.center);
      this.addAction(
              sequence(
                      Actions.rotateBy(10, .15f, fastSlow),
                      Actions.rotateBy(-20, .15f, fastSlow),
                      Actions.rotateBy(20, .15f, fastSlow),
                      Actions.rotateBy(-10, .15f, fastSlow),
                      delay(.75f),
                      run(() -> timeAnimZoomAndVibrate = 1f)
              )
      );
    }

  }
}
