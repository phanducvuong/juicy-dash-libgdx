package com.ss.controller;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.ss.config.Type;
import com.ss.objects.Item;
import com.ss.objects.Piece;
import com.ss.ui.GamePlayUI;
import com.ss.utils.Util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static com.ss.config.Config.*;

public class GameUIController {

  private Util util = Util.inst();
  private Group gParent;
  private GamePlayUI gamePlayUI;

  private Piece[][] arrPosPiece = new Piece[ROW][COLUMN];
  private HashMap<String, List<Item>> hmItem;

  private boolean isDragAndDrop = false, isWrap = false;
  private Piece pieceStart;

  public GameUIController(Group gParent) {

    this.gParent = gParent;
    this.gamePlayUI = new GamePlayUI();
    this.hmItem = new HashMap<>();

    //label add ui to scene
    gParent.addActor(gamePlayUI);

    initPiece();
    initItem();
    eventTouchScreen();

    addItem();
  }

  private void initPiece() {

    float offsetX = gamePlayUI.bgTable.getX() + OFFSET_X_PIECE;
    float offsetY = gamePlayUI.bgTable.getY() + OFFSET_Y_PIECE;

    for (int i=0; i<ROW; i++) {
      for (int j=0; j<COLUMN; j++) {
        Vector2 pos = new Vector2(offsetX + WIDTH_PIECE*j, offsetY + HEIGHT_PIECE*i);
        Piece piece = new Piece(i, j, pos);
        arrPosPiece[i][j] = piece;
      }
    }

  }

  private void initItem() {
    for (int i=0; i<AMOUNT_ITEM; i++) {

      String region = util.getRegion(i);
      Type type = util.getType(i);
      List<Item> lsItem = new ArrayList<>();

      for (int j=0; j<AMOUNT_ITEM_CREATE; j++) {
        if (i < 6)
          lsItem.add(new Item(region, type));
        else
          lsItem.add(new Item(region, type));
      }

      hmItem.put(region, lsItem);

    }

  }

  private void eventTouchScreen() {

    //label: drag and drop
    gamePlayUI.addListener(new DragListener() {

      @Override
      public void dragStart(InputEvent event, float x, float y, int pointer) {
        super.dragStart(event, x, y, pointer);

        isDragAndDrop = true;
        pieceStart = util.inRange(arrPosPiece, new Vector2(x, y));

      }

      @Override
      public void drag(InputEvent event, float x, float y, int pointer) {
        super.drag(event, x, y, pointer);

        Piece pieceDrag = util.inRange(arrPosPiece, new Vector2(x, y));
        if (pieceStart != null && pieceDrag != pieceStart) {
          util.log(pieceStart);
          util.log(pieceDrag);

          pieceStart = null;
        }

      }
    });

    //label: click
    gamePlayUI.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);

        Vector2 pos = new Vector2(x, y);
        Piece pieceClick = util.inRange(arrPosPiece, pos);
//        System.out.println("CLICK: " + pieceClick.pos);

      }
    });

  }

  private void addItem() {

    List<Item> lsItem = util.getLsItem(hmItem, Type.strawberry, Type.orange, Type.grape, Type.banana);
    Collections.shuffle(lsItem, new Random());
    System.out.println("SIZE: " + lsItem.size());

    int count = 0;
    for (int i=ROW-1; i>=0; i--) {
      for (int j=0; j<COLUMN; j++) {

        Item item = lsItem.get(count);
        gamePlayUI.addToGItem(item);

        Piece piece = util.getPieceEmpty(arrPosPiece, i, j);
        piece.setItem(item);

        item.setPosStart(piece.pos);
        item.moveToPos(piece.pos);

        count++;

      }
    }

  }

}
