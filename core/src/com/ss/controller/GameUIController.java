package com.ss.controller;

import static com.badlogic.gdx.math.Interpolation.*;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.DragListener;
import com.ss.GMain;
import com.ss.config.Type;
import com.ss.core.action.exAction.GSimpleAction;
import com.ss.core.effect.SoundEffects;
import com.ss.core.util.GStage;
import com.ss.core.util.GUI;
import com.ss.gameLogic.effects.Particle;
import com.ss.objects.Item;
import com.ss.objects.Piece;
import com.ss.scenes.GameScene;
import com.ss.ui.GamePlayUI;
import com.ss.ui.PauseUI;
import com.ss.ui.TutorialUI;
import com.ss.utils.Solid;
import com.ss.utils.Util;
import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import static com.ss.config.Config.*;

public class GameUIController {

  private Util        util = Util.inst();
  public  Group       gParent;
  public  GameScene   scene;
  private GamePlayUI  gamePlayUI;
  public  PauseUI     pauseUI;
  private TutorialUI  tutorialUI;
  public  Image       blackScreen;

  public  Piece[][]                   arrPosPiece = new Piece[ROW][COL];
  public  HashMap<String, List<Item>> hmItem;
  private List<Piece>                 lsPieceNullItem = new ArrayList<>();
  private int                         turn = ROW;
  private List<Type>                  lv;

  public boolean  isWrap      = true,
                  isGameOver  = false,
                  unlockClick = false,   //unlock click gamePlayUI to use skill item
                  isPause     = false;
  private Piece   pieceStart, pieceEnd;

  private int     timeExpired,
                  timeOut           = 0;
  public  int     round             = 0;
  private float   sclTime;
  public  long    target,
                  scorePre          = 0; //điểm hiện tại của user
  private long    dtScore           = 0, //target tăng lên mỗi khi user qua màn (dtScore dùng để tính clipping)
                  sumScoreEachTurn  = 0, //cộng dồn điểm mỗi khi match => dùng để tính clipping
                  targetIncrease,
                  tmpScore,              //tmpScore: điểm mỗi khi ăn trái cây để update scorePre
                  countScoreToShowWonder;              //tmpScore: điểm mỗi khi ăn trái cây để update scorePre

  public boolean  isCompleteRound         = true,
                  isTutorial;

  public int      amountItemStar,
                  amountItemBoom;

  private List<Image>     lsRayJam;
  private Particle        pBurnAll;
  private List<Particle>  lsParticleIce, lsParticleBurnJam;

  public GameUIController(GameScene gameScene) {

    this.gParent                  = gameScene.gParent;
    this.scene                    = gameScene;

    this.amountItemStar           = GMain.pref.getInteger("amount_item_star");
    this.amountItemBoom           = GMain.pref.getInteger("amount_item_boom");
    this.isTutorial               = GMain.pref.getBoolean("is_tutorial");

    this.gamePlayUI               = new GamePlayUI(this);
    this.pauseUI                  = new PauseUI(this);
    this.tutorialUI               = new TutorialUI(this);
    this.blackScreen              = new Image(Solid.create(new Color(128/255f, 213/255f, 181/255f, .45f)));
    this.blackScreen.setSize(GStage.getWorldWidth(), GStage.getWorldHeight());

    this.hmItem                   = new HashMap<>();
    this.lv                       = new ArrayList<>();
    this.lsRayJam                 = new ArrayList<>();
    this.lsParticleIce            = new ArrayList<>();
    this.lsParticleBurnJam        = new ArrayList<>();

    this.target                   = 0;
    this.targetIncrease           = TARGET;
    this.tmpScore                 = 0;
    this.timeExpired              = TIME_START_GAME;

    //label add ui to scene
    gParent.addActor(gamePlayUI);

    initPiece();
    initItem();
    initLv();
    initRayJam();
    initParticle();
    eventTouchScreen();

    //label: test animation

    Image animJam = GUI.createImage(GMain.itemAtlas, "anim_jam");
    animJam.setOrigin(0, animJam.getHeight()/2);
//    gParent.addActor(animJam);

    Image icon = GUI.createImage(GMain.bgAtlas, "icon_pause");
    icon.setPosition(500, 20);
    gParent.addActor(icon);

    Particle wonder = new Particle(gParent, WONDER, GMain.particleAtlas);
    wonder.initLsEmitter();

    icon.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);

//        gamePlayUI.animItemStar(arrPosPiece[0][0].pos, () -> {});

      }
    });

  }

  //------------------init ui---------------------------------------------
  private void initRayJam() {
    for (int i=0; i<ROW*COL; i++)
      lsRayJam.add(GUI.createImage(GMain.itemAtlas, "anim_jam"));
  }

  private void initParticle() {
    pBurnAll = new Particle(gamePlayUI.gAnimSkill, BURN_ALL, GMain.particleAtlas);

    for (int i=0; i<14; i++)
      lsParticleIce.add(new Particle(gamePlayUI.gAnimSkill, ICE_PARTICLE, GMain.particleAtlas));

    for (int i=0; i<ROW*COL; i++)
      lsParticleBurnJam.add(new Particle(gamePlayUI.gAnimSkill, BURN_JAM, GMain.particleAtlas));
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
        Item item = new Item(region, type, gamePlayUI.gAnimLb);
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

        if (!isGameOver && !unlockClick)
          pieceStart = util.inRange(arrPosPiece, new Vector2(x, y));

      }

      @Override
      public void drag(InputEvent event, float x, float y, int pointer) {
        super.drag(event, x, y, pointer);

        if (!isGameOver && !isPause && !unlockClick) {
          pieceEnd = util.inRange(arrPosPiece, new Vector2(x, y));
          if (!isWrap && pieceStart != null && pieceEnd != null && pieceEnd != pieceStart) {

            if ((pieceStart.row == pieceEnd.row && (pieceStart.col == pieceEnd.col+1 || pieceStart.col == pieceEnd.col-1)) ||
                (pieceStart.col == pieceEnd.col && (pieceStart.row == pieceEnd.row+1 || pieceStart.row == pieceEnd.row-1))) {
              swap(pieceStart, pieceEnd);

              isWrap = true;
              gamePlayUI.setTouchableItemSkill(Touchable.disabled);
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

        if (unlockClick) {
          Piece piece = util.getPieceByCoordinate(x, y, arrPosPiece);
          if (piece != null && gamePlayUI.isChooseBoom)
            skillBoom(piece);
          else if (piece != null && gamePlayUI.isChooseStar)
            skillStar(piece);
        }

      }
    });

  }
  //------------------event click-----------------------------------------

  //------------------new game--------------------------------------------
  private void addItem() {

    List<Item> lsItem = util.getLsItem(hmItem, lv);
    Collections.shuffle(lsItem, new Random());

    int count = 0;
    for (int i=ROW-1; i>=0; i--) {
      for (int j = 0; j< COL; j++) {

        Piece piece = util.getPieceEmpty(arrPosPiece, i, j);
        addNewItem(piece);

        count++;

      }
    }

//    addItemAt(arrPosPiece[2][3], "item_glass_juice");
  }

  private void addItemAt(Piece piece, String key) {
    Item item = util.getItem(hmItem.get(key));
    clrPiece(piece, false);
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
        if (!lsPosIsMatch.contains(arrPosPiece[i][j]) &&
            !lsPosIsMatch.contains(arrPosPiece[i][j]) &&
            !arrPosPiece[i][j].isEmpty                &&
            util.chkTypeFruit(arrPosPiece[i][j])) {

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
  private void updateBoard() {

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
        if (piece.item == null)
          lsPieceNullItem.add(piece);
      }
    }

//    addItemSequence(lsPieceNullItem, ROW-1, .75f);
    if (lsPieceNullItem.size() > 0) {
      countScoreToShowWonder += lsPieceNullItem.size();
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

        item.addAction(
                sequence(
                        delay(.195f),
                        run(() -> SoundEffects.start(util.rndSoundDrop(), DROP_VOLUME))
                )
        );
        break;
      }
    }
  }

  private void clrLsFilterPiece(List<Piece> filter) {
    for (Piece piece : filter) {
      tmpScore += SCORE_FRUIT;
      clrPiece(piece, false);
    }
  }

  private void clrPieceByVer(Piece piece, Runnable onUpdateBoard) {

    SoundEffects.start("freezing", FREEZING_VOLUME);

    //label: anim glass juice
    piece.animGlassJuice(false, false, onUpdateBoard);

    for (int row=0; row<ROW; row++) {
      Piece    pCheck  = arrPosPiece[row][piece.col];
      Particle pIce    = lsParticleIce.get(row);
      if (pCheck != piece && pCheck.item != null && util.chkTypeFruit(pCheck)) {
        tmpScore += SCORE_SKILL_GLASS_JUICE;
        pCheck.setScore(SCORE_SKILL_GLASS_JUICE);
        pCheck.animIce(pIce);
      }
      else if (pCheck != piece && pCheck.item != null && pCheck.item.type == Type.glass_fruit) {
        gParent.addAction(
                sequence(
                        delay(1f),
                        run(pCheck::animLbScore)
                )
        );
      }
      else if (pCheck != piece && pCheck.item != null && pCheck.item.type == Type.jam)
        gParent.addAction(
                sequence(
                        delay(1f),
                        run(pCheck::animLbScore)
                )
        );
      else if (pCheck != piece && pCheck.item != null && pCheck.item.type == Type.clock)
        gParent.addAction(
                sequence(
                        delay(1f),
                        run(() -> pCheck.animClock(() -> {}))
                )
        );
    }
  }

  private void clrPieceByHor(Piece piece, Runnable onUpdateBoard) {

    SoundEffects.start("freezing", FREEZING_VOLUME);

    //label: anim glass juice
    piece.animGlassJuice(true, false, onUpdateBoard);

    for (int col=0; col<COL; col++) {
      Piece    pCheck = arrPosPiece[piece.row][col];
      Particle pIce   = lsParticleIce.get(col);
      if (pCheck != piece && pCheck.item!= null && util.chkTypeFruit(pCheck)) {
        tmpScore += SCORE_SKILL_GLASS_JUICE;
        pCheck.setScore(SCORE_SKILL_GLASS_JUICE);
        pCheck.animIce(pIce);
      }
      else if (pCheck != piece && pCheck.item!= null && pCheck.item.type == Type.glass_fruit) {
        gParent.addAction(
                sequence(
                        delay(1f),
                        run(pCheck::animLbScore)
                )
        );
      }
      else if (pCheck != piece && pCheck.item!= null && pCheck.item.type == Type.jam) {
        gParent.addAction(
                sequence(
                        delay(1f),
                        run(pCheck::animLbScore)
                )
        );
      }
      else if (pCheck != piece && pCheck.item!= null && pCheck.item.type == Type.clock) {
        gParent.addAction(
                sequence(
                        delay(1f),
                        run(() -> pCheck.animClock(() -> {}))
                )
        );
      }
    }
  }

  private void clrPieceByHorAndVer(Piece piece, Runnable onUpdateBoard) {

    SoundEffects.start("freezing", FREEZING_VOLUME);

    //label: anim glass juice
    piece.animGlassJuice(true, true, onUpdateBoard);

    int indexParticle = 0;
    for (int col=0; col<COL; col++) {
      Piece    pCheck = arrPosPiece[piece.row][col];
      Particle pIce   = lsParticleIce.get(indexParticle);
      indexParticle++;

      if (pCheck != piece && pCheck.item != null && util.chkTypeFruit(pCheck)) {
        tmpScore += SCORE_SKILL_GLASS_JUICE;
        pCheck.setScore(SCORE_SKILL_GLASS_JUICE + 20);
        pCheck.animIce(pIce);
      }
      else if (pCheck != piece && pCheck.item != null && pCheck.item.type == Type.glass_fruit) {
        gParent.addAction(
                sequence(
                        delay(1f),
                        run(pCheck::animLbScore)
                )
        );
      }
      else if (pCheck != piece && pCheck.item != null && pCheck.item.type == Type.jam) {
        gParent.addAction(
                sequence(
                        delay(1f),
                        run(pCheck::animLbScore)
                )
        );
      }
      else if (pCheck != piece && pCheck.item != null && pCheck.item.type == Type.clock) {
        gParent.addAction(
                sequence(
                        delay(1f),
                        run(() -> pCheck.animClock(() -> {}))
                )
        );
      }
    }

    for (int row=0; row<ROW; row++) {
      Piece    pCheck = arrPosPiece[row][piece.col];
      Particle pIce   = lsParticleIce.get(indexParticle);
      indexParticle++;

      if (pCheck != piece && pCheck.item!= null && util.chkTypeFruit(pCheck)) {
        tmpScore += SCORE_SKILL_GLASS_JUICE;
        pCheck.setScore(SCORE_SKILL_GLASS_JUICE + 20);
        pCheck.animIce(pIce);
      }
      else if (pCheck != piece && pCheck.item!= null && pCheck.item.type == Type.glass_fruit) {
        gParent.addAction(
                sequence(
                        delay(1f),
                        run(pCheck::animLbScore)
                )
        );
      }
      else if (pCheck != piece && pCheck.item!= null && pCheck.item.type == Type.jam) {
        gParent.addAction(
                sequence(
                        delay(1f),
                        run(pCheck::animLbScore)
                )
        );
      }
      else if (pCheck != piece && pCheck.item!= null && pCheck.item.type == Type.clock) {
        gParent.addAction(
                sequence(
                        delay(1f),
                        run(() -> pCheck.animClock(() -> {}))
                )
        );
      }
    }
  }

  private void clrAll(Piece pStart, Piece pEnd) {
    for (Piece[] pieces : arrPosPiece)
      for (Piece piece : pieces) {
        if (piece != pStart && piece != pEnd) {
          tmpScore += SCORE_SKILL_JAM + 40;
          piece.setScore(SCORE_SKILL_JAM + 40);
          clrPiece(piece, true);
        }
      }
  }

  private void clrAll() {
    for (Piece[] pieces : arrPosPiece) {
      for (Piece piece : pieces) {
        if (piece.item != null) {
          piece.stopAnimZoomAndVibration();
          piece.clear();
        }
      }
    }
  }

  private void lvSuccess(Runnable onNextLv) {
    gamePlayUI.animComplete(onNextLv);
    for (Piece[] pieces : arrPosPiece) {
      for (Piece piece : pieces)
        if (piece.item != null) {
          if (util.chkTypeFruit(piece))
            piece.animLvSuccess();
          else {
            piece.item.remove();
            piece.clear();
          }
        }
    }
  }

  private void skillJam(Piece pCheck, Piece point, Runnable onComplete) {

    gamePlayUI.gBackground.addAction(
            sequence(
                    delay(1f),
                    run(() -> SoundEffects.start("jam", JAM_VOLUME))
            )
    );

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
        addAnimJam(point, targets.get(i),
                   lsRayJam.get(indexRay),
                   lsParticleBurnJam.get(indexRay),
                   onComplete);
      }
      else
        addAnimJam(point, targets.get(i),
                   lsRayJam.get(indexRay),
                   lsParticleBurnJam.get(indexRay),
                   () -> {});
      indexRay++;
    }

  }

  private void skillBoom(Piece piece) {
    gamePlayUI.setTouchableItemSkill(Touchable.disabled);
    amountItemBoom -= 1;
    int row = piece.row,
        col = piece.col;

    util.saveData(amountItemStar, "amount_item_boom");

    List<Piece> lsPieceAffectBoom = getPieceAffectBoom(row, col);
    if (lsPieceAffectBoom != null) {
      //label: show particle boom
      gamePlayUI.animItemBoom(piece.pos, () -> {
        for (Piece p : lsPieceAffectBoom) {
          if (p.item.type == Type.clock) {
            tmpScore += SCORE_CLOCK;
            updateTimeLine(ADD_SECOND);
          }
          else if (p.item.type == Type.glass_fruit)
            tmpScore += SCORE_GLASS_JUICE;
          else if (p.item.type == Type.jam)
            tmpScore += SCORE_JAM;
          else
            tmpScore += SCORE_FRUIT;

          p.stopAnimZoomAndVibration();
          clrPiece(p, false);
        }

        SoundEffects.start("skill_boom", SKILL_BOOM_VOLUME);

        Vector2 posShowBoom = lsPieceAffectBoom.get(4).pos;
        gamePlayUI.pBoom.start(posShowBoom.x + WIDTH_PIECE/2, posShowBoom.y + HEIGHT_PIECE/2, .5f);
        scene.shake();

        gamePlayUI.updateAmountSkill();
        stopAnimZoomAndVibrateOnBoard();
        updateBoard();
      });
    }
  }

  private void skillStar(Piece piece) {
    gamePlayUI.setTouchableItemSkill(Touchable.disabled);
    gamePlayUI.addAction(
            sequence(
                    delay(.625f),
                    run(() -> {
                      piece.stopAnimZoomAndVibration();
                      clrPiece(piece, false);

                      SoundEffects.start("skill_star", SKILL_STAR_VOLUME);
                      gamePlayUI.pStar.start(piece.pos.x + WIDTH_PIECE/2,
                              piece.pos.y + HEIGHT_PIECE/2, .35f);
                    })
            )
    );
    gamePlayUI.animItemStar(piece.pos, () -> {
      amountItemStar -= 1;
      util.saveData(amountItemStar, "amount_item_star");
      gamePlayUI.updateAmountSkill();
      stopAnimZoomAndVibrateOnBoard();
      updateBoard();
    });
  }

  private List<Piece> getPieceAffectBoom(int row, int col) {
    if (row == 0 && col == 0)
      return util.getRallyPieceBy(row, col, arrPosPiece);
    else if (row == ROW-1 && col == 0)
      return util.getRallyPieceBy(row-2, col, arrPosPiece);
    else if (row == ROW-1 && col == COL-1)
      return util.getRallyPieceBy(row-2, col-2, arrPosPiece);
    else if (row == 0 && col == COL-1)
      return util.getRallyPieceBy(row, col-2, arrPosPiece);
    else if (row == 0 && col > 0 && col < COL-1)
      return util.getRallyPieceBy(row, col-1, arrPosPiece);
    else if (row > 0 && row < ROW-1 && col == 0)
      return util.getRallyPieceBy(row-1, col, arrPosPiece);
    else if (row == ROW-1 && col > 0 && col < COL-1)
      return util.getRallyPieceBy(row-2, col-1, arrPosPiece);
    else if (row > 0 && row < ROW-1 && col == COL-1)
      return util.getRallyPieceBy(row-1, col-2, arrPosPiece);
    else if (row > 0 && row < ROW-1 && col > 0 && col < COL-1)
      return util.getRallyPieceBy(row-1, col-1, arrPosPiece);
    return null;
  }

  private void clrPiece(Piece piece, boolean isSpeciaItem) { //isSpecialItem to play sound effect
    if (piece.item != null) {
      if (util.chkTypeFruit(piece))
        piece.item.startAnimFruit(isSpeciaItem);
      else {
        piece.item.animLbScore();
        piece.item.remove();
      }
    }
    piece.clear();
  }
  //-------------------update array pos piece------------------------------

  //-------------------animation || particle-------------------------------
  private void addAnimJam(Piece point, Piece target, Image ray, Particle pBurn, Runnable onComplete) {
    float   degree  = Util.inst().calDegreeBy(point, target);
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
                    delay(.5f),
                    moveTo(movePos.x, movePos.y,.15f, linear),
                    run(() -> {
                      scene.shake();
                      if (target.item != null) {
                        float x = target.item.getX() + target.item.getWidth()/2;
                        float y = target.item.getY() + target.item.getHeight()/2;
                        tmpScore += SCORE_SKILL_JAM;
                        target.setScore(SCORE_SKILL_JAM);
                        pBurn.start(x, y,.5f);
                      }
                      clrPiece(target, true);
                    }),
                    run(onComplete),
                    run(ray::remove)
            )
    );

    //todo: thêm tiêu điểm vào tại vị trí target

    gamePlayUI.gAnimSkill.addActor(ray);
  }

  public void startAnimZoomAndVibrateOnBoard() {
    for (Piece[] pieces : arrPosPiece) {
      for (Piece piece  : pieces) {
        if (util.chkTypeFruit(piece))
          piece.animZoomAndVibration();
      }
    }
  }

  public void stopAnimZoomAndVibrateOnBoard() {
    for (Piece[] pieces : arrPosPiece) {
      for (Piece piece  : pieces) {
        if (util.chkTypeFruit(piece))
          piece.stopAnimZoomAndVibration();
      }
    }
  }
  //-------------------animation || particle-------------------------------

  //-------------------action add new item---------------------------------
  private void startNewItem(int row) {
    if (row >= 0) {
      if (isTutorial)
        gamePlayUI.gBackground.addAction(GSimpleAction.simpleAction(this::actionTutorial));
      else
        gamePlayUI.gBackground.addAction(GSimpleAction.simpleAction(this::action));
    }
    else {
      //todo: fillAll
//      System.out.println("FINISHED!");
      gamePlayUI.gBackground.addAction(
              sequence(
                      delay(TIME_DELAY_TO_CHECK_ALL),
                      run(() -> {
                        if (scorePre >= target && !isGameOver) {
                          isCompleteRound = true;
                          nextLevel();
                          //todo: next level
                        }
                        else if (!isGameOver) {
                          filterAll();
                          updateBoard();
                        }
                      }),
                      run(() -> {
                        if (lsPieceNullItem.size() <= 0) {
                          //todo: show tutorial
                          if (isTutorial)
                            tutorialUI.showTutorial();
                          showWonder();
                          unlockInput();
                          countScoreToShowWonder = 0;
                        }
                      })
              )
      );
    }
  }

  private boolean actionTutorial(float dt, Actor a) {
    for (Piece piece : lsPieceNullItem) {
      for (int col=0; col<COL; col++) {
        if (turn >= 0 && turn < ROW && arrPosPiece[turn][col] == piece) {
          Item item = util.getItemBy(hmItem, itemTutorial[turn][col]);
          addNewItemBy(piece, item);
        }
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

  private boolean action(float dt, Actor a) {

    for (Piece piece : lsPieceNullItem) {
      for (int col=0; col<COL; col++) {
        if (turn >= 0 && turn < ROW && arrPosPiece[turn][col] == piece)
          addNewItem(piece);
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

  private void addNewItemBy(Piece piece, Item item) {
    piece.setItem(item);
    addToGroup(item, gamePlayUI.gItem);
    item.setPosStart(piece.pos);
    item.moveToPos(piece.pos, .2f);

    item.addAction(
            sequence(
                    delay(.195f),
                    run(() -> SoundEffects.start(util.rndSoundDrop(), DROP_VOLUME))
            )
    );
  }

  private void addNewItem(Piece piece) {
    Item item = util.getRndItem(hmItem, lv);
    piece.setItem(item);
    addToGroup(item, gamePlayUI.gItem);
    item.setPosStart(piece.pos);
    item.moveToPos(piece.pos, .2f);

    item.addAction(
            sequence(
                    delay(.195f),
                    run(() -> SoundEffects.start(util.rndSoundDrop(), DROP_VOLUME))
            )
    );
  }
  //-------------------action add new item---------------------------------

  //-------------------swap and reverse item-------------------------------
  public void swap(Piece pStart, Piece pEnd) {

    if (pStart.item != null && pEnd.item != null) {
      Item tmp    = pStart.item;
      pStart.item = pEnd.item;
      pEnd.item   = tmp;
      isWrap      = true;

      pStart.item.addAction(
              moveTo(pStart.pos.x, pStart.pos.y, WRAP_ITEM, fastSlow)
      );

      SoundEffects.start("exchange", EXCHANGE_VOLUME);
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

      SoundEffects.start("exchange", EXCHANGE_VOLUME);
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
//    System.out.println("Add special");
    Item item = util.getItem(hmItem.get(key));
    clrPiece(piece, true);
    piece.setItem(item);
    item.setPosition(piece.pos);
    addToGroup(item, gamePlayUI.gItem);
  }
  //-------------------special item----------------------------------------

  //-------------------check logic-----------------------------------------
  //swap item which is the special item
  private void chkSpecialItemWhenSwap(Piece pStart, Piece pEnd) {

    if (pStart.item != null && pEnd.item != null) {
      if (pStart.item.type == Type.clock && pEnd.item.type == Type.clock) {
//      System.out.println("x2 time");
        //todo: x2 time
        updateTimeLine(ADD_SECOND*2);
        tmpScore += SCORE_CLOCK*2;

        pStart.animClock(() -> {});
        pEnd.animClock(this::updateBoard);
      }
      else if ((pStart.item.type == Type.clock || pEnd.item.type == Type.clock)
              && (pStart.item.type == Type.jam || pEnd.item.type == Type.jam)) {
//      System.out.println("x1 time, jam");
        //todo: x1 time
        updateTimeLine(ADD_SECOND);
        tmpScore += SCORE_CLOCK;

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
                  updateBoard();
                });

      }
      else if ((pStart.item.type == Type.clock || pEnd.item.type == Type.clock)
              && (pStart.item.type == Type.glass_fruit || pEnd.item.type == Type.glass_fruit)) {
//      System.out.println("x1 time, glass juice");
        //todo: x1 time
        updateTimeLine(ADD_SECOND);
        tmpScore += SCORE_CLOCK;

        if (pStart.row == pEnd.row) {
          if (pStart.item.type == Type.glass_fruit) {
            clrPieceByHor(pStart, this::updateBoard);
          }
          else {
            clrPieceByHor(pEnd, this::updateBoard);
          }
        }
        else {
          if (pStart.item.type == Type.glass_fruit) {
            clrPieceByVer(pStart, this::updateBoard);
          }
          else {
            clrPieceByVer(pEnd, this::updateBoard);
          }
        }
      }
      else if (pStart.item.type == Type.clock && util.chkTypeFruit(pEnd)) {
//      System.out.println("x1 time, normal item");
        //todo: x1 time
        updateTimeLine(ADD_SECOND);
        tmpScore += SCORE_CLOCK;

        pStart.animClock(() -> {});

        filterAll();
        clrPiece(pEnd, false);
        updateBoard();
      }
      else if (pEnd.item.type == Type.clock && util.chkTypeFruit(pStart)) {
//      System.out.println("x1 time, normal item");
        //todo: x1 time
        updateTimeLine(ADD_SECOND);
        tmpScore += SCORE_CLOCK;

        pEnd.animClock(() -> {});

        filterAll();
        clrPiece(pStart, false);
        updateBoard();
      }
      else if (pStart.item.type == Type.jam && pEnd.item.type == Type.jam) {
//      System.out.println("jam + jam");
        //todo: effect clear all

        SoundEffects.start("stamp", STAMP_VOLUME);
        gamePlayUI.showPWonder(2);
        pBurnAll.start(GStage.getWorldWidth()/2, GStage.getWorldHeight()/2, 4f);
        scene.shake();
        pStart.animJam(() -> {});
        pEnd.animJam(() -> {});
        clrAll(pStart, pEnd);

        gamePlayUI.gBackground.addAction(
                sequence(
                        delay(1f),
                        run(this::updateBoard)
                )
        );

      }
      else if ((pStart.item.type == Type.jam || pEnd.item.type == Type.jam)
              && (pStart.item.type == Type.glass_fruit || pEnd.item.type == Type.glass_fruit)) {
//      System.out.println("jam + glass juice");

        Piece point;
        if (pStart.item.type == Type.jam)
          point = pStart;
        else
          point = pEnd;

        Item itemPoint = point.item;
        skillJam(util.getPieceTypeDifferenceWith(arrPosPiece, pStart, pEnd), point,
                () -> {
                  itemPoint.animJam(() -> {});
                  updateBoard();
                }); //todo: effect jam + glass juice

        if (pStart.row == pEnd.row) {
          if (pStart.item.type == Type.glass_fruit)
            clrPieceByHor(pStart, () -> {});
          else
            clrPieceByHor(pEnd, () -> {});
        }
        else {
          if (pStart.item.type == Type.glass_fruit)
            clrPieceByVer(pStart, () -> {});
          else
            clrPieceByVer(pEnd, () -> {});
        }

      }
      else if (pStart.item.type == Type.jam && util.chkTypeFruit(pEnd)) {
//      System.out.println("jam + fruit");
        skillJam(pEnd, pStart, () -> {
          pStart.animJam(() -> {});
          updateBoard();
        });
      }
      else if (pEnd.item.type == Type.jam && util.chkTypeFruit(pStart)) {
//      System.out.println("jam + fruit");
        skillJam(pStart, pEnd, () -> {
          pEnd.animJam(() -> {});
          updateBoard();
        });
      }
      else if (pStart.item.type == Type.glass_fruit && pEnd.item.type == Type.glass_fruit) {
//      System.out.println("glass juice + glass juice");
//      clrPiece(pStart);
        clrPieceByHorAndVer(pEnd, this::updateBoard);
      }
      else if (pStart.item.type == Type.glass_fruit && util.chkTypeFruit(pEnd)) {
//      System.out.println("glass juice + fruit");
        filterAll();
        if (pStart.row == pEnd.row)
          clrPieceByHor(pStart, this::updateBoard);
        else
          clrPieceByVer(pStart, this::updateBoard);
      }
      else if (pEnd.item.type == Type.glass_fruit && util.chkTypeFruit(pStart)) {
//      System.out.println("glass juice + fruit");
        filterAll();
        if (pStart.row == pEnd.row)
          clrPieceByHor(pEnd, this::updateBoard);
        else
          clrPieceByVer(pEnd, this::updateBoard);
      }
      else {
//      System.out.println("normal + normal");
        filterAll();
        updateBoard();
      }
    }

  }

  private void chkItemIsMatchByHor(List<Piece> pieces, List<Piece> saves) {

    boolean itemIsMatchHor = false;
    for (Piece piece : pieces) {
      List<Piece> tmpH = util.filterHorizontally(arrPosPiece, arrPosPiece[piece.row][piece.col]);
      if (tmpH.size() >= 3) {
        saves.addAll(tmpH);
//        System.out.println("BEFORE VER: " + saves.size());
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
//        System.out.println("BEFORE HOR: " + saves.size());
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

  public void updateLbTimeLine() {

    if (!isCompleteRound) {
      if (timeOut > 0) {
        timeOut -= 1f;
        int minute = timeOut % 3600/60;
        int second = timeOut % 60;

        String ss = second+"";
        if (second < 10)
          ss = "0" + second;

        String time = minute + ":" + ss;
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
    scorePre    += tmpScore;
    sumScoreEachTurn += tmpScore;
    float sclTo = (float) sumScoreEachTurn / dtScore;
    tmpScore    = 0;
    gamePlayUI.scoreLine.clipTo(sclTo, 1f);
    gamePlayUI.updateScore(scorePre);

    //todo: tính lại scl score bắt đầu từ 0 -> 1
  }

  //label: add time for clock
  private void updateTimeLine(int second) {

    SoundEffects.start("clock", CLOCK_VOLUME);

    int temp = timeOut + second;
    if (temp > timeExpired) {
      timeOut = timeExpired;
      gamePlayUI.timeLine.reset(1f, 1f);
      updateLbTimeLine();
    }
    else {
      timeOut = temp;
      gamePlayUI.addTimeLine(second*sclTime);
      updateLbTimeLine();
    }
  }

  private double sclTime(float scl) {
    return 1d/scl;
  }

  private void nextLevel() {

    Runnable onNextLv = () -> {
      isCompleteRound   = false;
      updateBoard();
      countScoreToShowWonder = 0;
    };

    //label: reset time
    if (round > 0)
      timeExpired -= TIME_MINUS;
    if (timeExpired <= MIN_TIME_EXPIRED)
      timeExpired = MIN_TIME_EXPIRED;
    timeOut = timeExpired;
    sclTime = (float) sclTime(timeOut);
    gamePlayUI.setTimeLineForNewRound(timeOut);

    //label: reset score
    if (round == 0)
      targetIncrease = TARGET;
    else if (targetIncrease >= MAX_TARGET_INCREASE)
      targetIncrease = MAX_TARGET_INCREASE;
    else
      targetIncrease += TARGET_INCREASE;
    target += targetIncrease;

    if (scorePre >= target)
      target = scorePre + targetIncrease;
    dtScore = target - scorePre;
    sumScoreEachTurn  = 0;

    gamePlayUI.updateGoal(target);
    updateScore();

    //label: reset round
    round += 1;
    gamePlayUI.updateRound(round);
    gamePlayUI.timeLine.reset(1f, 1f);

    if (round == 10 && !util.chkTypeIn(lv, Type.kiwi))
      lv.add(Type.kiwi);

    if (round == 1)
      gamePlayUI.animLbRound(onNextLv);
    else
      lvSuccess(onNextLv);

  }

  public void continueWhenWatchAdsGetTime() {
    unlockClick      = false;
    isGameOver       = false;
    isPause          = false;
    isWrap           = false;
    pieceStart       = null;
    pieceEnd         = null;
    updateTimeLine(TIME_WATCH_ADS);
  }

  private void gameOver() {
    isGameOver              = true;
    if (GMain.platform.isVideoRewardReady())
      gamePlayUI.showPopupAdsTime();
    else
      gamePlayUI.showPopupGameOver();
    //todo: show popup game over
  }

  private void resetGame() {
    unlockClick             = false;
    isCompleteRound         = true;
    timeExpired             = TIME_START_GAME;
    target                  = 0;
    scorePre                = 0;
    tmpScore                = 0;
    targetIncrease          = TARGET;
    timeOut                 = 0;
    round                   = 0;
    dtScore                 = 0;
    sumScoreEachTurn        = 0;
    countScoreToShowWonder  = 0;

    gamePlayUI.setTouchableItemSkill(Touchable.disabled);
  }

  public void newGame() {
    refactoryAllItemInGame();
    resetGame();
    isGameOver = false;
    nextLevel();
  }

  private void refactoryAllItemInGame() {
    gParent.clearActions();
    gamePlayUI.clearActionAllObject();
    clrAll();

    for (List<Item> items : hmItem.values()) {
      for (Item item : items)
        item.refactoryItem();
    }

    for (Particle p : lsParticleIce) {
      p.remove();
    }
    for (Particle p : lsParticleBurnJam) {
      p.remove();
    }
    for (Image ray : lsRayJam) {
      ray.clearActions();
      ray.remove();
    }
  }

  private void unlockInput() {
    isWrap     = false;
    pieceStart = null;
    pieceEnd   = null;

    gamePlayUI.setTouchableItemSkill(Touchable.enabled);
  }

  //------------------------------------show/hide combo and popup-----------------------------------
  private void showWonder() {
    //todo: add sound effect
    if (countScoreToShowWonder >= WONDER_LOVELY &&
        countScoreToShowWonder < WONDER_FANTASTIC)
      gamePlayUI.showLovely();
    else if (countScoreToShowWonder >= WONDER_FANTASTIC &&
             countScoreToShowWonder < WONDER_AWESOME)
      gamePlayUI.showPWonder(0);
    else if (countScoreToShowWonder >= WONDER_AWESOME &&
             countScoreToShowWonder < WONDER_AMAZING)
      gamePlayUI.showPWonder(1);
    else if (countScoreToShowWonder >= WONDER_AMAZING)
      gamePlayUI.showPWonder(2);
  }

  public void showPauseUI() {
    isPause = true;
    gParent.addActor(blackScreen);
    gParent.addActor(pauseUI);
    pauseUI.showPause();
  }
  //------------------------------------show/hide combo and popup-----------------------------------

  private void addToGroup(Item item, Group group) {
    group.addActor(item);
  }

  public void setTouchableGamePlayUI(Touchable touchable) {
    gamePlayUI.setTouchable(touchable);
  }

}
