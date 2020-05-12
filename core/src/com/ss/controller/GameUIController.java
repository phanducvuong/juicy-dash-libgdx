package com.ss.controller;

import static com.badlogic.gdx.math.Interpolation.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.ss.config.Type;
import com.ss.core.action.exAction.GSimpleAction;
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

public class GameUIController {

  private Util util = Util.inst();
  private Group gParent;
  private GamePlayUI gamePlayUI;

  private Piece[][] arrPosPiece = new Piece[ROW][COL];
  private HashMap<String, List<Item>> hmItem;
  private List<Piece> lsPieceNullItem = new ArrayList<>();
  private int turn = ROW;
  private List<Type> lv;

  public boolean isDragAndDrop = true, isWrap = false, isTheSame = false;
  private Piece pieceStart, pieceEnd;

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
    lv.add(Type.banana);
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
    gamePlayUI.addListener(new DragListener() {

      @Override
      public void dragStart(InputEvent event, float x, float y, int pointer) {
        super.dragStart(event, x, y, pointer);

        pieceStart = util.inRange(arrPosPiece, new Vector2(x, y));

      }

      @Override
      public void drag(InputEvent event, float x, float y, int pointer) {
        super.drag(event, x, y, pointer);

        pieceEnd = util.inRange(arrPosPiece, new Vector2(x, y));
        if (!isWrap && pieceStart != null && pieceEnd != pieceStart) {
          util.log("start: ", pieceStart);
          util.log("end: ", pieceEnd);

          wrap(pieceStart, pieceEnd);

          isWrap = true;
//          pieceStart = null;
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

    List<HashMap<String, List<Piece>>> lsSpecialItem = new ArrayList<>();

    for (int i=0; i<ROW; i++) {
      for (int j=0; j<COL; j++) {

        List<Piece> tmpV = util.filterVertically(arrPosPiece, arrPosPiece[i][j]);
        List<Piece> tmpH = util.filterHorizontally(arrPosPiece, arrPosPiece[i][j]);
        HashMap<String, List<Piece>> hmTmp = new HashMap<>();

        if (tmpV.size() >= 3 && !util.chkContain(lsVertical, tmpV)) {
          hmTmp.put("ver", tmpV);
          lsVertical.addAll(tmpV);
        }

        if (tmpH.size() >= 3 && !util.chkContain(lsHorizontal, tmpH)) {
          hmTmp.put("hor", tmpH);
          lsHorizontal.addAll(tmpH);
        }

        if (hmTmp.size() > 0)
          lsSpecialItem.add(hmTmp);

      }
    }

    //todo: check lsSpecialItem and add special item
    for (HashMap<String, List<Piece>> hh : lsSpecialItem) {
      if (hh.get("ver") != null) {
        System.out.print("VER: ");
        util.log(hh.get("ver").get(0));
      }
      if (hh.get("hor") != null) {
        System.out.print("HOR: ");
        util.log(hh.get("hor").get(0));
      }
    }

    clrLsFilterPiece(lsVertical);
    clrLsFilterPiece(lsHorizontal);

    updateArrPiece();

  }

  private void filterAt(int row, int col) {

    List<Piece> lsPieceHor = util.filterVertically(arrPosPiece, arrPosPiece[row][col]);
    List<Piece> lsPieceVer = util.filterHorizontally(arrPosPiece, arrPosPiece[row][col]);

    int countItem = 0;
    if (lsPieceVer.size() >= 3) {
      countItem += lsPieceVer.size();
      clrLsFilterPiece(lsPieceVer);
      isTheSame = true;
    }

    if (lsPieceHor.size() >= 3) {
      countItem += lsPieceVer.size();
      clrLsFilterPiece(lsPieceHor);
      isTheSame = true;
    }

    //todo: check count item to add special item

    updateArrPiece();

  }

  //label: update array pos piece
  private void updateArrPiece() {

    lsPieceNullItem.clear();

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
    for (int row=ROW-1; row>=0; row--) {
      for (int col=0; col<COL; col++) {
        Piece piece = arrPosPiece[row][col];
        if (piece.item == null) {
          piece.item = util.getRndItem(hmItem, lv);
          lsPieceNullItem.add(piece);
        }
      }
    }

//    addItemSequence(lsPieceNullItem, ROW-1, .75f);
    if (lsPieceNullItem.size() > 0) {
      turn = ROW;
      nextRow();
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
        item.moveToPos(arrPosPiece[row][col].pos, .35f);
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

  private void startNewItem(int row) {
    if (row >= 0)
      gamePlayUI.gBackground.addAction(GSimpleAction.simpleAction(this::action));
    else {
      //todo: fillAll
      System.out.println("FINISHED!");
      gamePlayUI.gBackground.addAction(
              sequence(
                      delay(TIME_DELAY_TO_CHECK_ALL),
                      run(this::filterAll),
                      run(() -> {
                        if (lsPieceNullItem.size() <= 0)
                          blockInput();
                      })
              )
      );
    }
  }

  private boolean action(float dt, Actor a) {

    for (Piece piece : lsPieceNullItem) {
      for (int col=0; col<COL; col++) {
        if (arrPosPiece[turn][col] == piece)
          addNewItem(piece.item, piece);
      }
    }

    gamePlayUI.gBackground.addAction(
            sequence(
                    delay(.05f),
                    run(this::nextRow)
            )
    );

    return true;
  }

  private void nextRow() {
    turn--;
    startNewItem(turn);
  }

  private void addNewItem(Item item, Piece piece) {
    piece.setItem(item);
    gamePlayUI.addToGItem(item);
    item.setPosStart(piece.pos);
    item.moveToPos(piece.pos, .2f);
  }

  private void wrap(Piece pStart, Piece pEnd) {

    Item tmp = pStart.item;
    pStart.item = pEnd.item;
    pEnd.item = tmp;

    isWrap = true;

    pStart.item.addAction(
            moveTo(pStart.pos.x, pStart.pos.y, WRAP_ITEM, fastSlow)
    );

    pEnd.item.addAction(
            sequence(
                    moveTo(pEnd.pos.x, pEnd.pos.y, WRAP_ITEM, fastSlow),
                    run(this::filterAll),
                    run(() -> {
                      if (lsPieceNullItem.size() <= 0)
                        reverse(pStart, pEnd);
                    })
            )
    );

  }

  private void reverse(Piece pStart, Piece pEnd) {

    Item tmp = pStart.item;
    pStart.item = pEnd.item;
    pEnd.item = tmp;

    pStart.item.addAction(
            moveTo(pStart.pos.x, pStart.pos.y, WRAP_ITEM, fastSlow)
    );

    pEnd.item.addAction(
            sequence(
                    moveTo(pEnd.pos.x, pEnd.pos.y, WRAP_ITEM, fastSlow),
                    run(this::blockInput)
            )
    );

  }

  private void addSpecialItem(List<Piece> lsVer, List<Piece> lsHor, Piece pSpecial) {



  }

  private void blockInput() {
    isWrap = false;
    pieceStart = null;
    pieceEnd = null;
  }

}
