package com.ss.ui;

import com.badlogic.gdx.graphics.Color;
import static com.badlogic.gdx.math.Interpolation.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.I18NBundle;
import com.ss.GMain;
import static com.ss.config.Config.*;

import com.ss.config.C;
import com.ss.config.Config;
import com.ss.controller.GameUIController;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import com.ss.objects.Item;
import com.ss.objects.Piece;
import com.ss.utils.Solid;
import com.ss.utils.Util;
import java.util.ArrayList;
import java.util.List;

public class TutorialUI extends Group {

  private GameUIController controller;
  private Util             util;
  private I18NBundle       locale       = C.lang.locale;

  private Image            black,
                           bgTuto9,
                           bgTuto2;
  private Image            arrowL, arrowR;
  private Label            lbTutorial;

  private Piece            pStart, pEnd;
  private boolean          isAnimArr    = true;

  private boolean          isTuto9      = false,
                           isTuto2      = false,
                           isTutoGlass  = false,
                           isWrap       = true;

  public  int              countTutorial = 0;
  private List<Item>       lsItem;

  public TutorialUI(GameUIController controller) {

    this.controller = controller;
    this.util       = Util.inst();
    this.lsItem     = new ArrayList<>();

    this.black      = new Image(Solid.create(new Color(0/255f, 0/255f, 0/255f, .35f)));
    this.black.setSize(GStage.getWorldWidth(), GStage.getWorldHeight());

    lbTutorial  = new Label(locale.get("txt_tutorial_9"), new Label.LabelStyle(whiteFont, null));
    lbTutorial.setPosition(GStage.getWorldWidth()/2 - lbTutorial.getWidth()/2,
                           GStage.getWorldHeight()/2 - 250);

    bgTuto9 = GUI.createImage(GMain.bgAtlas, "bg_tuto_9");
    bgTuto2 = GUI.createImage(GMain.bgAtlas, "bg_tuto_2");

    arrowL  = GUI.createImage(GMain.bgAtlas, "arrow");
    arrowR  = GUI.createImage(GMain.bgAtlas, "arrow");

    arrowL.setOrigin(Align.center);
    arrowR.setOrigin(Align.center);

    this.addListener(new DragListener() {
      @Override
      public void dragStart(InputEvent event, float x, float y, int pointer) {
        super.dragStart(event, x, y, pointer);

        pStart = util.getPieceByCoordinate(x, y, controller.arrPosPiece);

      }

      @Override
      public void drag(InputEvent event, float x, float y, int pointer) {
        super.drag(event, x, y, pointer);

        pEnd = util.getPieceByCoordinate(x, y, controller.arrPosPiece);
        if (!isWrap && pStart != null && pEnd != null && pStart != pEnd) {
          if (isTuto9)
            animTuto9();
          else if (isTuto2)
            animTuto2();
          else if (isTutoGlass)
            animTutoGlass();
        }

      }
    });

  }

  //-----------------------------------anim tutorial------------------------------------------------
  private void animTuto9() {

    if (pStart.row == 3 && pStart.col == 2 && pEnd.row == 4 && pEnd.col == 2 ||
        pStart.row == 4 && pStart.col == 2 && pEnd.row == 3 && pEnd.col == 2) {

      isWrap  = true;
      isTuto9 = false;
      this.removeAllChild();
      this.remove();
      controller.setTouchableGamePlayUI(Touchable.enabled);
      controller.swap(controller.arrPosPiece[3][2], controller.arrPosPiece[4][2]);

    }

  }

  private void animTuto2() {
    if (pStart.row == 4 && pStart.col == 4 && pEnd.row == 5 && pEnd.col == 4 ||
        pStart.row == 5 && pStart.col == 4 && pEnd.row == 4 && pEnd.col == 4) {

      isWrap  = true;
      isTuto2 = false;
      this.removeAllChild();
      this.remove();
      controller.setTouchableGamePlayUI(Touchable.enabled);
      controller.swap(controller.arrPosPiece[4][4], controller.arrPosPiece[5][4]);

    }
  }

  private void animTutoGlass() {
    if (pStart.row == 4 && pStart.col == 3 && pEnd.row == 4 && pEnd.col == 4 ||
        pStart.row == 4 && pStart.col == 4 && pEnd.row == 4 && pEnd.col == 3) {

      isWrap  = true;
      isTuto2 = false;
      this.removeAllChild();
      this.remove();
      controller.setTouchableGamePlayUI(Touchable.enabled);
      controller.swap(controller.arrPosPiece[4][3], controller.arrPosPiece[4][4]);
      controller.isTutorial = false;

      util.saveData(false, "is_tutorial");

      //todo: save tutorial in preference

    }
  }

  private void animArrow(float moveByX, float moveByY) {
    if (isAnimArr)
      return;
    arrowL.addAction(
            sequence(
                    Actions.moveBy(moveByX, -moveByY, .5f, fastSlow),
                    Actions.moveBy(-moveByX, moveByY, .5f, fastSlow),
                    run(() -> animArrow(moveByX, moveByY))
            )
    );

    arrowR.addAction(
            sequence(
                    Actions.moveBy(-moveByX, moveByY, .5f, fastSlow),
                    Actions.moveBy(moveByX, -moveByY, .5f, fastSlow)
            )
    );
  }
  //-----------------------------------anim tutorial------------------------------------------------

  //-----------------------------------show tutorial------------------------------------------------
  private void showTutoGlass() {
    isWrap      = false;
    isTutoGlass = true;
    controller.setTouchableGamePlayUI(Touchable.disabled);
    lbTutorial.setText(locale.get("txt_tutorial_glass"));

    this.addActor(black);
    this.addActor(bgTuto2);
    this.addActor(lbTutorial);

    Vector2 pos = controller.arrPosPiece[4][3].pos;
    bgTuto2.setPosition(pos.x, pos.y);
    bgTuto2.setRotation(0);

    lbTutorial.setPosition(bgTuto2.getX() - 100, bgTuto2.getY() - 200);

    arrowL.setPosition(pos.x + WIDTH_PIECE/2 + 15, pos.y + HEIGHT_PIECE/2 - 35);
    arrowR.setRotation(180);
    arrowR.setPosition(arrowL.getX(), arrowL.getY() + 35);

    isAnimArr = false;
    animArrow(10, 0);

    Item glass  = util.getItemBy(controller.hmItem, "glass_juice");
    Item i44    = util.getItemBy(controller.hmItem, "strawberry");

    glass.setPosition(controller.arrPosPiece[4][3].pos);
    i44.setPosition(controller.arrPosPiece[4][4].pos);

    lsItem.add(glass);
    lsItem.add(i44);

    this.addActor(glass);
    this.addActor(i44);
    this.addActor(arrowL);
    this.addActor(arrowR);
  }

  private void showTuto2() {
    isWrap  = false;
    isTuto2 = true;
    controller.setTouchableGamePlayUI(Touchable.disabled);
    lbTutorial.setText(locale.get("txt_tutorial_2"));

    this.addActor(black);
    this.addActor(bgTuto2);
    this.addActor(lbTutorial);

    Vector2 pos = controller.arrPosPiece[4][4].pos;
    bgTuto2.setPosition(pos.x - WIDTH_PIECE/2, pos.y + HEIGHT_PIECE/2);
    lbTutorial.setPosition(bgTuto2.getX() - 50, bgTuto2.getY() - 200);

    bgTuto2.setOrigin(Align.center);
    bgTuto2.setRotation(90);

    arrowL.setRotation(-90);
    arrowL.setPosition(pos.x + WIDTH_PIECE/2 - 50, pos.y + HEIGHT_PIECE - 20);
    arrowR.setRotation(90);
    arrowR.setPosition(arrowL.getX() + 35, arrowL.getY());

    isAnimArr = false;
    animArrow(0, 10);

    Item i44 = util.getItemBy(controller.hmItem, itemTutorial[4][4]);
    Item i54 = util.getItemBy(controller.hmItem, itemTutorial[5][4]);

    i44.setPosition(controller.arrPosPiece[4][4].pos);
    i54.setPosition(controller.arrPosPiece[5][4].pos);

    lsItem.add(i44);
    lsItem.add(i54);

    this.addActor(i44);
    this.addActor(i54);
    this.addActor(arrowL);
    this.addActor(arrowR);
  }

  private void showTuto9() {
    isWrap  = false;
    isTuto9 = true;
    controller.setTouchableGamePlayUI(Touchable.disabled);
    lbTutorial.setText(locale.get("txt_tutorial_9"));

    this.addActor(black);
    this.addActor(bgTuto9);
    this.addActor(lbTutorial);

    Vector2 pos = controller.arrPosPiece[3][2].pos;
    bgTuto9.setPosition(pos.x, pos.y);

    arrowL.setRotation(-90);
    arrowL.setPosition(pos.x + WIDTH_PIECE/2 - 50, pos.y + HEIGHT_PIECE - 20);
    arrowR.setRotation(90);
    arrowR.setPosition(arrowL.getX() + 35, arrowL.getY());

    isAnimArr = false;
    animArrow(0, 10);

    Item i32 = util.getItemBy(controller.hmItem, itemTutorial[3][2]);
    Item i33 = util.getItemBy(controller.hmItem, itemTutorial[3][3]);
    Item i34 = util.getItemBy(controller.hmItem, itemTutorial[3][4]);

    Item i42 = util.getItemBy(controller.hmItem, itemTutorial[4][2]);
    Item i43 = util.getItemBy(controller.hmItem, itemTutorial[4][3]);
    Item i44 = util.getItemBy(controller.hmItem, itemTutorial[4][4]);

    i32.setPosition(controller.arrPosPiece[3][2].pos);
    i33.setPosition(controller.arrPosPiece[3][3].pos);
    i34.setPosition(controller.arrPosPiece[3][4].pos);

    i42.setPosition(controller.arrPosPiece[4][2].pos);
    i43.setPosition(controller.arrPosPiece[4][3].pos);
    i44.setPosition(controller.arrPosPiece[4][4].pos);

    lsItem.add(i32);
    lsItem.add(i33);
    lsItem.add(i34);
    lsItem.add(i42);
    lsItem.add(i43);
    lsItem.add(i44);

    this.addActor(i32);
    this.addActor(i33);
    this.addActor(i34);
    this.addActor(i42);
    this.addActor(i43);
    this.addActor(i44);
    this.addActor(arrowL);
    this.addActor(arrowR);
  }

  public void showTutorial() {
    countTutorial += 1;
    if (countTutorial == 1) {
      controller.gParent.addActor(this);
      showTuto9();
    }
    else if (countTutorial == 2) {
      controller.gParent.addActor(this);
      showTuto2();
    }
    else if (countTutorial == 3) {
      controller.gParent.addActor(this);
      showTutoGlass();
    }
  }
  //-----------------------------------show tutorial------------------------------------------------

  private void removeAllChild() {
    isAnimArr = true;

    black.remove();
    bgTuto9.remove();
    bgTuto2.remove();
    arrowL.remove();
    arrowR.remove();

    arrowL.clearActions();
    arrowR.clearActions();
    arrowL.setRotation(0);
    arrowR.setRotation(0);

    lbTutorial.remove();

    for (Item item : lsItem) {
      item.isAlive = false;
      item.remove();
    }
    lsItem.clear();
  }

}
