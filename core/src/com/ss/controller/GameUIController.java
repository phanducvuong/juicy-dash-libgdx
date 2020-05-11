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
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import static com.ss.config.Config.*;

public class GameUIController implements Item.IFinishMove {

  private Util util = Util.inst();
  private Group gParent;
  private GamePlayUI gamePlayUI;

  private Piece[][] arrPosPiece = new Piece[ROW][COL];
  private HashMap<String, List<Item>> hmItem;
  private List<Type> lv;

  public boolean isDragAndDrop = false, isWrap = false;
  private Piece pieceStart;

  public GameUIController(Group gParent) {

    this.gParent = gParent;
    this.gamePlayUI = new GamePlayUI(this);
    this.hmItem = new HashMap<>();
    this.lv = new ArrayList<>();

    //label add ui to scene
    gParent.addActor(gamePlayUI);

    initPiece();
    initItem();

    initLv();

    eventTouchScreen();

    addItem();
  }

  private void initLv() {
    lv.add(Type.strawberry);
    lv.add(Type.orange);
    lv.add(Type.grape);
//    lv.add(Type.banana);
  }

  private void initPiece() {

    float offsetX = gamePlayUI.bgTable.getX() + OFFSET_X_PIECE;
    float offsetY = gamePlayUI.bgTable.getY() + OFFSET_Y_PIECE;

    for (int i=0; i<ROW; i++) {
      for (int j = 0; j< COL; j++) {
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
        Item item = new Item(region, type);
        item.setiFinishMove(this);
        if (i < 6)
          lsItem.add(item);
        else
          lsItem.add(item);
      }

      hmItem.put(region, lsItem);

    }

  }

  private void eventTouchScreen() {

    //label: drag and drop
//    if (isDragAndDrop) {
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
            util.log("start: ", pieceStart);
            util.log("end: ", pieceDrag);

            pieceStart = null;
          }

        }
      });
//    }

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

    List<Item> lsItem = util.getLsItem(hmItem, lv);
    Collections.shuffle(lsItem, new Random());
    System.out.println("SIZE: " + lsItem.size());

    int count = 0;
    for (int i=ROW-1; i>=0; i--) {
      for (int j = 0; j< COL; j++) {

        Item item = lsItem.get(count);
        Piece piece = util.getPieceEmpty(arrPosPiece, i, j);
        addNewItem(item, piece);

        count++;

      }
    }

  }

  public void filterAll() {

    List<Piece> lsVertical    = new ArrayList<>();
    List<Piece> lsHorizontal  = new ArrayList<>();

    for (int i=0; i<ROW; i++) {
      for (int j=0; j<COL; j++) {

        List<Piece> tmpV = util.filterVertically(arrPosPiece, arrPosPiece[i][j]);
        List<Piece> tmpH = util.filterHorizontally(arrPosPiece, arrPosPiece[i][j]);

        if (tmpV.size() >= 3)
          util.chkContain(lsVertical, tmpV);
        if (tmpH.size() >= 3)
          util.chkContain(lsHorizontal, tmpH);

      }
    }

    clrLsFilterPiece(lsVertical);
    clrLsFilterPiece(lsHorizontal);

    //update arrPosPiece
    for (int col=0; col<COL; col++) {
      for (int row=ROW-1; row>=0; row--) {
        Piece piece = arrPosPiece[row][col];
        if (piece.item == null) {
          slideVer(piece.row, piece.col);
        }
      }
    }

    //add item at piece is null item
    for (int col=0; col<COL; col++) {
      List<Piece> tmp = new ArrayList<>(); //ls piece is null item
      for (int row=ROW-1; row>=0; row--) {
        Piece piece = arrPosPiece[row][col];
        if (piece.item == null) {
          Item item = util.getRndItem(hmItem, lv);
          addNewItem(item, piece);
        }
      }
    }

  }

  private void filterAt(int row, int col) {

    List<Piece> lsPieceHor = util.filterVertically(arrPosPiece, arrPosPiece[row][col]);
    List<Piece> lsPieceVer = util.filterHorizontally(arrPosPiece, arrPosPiece[row][col]);

    if (lsPieceVer.size() >= 3) {
      clrLsFilterPiece(lsPieceVer);

      //label: slide
      Piece pMinRow = util.getPieceMinRow(lsPieceVer);
      slideVer(pMinRow.row - 1, pMinRow.col);
    }// check by vertical

    if (lsPieceHor.size() >= 3) {
      clrLsFilterPiece(lsPieceHor);

      //label: slide
      if (lsPieceVer.size() >= 3)
        lsPieceHor.remove(arrPosPiece[row][col]);
      for (Piece piece : lsPieceHor) //check by horizontal
        slideVer(piece.row-1, piece.col);
    }

  }

  //label: slide by vertical (up to down)
  private void slideVer(int row, int col) {

    for (int r=row-1; r>=0; r--) {
      Piece piece = arrPosPiece[r][col];
      Item item = piece.item;
      if (piece.item != null) {
        arrPosPiece[row][col].setItem(piece.item);
        piece.clear();
        item.moveToPos(arrPosPiece[row][col].pos, .5f);
        break;
      }
    }

  }

  private void clrLsFilterPiece(List<Piece> filter) {
    for (Piece piece : filter) {
      if (piece.item != null)
        piece.item.reset();
      piece.clear();
    }

    //todo: action new item in piece is empty
  }

  private void addNewItem(Item item, Piece piece) {
    piece.setItem(item);
    gamePlayUI.addToGItem(item);
    item.setPosStart(piece.pos);
    item.moveToPos(piece.pos, .5f);
  }

  @Override
  public void finished() {
//    filterAll();
  }

  @Override
  public void blockInput() {
    isDragAndDrop = false;
  }
}
