package com.ss.objects;

import static com.badlogic.gdx.math.Interpolation.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Align;
import com.ss.GMain;
import com.ss.config.Type;
import com.ss.core.util.GUI;
import com.ss.ui.GamePlayUI;
import com.ss.utils.Util;

import java.util.ArrayList;
import java.util.List;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import static com.ss.config.Config.*;

public class Item extends Group {

  private Image fruit, animL, animR;
  private Image flare, glassL1, glassL2, glassR1, glassR2;
  private Group gAnim;
  public Type type;
  public String name;
  public boolean isAlive = false;

  public Item(String region, Type type) {

    this.name = region;
    this.type = type;

    fruit = GUI.createImage(GMain.itemAtlas, region);
    setSize(fruit.getWidth(), fruit.getHeight());
    this.setOrigin(Align.center);
    this.addActor(fruit);

    if (chkRegion(type)) {
      gAnim = new Group();
      animL = GUI.createImage(GMain.itemAtlas, type.name()+"_l");
      animR = GUI.createImage(GMain.itemAtlas, type.name()+"_r");

      if (animL.getWidth() >= animR.getWidth())
        gAnim.setWidth(animL.getWidth());
      else
        gAnim.setWidth(animR.getWidth());

      if (animL.getHeight() >= animR.getHeight())
        gAnim.setHeight(animL.getHeight());
      else
        gAnim.setHeight(animR.getHeight());

      animL.setOrigin(Align.center);
      animR.setOrigin(Align.center);

      gAnim.addActor(animR);
      gAnim.addActor(animL);
      setPosAnim();
    }
    else
      createAnimSpecialItem();

  }

  private void createAnimSpecialItem() {

    flare = GUI.createImage(GMain.itemAtlas, "flare");
    flare.setPosition(fruit.getX() + fruit.getWidth()/2 - flare.getWidth()/2,
            fruit.getY() + fruit.getHeight()/2 - flare.getHeight()/2);
    flare.setOrigin(Align.center);
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

  public void reset() {
    if (flare != null)
      flare.remove();
    startAnim();
  }

  private void startAnim() {

    fruit.setVisible(false);
    if (chkRegion(type)) {
      this.addActor(gAnim);
      rndAnimFruit();
    }

  }

  private void rndAnimFruit() {
    int rnd = (int) Math.round(Math.random() * 3) + 1;
    switch (rnd) {
      case 1: anim1(); break;
      case 2: anim2(); break;
      case 3: anim3(); break;
      case 4: anim4(); break;
    }
  }

  private boolean chkRegion(Type type) {
    return  type != Type.glass_fruit &&
            type != Type.jam         &&
            type != Type.clock       &&
            type != Type.walnut;
  }

  //---------------------------------------add to scene---------------------------------------------
  //anim normal item
  public void addGAnimToScene() {
    this.addActor(gAnim);
    fruit.setVisible(false);
  }

  public void addFlare() {
    this.addActor(flare);
    fruit.setZIndex(1000);
  }
  //---------------------------------------add to scene---------------------------------------------

  private void setPosAnim() {
    switch (name) {
      case "item_apple":
        animL.setPosition(-3, -10);
        animR.setPosition(0, 7);
        break;
      case "item_strawberry":
        animL.setPosition(0, -17);
        animR.setPosition(-2, 7);
        break;
      case "item_orange":
        animR.setZIndex(1000);
        animR.setPosition(0, 20);
        animL.setPosition(-1, -6);
        break;
      case "item_grape":
        animL.setPosition(-10, 0);
        animR.setPosition(5, 40);
        break;
      case "item_kiwi":
        animR.setZIndex(1000);
        animR.setPosition(-2, 16);
        animL.setPosition(-15, 0);
        break;
      case "item_banana":
        animL.setPosition(42, -3);
        animR.setPosition(0, 20);
        break;
    }
  }

  //label: anim normal item
  public void anim0() {
    fruit.setOrigin(Align.topLeft);
    fruit.addAction(
            sequence(
                    Actions.scaleBy(0, -.1f, .2f, fastSlow),
                    Actions.scaleBy(0, .1f, .2f, fastSlow)
            )
    );
  }

  private void anim1() {

    Runnable reset = () -> {
      animL.moveBy(25, 7);
      animL.moveBy(0, -70);
      animL.setRotation(0);
      animL.getColor().a = 1f;

      animR.moveBy(-25, 7);
      animR.moveBy(0, -70);
      animR.setRotation(0);
      animR.getColor().a = 1f;

      fruit.setVisible(true);
      isAlive = false;
      gAnim.remove();
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

    animL.addAction(seqL);
    animR.addAction(seqR);

  }

  private void anim2() {

    Runnable reset = () -> {
      animL.moveBy(-100, -110);
      animL.setRotation(0);
      animL.getColor().a = 1f;

      animR.moveBy(110, -70);
      animR.setRotation(0);
      animR.getColor().a = 1f;

      fruit.setVisible(true);
      isAlive = false;
      gAnim.remove();
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

    animL.addAction(seqL);
    animR.addAction(seqR);

  }

  private void anim3() {

    Runnable reset = () -> {
      animL.moveBy(-45, -90);
      animL.setRotation(0);
      animL.getColor().a = 1f;

      animR.moveBy(45, -90);
      animR.setRotation(0);
      animR.getColor().a = 1f;

      fruit.setVisible(true);
      isAlive = false;
      gAnim.remove();
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

    animL.addAction(seqL);
    animR.addAction(seqR);

  }

  private void anim4() {

    Runnable reset = () -> {
      animL.moveBy(-80, -100);
      animL.setRotation(0);
      animL.getColor().a = 1f;

      animR.moveBy(130, -132);
      animR.setRotation(0);
      animR.getColor().a = 1f;

      fruit.setVisible(true);
      isAlive = false;
      gAnim.remove();
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

    animL.addAction(seqL);
    animR.addAction(seqR);

  }

  private void animFlare() {
    flare.addAction(
            sequence(
                    Actions.rotateBy(-10, .25f, linear),
                    run(this::animFlare)
            )
    );
  }

  //label: anim glass juice
  public void animGlassJuice(boolean hor) {

    Runnable reset = () -> {
      isAlive = false;
      this.remove();
    };

    flare.remove();
    if (hor) {
      float moveLeft = this.getX() - GamePlayUI.bgTable.getX();
      float moveRight   = GamePlayUI.bgTable.getX() + GamePlayUI.bgTable.getWidth() - this.getX() - glassR1.getWidth();
      glassL1.addAction(moveTo(-moveLeft, glassL1.getY(), .25f, slowFast));
      glassR1.addAction(
              sequence(
                      moveTo(moveRight, glassR1.getY(), .25f, slowFast),
                      run(reset)
              )
      );

      float moveDown = this.getY() - GamePlayUI.bgTable.getY();
      float moveUp   = GamePlayUI.bgTable.getY() + GamePlayUI.bgTable.getHeight() - this.getY() - glassR2.getHeight();
      glassL2.addAction(moveTo(glassL2.getX(), -moveDown, .25f, slowFast));
      glassR2.addAction(
              sequence(
                      moveTo(glassR2.getX(), moveUp, .25f, slowFast),
                      run(reset)
              )
      );
    }
    else {
      float moveDown = this.getY() - GamePlayUI.bgTable.getY();
      float moveUp   = GamePlayUI.bgTable.getY() + GamePlayUI.bgTable.getHeight() - this.getY() - glassR1.getHeight();
      glassL1.addAction(moveTo(glassL1.getX(), -moveDown, .25f, slowFast));
      glassR1.addAction(
              sequence(
                      moveTo(glassR1.getX(), moveUp, .25f, slowFast),
                      run(reset)
              )
      );

      float moveLeft = this.getX() - GamePlayUI.bgTable.getX();
      float moveRight   = GamePlayUI.bgTable.getX() + GamePlayUI.bgTable.getWidth() - this.getX() - glassR2.getWidth();
      glassL2.addAction(moveTo(-moveLeft, glassL2.getY(), .25f, slowFast));
      glassR2.addAction(
              sequence(
                      moveTo(moveRight, glassR2.getY(), .25f, slowFast),
                      run(reset)
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
      this.addActor(glassL1);
      this.addActor(glassR1);
      this.addActor(glassL2);
      this.addActor(glassR2);
    }
    else {
      this.addActor(glassL1);
      this.addActor(glassR1);
    }
  }

}
