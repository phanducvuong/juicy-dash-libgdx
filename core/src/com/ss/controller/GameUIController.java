package com.ss.controller;

import static com.badlogic.gdx.math.Interpolation.*;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.ss.GMain;
import com.ss.config.Type;
import com.ss.core.action.exAction.GSimpleAction;
import com.ss.core.util.GUI;
import com.ss.gameLogic.effects.Particle;
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

  public boolean isWrap = false, isGameOver = false;
  private Piece pieceStart, pieceEnd;

  private int timeOut = 160, timeExpired = 36000, round = 0;
  private float sclTime;
  private long target, scorePre = 0;
  private boolean isCompleteRound = false;

  private List<Image> lsRayJam;

  public GameUIController(Group gParent) {

    this.gParent = gParent;
    this.gamePlayUI = new GamePlayUI(this);
    this.hmItem = new HashMap<>();
    this.lv = new ArrayList<>();
    this.lsRayJam = new ArrayList<>();
    this.target = TARGET;

    //label add ui to scene
    gParent.addActor(gamePlayUI);

    initPiece();
    initItem();
    initLv();
    initRayJam();

    eventTouchScreen();
//    addItem();

    //next level
    nextLevel();
    startNewItem(ROW-1);

    //label: test animation

    Image animJam = GUI.createImage(GMain.itemAtlas, "anim_jam");
    animJam.setOrigin(0, animJam.getHeight()/2);
//    gParent.addActor(animJam);

    Image icon = GUI.createImage(GMain.bgAtlas, "icon_pause");
    icon.setPosition(500, 20);
    gParent.addActor(icon);

    Particle iceP = new Particle(gParent, ICE_PARTICLE, GMain.particleAtlas);

    icon.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);
        Piece piece = arrPosPiece[2][2];
        Piece target = arrPosPiece[2][3];

        addItemAt(target, "item_jam");
        addItemAt(arrPosPiece[2][0], "item_glass_juice");

        iceP.start(piece.pos.x + piece.item.getWidth()/2, piece.pos.y + piece.item.getHeight()/2, .85f);

        piece.animIce();
      }
    });

    Image addItem = GUI.createImage(GMain.bgAtlas, "icon_pause");
    icon.setPosition(400, 20);
//    gParent.addActor(addItem);

    addItem.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);

      }
    });

  }

  //------------------init ui---------------------------------------------
  private void initRayJam() {
    for (int i=0; i<ROW*COL; i++)
      lsRayJam.add(GUI.createImage(GMain.itemAtlas, "anim_jam"));
  }

  private void initLv() {
    lv.add(Type.strawberry);
    lv.add(Type.orange);
    lv.add(Type.grape);
    lv.add(Type.banana);
    lv.add(Type.apple);
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
        lsItem.add(item);
      }

      hmItem.put(region, lsItem);

    }

  }
  //------------------init ui---------------------------------------------

  //------------------event click-----------------------------------------
  private void eventTouchScreen() {

    //label: drag and drop
    gamePlayUI.addListener(new DragListener() {

      @Override
      public void dragStart(InputEvent event, float x, float y, int pointer) {
        super.dragStart(event, x, y, pointer);

        if (!isGameOver)
          pieceStart = util.inRange(arrPosPiece, new Vector2(x, y));

      }

      @Override
      public void drag(InputEvent event, float x, float y, int pointer) {
        super.drag(event, x, y, pointer);

        if (!isGameOver && !gamePlayUI.isPause) {
          pieceEnd = util.inRange(arrPosPiece, new Vector2(x, y));
          if (!isWrap && pieceStart != null && pieceEnd != null && pieceEnd != pieceStart) {
//          util.log("start: ", pieceStart);
//          util.log("end: ", pieceEnd);

            if ((pieceStart.row == pieceEnd.row && (pieceStart.col == pieceEnd.col+1 || pieceStart.col == pieceEnd.col-1)) ||
                (pieceStart.col == pieceEnd.col && (pieceStart.row == pieceEnd.row+1 || pieceStart.row == pieceEnd.row-1))) {
              swap(pieceStart, pieceEnd);
              isWrap = true;
            }
          }
        }

      }

    });

    //label: click
    gamePlayUI.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);

        if (!isGameOver) {
          Vector2 pos = new Vector2(x, y);
          Piece pieceClick = util.inRange(arrPosPiece, pos);
//        System.out.println("CLICK: " + pieceClick.pos);
        }

      }
    });

  }
  //------------------event click-----------------------------------------

  //------------------new game--------------------------------------------
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

//    addItemAt(arrPosPiece[2][3], "item_glass_juice");
  }

  private void addItemAt(Piece piece, String key) {
    Item item = util.getItem(hmItem.get(key));
    clrPiece(piece);
    piece.setItem(item);
    item.setPosition(piece.pos);
    addToGroup(item, gamePlayUI.gItem);
  }
  //------------------new game--------------------------------------------

  //-------------------update array pos piece------------------------------
  private void filterAll() {

    List<Piece> lsPosIsMatch = new ArrayList<>();

    for (int i=0; i<ROW; i++) {
      for (int j=0; j<COL; j++) {

        //loại bỏ các item đã được check qua và match với nhau
        if (!lsPosIsMatch.contains(arrPosPiece[i][j])
            && !lsPosIsMatch.contains(arrPosPiece[i][j])
            && !arrPosPiece[i][j].isEmpty) {

          List<Piece> tmpV = util.filterVertically(arrPosPiece, arrPosPiece[i][j]);
          List<Piece> tmpH = util.filterHorizontally(arrPosPiece, arrPosPiece[i][j]);

          if (tmpV.size() >= 3) {
            lsPosIsMatch.addAll(tmpV);
            chkItemIsMatchByHor(tmpV, lsPosIsMatch);
          }
          else if (tmpH.size() >= 3) {
            lsPosIsMatch.addAll(tmpH);
            chkItemIsMatchByVer(tmpH, lsPosIsMatch);
          }

        }

      }
    }

    clrLsFilterPiece(lsPosIsMatch);
  }

  //add new item at piece is null item after filter
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
      updateScore();
      turn = ROW;
      nextRow();
    }

  }

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
      clrPiece(piece);
    }
  }

  private void clrPieceByVer(Piece piece) {
    //label: anim glass juice
    piece.animGlassJuice(false, false);

    for (int row=0; row<ROW; row++)
      if (arrPosPiece[row][piece.col] != piece && util.chkTypeFruit(arrPosPiece[row][piece.col]))
        clrPiece(arrPosPiece[row][piece.col]);
  }

  private void clrPieceByHor(Piece piece) {
    //label: anim glass juice
    piece.animGlassJuice(true, false);

    for (int col=0; col<COL; col++) {
      if (arrPosPiece[piece.row][col] != piece && util.chkTypeFruit(arrPosPiece[piece.row][col]))
        clrPiece(arrPosPiece[piece.row][col]);
    }
  }

  private void clrPieceByHorAndVer(Piece piece) {
    //label: anim glass juice
    piece.animGlassJuice(true, true);

    for (int col=0; col<COL; col++)
      clrPiece(arrPosPiece[piece.row][col]);

    for (int row=0; row<ROW; row++)
      clrPiece(arrPosPiece[row][piece.col]);
  }

  private void clrAll() {
    for (Piece[] pieces : arrPosPiece)
      for (Piece piece : pieces)
        clrPiece(piece);
  }

  private void skillJam(Piece pCheck, Piece point, Runnable onComplete) {
    List<Piece> targets = new ArrayList<>();
    for (Piece[] pieces : arrPosPiece) {
      for (Piece target : pieces)
        if (target.item != null && target.item.type == pCheck.item.type) {
          targets.add(target);
        }
    }//find the same type

    int indexRay = 0;
    for (int i=0; i<targets.size(); i++) {
      if (i == targets.size()-1) {
        addAnimJam(point, targets.get(i), lsRayJam.get(indexRay), onComplete);
      }
      else
        addAnimJam(point, targets.get(i), lsRayJam.get(indexRay), () -> {});
      indexRay++;
    }

  }

  private void clrPiece(Piece piece) {
    if (piece.item != null) {
      if (util.chkTypeFruit(piece))
        piece.item.startAnimFruit();
      else
        piece.item.remove();
    }
    piece.clear();
  }
  //-------------------update array pos piece------------------------------

  //-------------------animation || particle-------------------------------
  private void addAnimJam(Piece point, Piece target, Image ray, Runnable onComplete) {
    float degree    = Util.inst().calDegreeBy(point, target);
    Vector2 movePos = new Vector2();

    if (point.row == target.row && point.col > target.col) {
      movePos.x = target.item.getX() + target.item.getWidth()/2 + ray.getWidth();
      movePos.y = target.item.getY() + target.item.getHeight()/2 - ray.getHeight()/2;
    }
    else if (point.row == target.row && point.col < target.col) {
      movePos.x = target.item.getX() + target.item.getWidth()/2 - ray.getWidth();
      movePos.y = target.item.getY() + target.item.getHeight()/2 - ray.getHeight()/2;
    }
    else if (point.col == target.col && point.row > target.row) {
      movePos.x = target.item.getX() + target.item.getWidth()/2;
      movePos.y = target.item.getY() + target.item.getHeight()/2 + ray.getHeight();
    }
    else if (point.col == target.col && point.row < target.row) {
      movePos.x = target.item.getX() + target.item.getWidth()/2;
      movePos.y = target.item.getY() + target.item.getHeight()/2 - ray.getWidth();
    }
    else if (point.col > target.col && point.row > target.row) {
      movePos.x = target.item.getX() + target.item.getWidth();
      movePos.y = target.item.getY() + target.item.getHeight();
    }
    else if (point.col > target.col && point.row < target.row) {
      movePos.x = target.item.getX() + target.item.getWidth();
      movePos.y = target.item.getY();
    }
    else if (point.col < target.col && point.row > target.row) {
      movePos.x = target.item.getX();
      movePos.y = target.item.getY() + target.item.getHeight();
    }
    else {
      movePos.x = target.item.getX();
      movePos.y = target.item.getY();
    }

    ray.setRotation(0);
    ray.setPosition(point.pos.x + point.item.getWidth()/2,
            point.pos.y + point.item.getHeight()/2 - ray.getHeight()/2);
    ray.addAction(
            sequence(
                    rotateTo(degree, .35f, fastSlow),
                    delay(.35f),
                    moveTo(movePos.x, movePos.y,.15f, linear),
                    run(() -> clrPiece(target)),
                    run(onComplete),
                    run(ray::remove)
            )
    );

    //todo: thêm tiêu điểm vào tại vị trí target

    gamePlayUI.gAnim.addActor(ray);
  }
  //-------------------animation || particle-------------------------------

  //-------------------action add new item---------------------------------
  private void startNewItem(int row) {
    if (row >= 0)
      gamePlayUI.gBackground.addAction(GSimpleAction.simpleAction(this::action));
    else {
      //todo: fillAll
//      System.out.println("FINISHED!");
      gamePlayUI.gBackground.addAction(
              sequence(
                      delay(TIME_DELAY_TO_CHECK_ALL),
                      run(() -> {
                        if (scorePre >= target) {
                          isCompleteRound = true;
                          nextLevel();
                          //todo: next level
                        }
                        else {
                          filterAll();
                          updateArrPiece();
                        }
                      }),
                      run(() -> {
                        if (lsPieceNullItem.size() <= 0)
                          unlockInput();
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
    addToGroup(item, gamePlayUI.gItem);
    item.setPosStart(piece.pos);
    item.moveToPos(piece.pos, .2f);
  }
  //-------------------action add new item---------------------------------

  //-------------------swap and reverse item-------------------------------
  private void swap(Piece pStart, Piece pEnd) {

    if (pStart.item != null && pEnd.item != null) {
      Item tmp    = pStart.item;
      pStart.item = pEnd.item;
      pEnd.item   = tmp;
      isWrap      = true;

      pStart.item.addAction(
              moveTo(pStart.pos.x, pStart.pos.y, WRAP_ITEM, fastSlow)
      );

      pEnd.item.addAction(
              sequence(
                      moveTo(pEnd.pos.x, pEnd.pos.y, WRAP_ITEM, fastSlow),
                      run(() -> {
                        chkSpecialItemWhenSwap(pStart, pEnd);
                        if (lsPieceNullItem.size() <= 0)
                          reverse(pStart, pEnd);
                      })
              )
      );
    }

  }

  private void reverse(Piece pStart, Piece pEnd) {

    if (pStart.item != null && pEnd.item != null && util.chkTypeFruit(pStart) && util.chkTypeFruit(pEnd)) {
      Item tmp = pStart.item;
      pStart.item = pEnd.item;
      pEnd.item = tmp;

      pStart.item.addAction(
              sequence(
                      moveTo(pStart.pos.x, pStart.pos.y, WRAP_ITEM, fastSlow),
                      run(() -> pStart.item.anim0())
              )
      );

      pEnd.item.addAction(
              sequence(
                      moveTo(pEnd.pos.x, pEnd.pos.y, WRAP_ITEM, fastSlow),
                      run(() -> {
                        pEnd.item.anim0();
                        unlockInput();
                      })
              )
      );
    }

  }
  //-------------------swap and reverse item-------------------------------

  //-------------------special item----------------------------------------
  private void addSpecialItem(Piece piece, String key) {
    System.out.println("Add special");
    Item item = util.getItem(hmItem.get(key));
    clrPiece(piece);
    piece.setItem(item);
    item.setPosition(piece.pos);
    addToGroup(item, gamePlayUI.gItem);
  }
  //-------------------special item----------------------------------------

  //-------------------check logic-----------------------------------------
  //swap item which is the special item
  private void chkSpecialItemWhenSwap(Piece pStart, Piece pEnd) {

    if (pStart.item.type == Type.clock && pEnd.item.type == Type.clock) {
      System.out.println("x2 time");
      //todo: x2 time
      addTimeLine(ADD_SECOND*2);

      pStart.animClock(() -> {});
      pEnd.animClock(() -> {
        updateArrPiece();
      });
    }
    else if ((pStart.item.type == Type.clock || pEnd.item.type == Type.clock)
            && (pStart.item.type == Type.jam || pEnd.item.type == Type.jam)) {
      System.out.println("x1 time, jam");
      //todo: x1 time
      addTimeLine(ADD_SECOND);

      Piece point;
      Piece pClock;
      if (pStart.item.type == Type.jam) {
        point = pStart;
        pClock = pEnd;
      }
      else {
        point = pEnd;
        pClock = pStart;
      }

      pClock.animClock(() -> {});
      skillJam(util.getPieceTypeDifferenceWith(arrPosPiece, pStart, pEnd),
              point,
              () -> {
                point.animJam(() -> {});
                updateArrPiece();
              });

    }
    else if ((pStart.item.type == Type.clock || pEnd.item.type == Type.clock)
            && (pStart.item.type == Type.glass_fruit || pEnd.item.type == Type.glass_fruit)) {
      System.out.println("x1 time, glass juice");
      //todo: x1 time
      addTimeLine(ADD_SECOND);

      if (pStart.row == pEnd.row) {
        if (pStart.item.type == Type.glass_fruit) {
          clrPieceByHor(pStart);
          pEnd.animClock(() -> {});
        }
        else {
          clrPieceByHor(pEnd);
          pStart.animClock(() -> {});
        }
      }
      else {
        if (pStart.item.type == Type.glass_fruit) {
          clrPieceByVer(pStart);
          pEnd.animClock(() -> {});
        }
        else {
          clrPieceByVer(pEnd);
          pStart.animClock(() -> {});
        }
      }

      updateArrPiece();
    }
    else if (pStart.item.type == Type.clock && util.chkTypeFruit(pEnd)) {
      System.out.println("x1 time, normal item");
      //todo: x1 time
      addTimeLine(ADD_SECOND);

      pStart.animClock(() -> {});

      filterAll();
      clrPiece(pEnd);
      updateArrPiece();
    }
    else if (pEnd.item.type == Type.clock && util.chkTypeFruit(pStart)) {
      System.out.println("x1 time, normal item");
      //todo: x1 time
      addTimeLine(ADD_SECOND);

      pEnd.animClock(() -> {});

      filterAll();
      clrPiece(pStart);
      updateArrPiece();
    }
    else if (pStart.item.type == Type.jam && pEnd.item.type == Type.jam) {
      System.out.println("jam + jam");
      //todo: effect clear all

      pStart.animJam(() -> {});
      pEnd.animJam(() -> {});

      clrAll();
      updateArrPiece();
    }
    else if ((pStart.item.type == Type.jam || pEnd.item.type == Type.jam)
            && (pStart.item.type == Type.glass_fruit || pEnd.item.type == Type.glass_fruit)) {
      System.out.println("jam + glass juice");

      Piece point;
      if (pStart.item.type == Type.jam)
        point = pStart;
      else
        point = pEnd;

      skillJam(util.getPieceTypeDifferenceWith(arrPosPiece, pStart, pEnd), point,
              () -> {
                point.animJam(() -> {});
                updateArrPiece();
              }); //todo: effect jam + glass juice

      if (pStart.row == pEnd.row) {
        if (pStart.item.type == Type.glass_fruit)
          clrPieceByHor(pStart);
        else
          clrPieceByHor(pEnd);
      }
      else {
        if (pStart.item.type == Type.glass_fruit)
          clrPieceByVer(pStart);
        else
          clrPieceByVer(pEnd);
      }

    }
    else if (pStart.item.type == Type.jam && util.chkTypeFruit(pEnd)) {
      System.out.println("jam + fruit");
      skillJam(pEnd, pStart, () -> {
        pStart.animJam(() -> {});
        updateArrPiece();
      });
    }
    else if (pEnd.item.type == Type.jam && util.chkTypeFruit(pStart)) {
      System.out.println("jam + fruit");
      skillJam(pStart, pEnd, () -> {
        pEnd.animJam(() -> {});
        updateArrPiece();
      });
    }
    else if (pStart.item.type == Type.glass_fruit && pEnd.item.type == Type.glass_fruit) {
      System.out.println("glass juice + glass juice");
      pieceStart.item.remove();
      clrPieceByHorAndVer(pEnd);

      updateArrPiece();
    }
    else if (pStart.item.type == Type.glass_fruit && util.chkTypeFruit(pEnd)) {
      System.out.println("glass juice + fruit");
      if (pStart.row == pEnd.row)
        clrPieceByHor(pStart);
      else
        clrPieceByVer(pStart);

      updateArrPiece();
    }
    else if (pEnd.item.type == Type.glass_fruit && util.chkTypeFruit(pStart)) {
      System.out.println("glass juice + fruit");
      if (pStart.row == pEnd.row)
        clrPieceByHor(pEnd);
      else
        clrPieceByVer(pEnd);

      updateArrPiece();
    }
    else {
      System.out.println("normal + normal");
      filterAll();
      updateArrPiece();
    }

  }

  private void chkItemIsMatchByHor(List<Piece> pieces, List<Piece> saves) {

    boolean itemIsMatchHor = false;
    for (Piece piece : pieces) {
      List<Piece> tmpH = util.filterHorizontally(arrPosPiece, arrPosPiece[piece.row][piece.col]);
      if (tmpH.size() >= 3) {
        saves.addAll(tmpH);
        System.out.println("BEFORE VER: " + saves.size());
        util.removeItemAt(piece, saves);
        addSpecialItem(piece, "item_clock");
        itemIsMatchHor = true;
      }
    }

    if (!itemIsMatchHor) {
      if (pieceStart != null && pieces.contains(pieceStart) && pieces.size() >= 5) {
        saves.remove(pieceStart);
        addSpecialItem(pieceStart, "item_jam");
      }
      else if (pieceEnd != null && pieces.contains(pieceEnd) && pieces.size() >= 5) {
        saves.remove(pieceEnd);
        addSpecialItem(pieceEnd, "item_jam");
      }
      else if (pieceStart != null && pieces.contains(pieceStart) && pieces.size() == 4) {
        saves.remove(pieceStart);
        addSpecialItem(pieceStart, "item_glass_juice");
      }
      else if (pieceEnd != null && pieces.contains(pieceEnd) && pieces.size() == 4) {
        saves.remove(pieceEnd);
        addSpecialItem(pieceEnd, "item_glass_juice");
      }
      else {
        if (pieces.size() >= 5) {
          saves.remove(pieces.get(0));
          addSpecialItem(pieces.get(0), "item_jam");
        }
        else if (pieces.size() == 4) {
          saves.remove(pieces.get(0));
          addSpecialItem(pieces.get(0), "item_glass_juice");
        }
      }
    }

  }

  private void chkItemIsMatchByVer(List<Piece> pieces, List<Piece> saves) {

    boolean itemIsMatchVer = false;
    for (Piece piece : pieces) {
      List<Piece> tmpV = util.filterVertically(arrPosPiece, arrPosPiece[piece.row][piece.col]);
      if (tmpV.size() >= 3) {
        saves.addAll(tmpV);
        util.removeItemAt(piece, saves);
        System.out.println("BEFORE HOR: " + saves.size());
        addSpecialItem(piece, "item_clock");
        itemIsMatchVer = true;
      }
    }

    if (!itemIsMatchVer) {
      if (pieceStart != null && pieces.contains(pieceStart) && pieces.size() >= 5) {
        saves.remove(pieceStart);
        addSpecialItem(pieceStart, "item_jam");
      }
      else if (pieceEnd != null && pieces.contains(pieceEnd) && pieces.size() >= 5) {
        saves.remove(pieceEnd);
        addSpecialItem(pieceEnd, "item_jam");
      }
      else if (pieceStart != null && pieces.contains(pieceStart) && pieces.size() == 4) {
        saves.remove(pieceStart);
        addSpecialItem(pieceStart, "item_glass_juice");
      }
      else if (pieceEnd != null && pieces.contains(pieceEnd) && pieces.size() == 4) {
        saves.remove(pieceEnd);
        addSpecialItem(pieceEnd, "item_glass_juice");
      }
      else {
        if (pieces.size() >= 5) {
          saves.remove(pieces.get(0));
          addSpecialItem(pieces.get(0), "item_jam");
        }
        else if (pieces.size() == 4) {
          saves.remove(pieces.get(0));
          addSpecialItem(pieces.get(0), "item_glass_juice");
        }
      }
    }

  }

  //-------------------check logic-----------------------------------------

  public void updateTime() {

    if (!isCompleteRound) {
      if (timeOut > 0) {
        timeOut -= 1f;
        int minute = timeOut %3600/60;
        int second = timeOut %60;

        String time = minute + ":" + second;
        gamePlayUI.lbTime.setText(time);
        gamePlayUI.timeLine.clipBy(sclTime, 0f);
      }
      else {
        gameOver();
        gamePlayUI.lbTime.setText("0:0");
      }
    }

  }

  private void updateScore() {

    scorePre += (lsPieceNullItem.size() * SCORE_FRUIT);
    float sclTo = (float) scorePre / target;
    gamePlayUI.scoreLine.clipTo(sclTo, 1f);
    gamePlayUI.updateScore(scorePre);

  }

  //label: add time for clock
  private void addTimeLine(int second) {
    int temp = timeOut + second;
    if (temp > timeExpired) {
      timeOut = timeExpired;
      gamePlayUI.timeLine.reset(1f, 1f);
      updateTime();
    }
    else {
      timeOut = temp;
      gamePlayUI.addTimeLine(second*sclTime);
      updateTime();
    }
  }

  private double sclTime(float scl) {
    return 1d/scl;
  }

  private void nextLevel() {
    round += 1;
    timeOut = timeExpired;
    sclTime = (float) sclTime(timeOut);

    gamePlayUI.updateRound(round);
    gamePlayUI.timeLine.reset(1f, 1f);
    isCompleteRound = false;

    target *= 10;
    gamePlayUI.updateGoal(target);
    updateScore();

    //todo: unlock input
    unlockInput();
  }

  private void gameOver() {
    isGameOver = true;
    //todo: show popup game over
  }

  private void unlockInput() {
    isWrap = false;
    pieceStart = null;
    pieceEnd = null;
  }

  private void addToGroup(Item item, Group group) {
    group.addActor(item);
  }

}
