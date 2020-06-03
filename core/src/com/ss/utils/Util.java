package com.ss.utils;

import com.badlogic.gdx.math.Vector2;
import com.ss.config.Type;
import com.ss.gameLogic.effects.Particle;
import com.ss.objects.Item;
import com.ss.objects.Piece;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.ss.config.Config.*;

public class Util {

  private static Util instance;
  public static Util inst() {
    return instance == null ? instance = new Util() : instance;
  }

  private Util() {}

  public String getRegion(int id) {

    String r = "";
    switch (id) {
      case 0: r = "item_grape"; break;
      case 1: r = "item_banana"; break;
      case 2: r = "item_kiwi"; break;
      case 3: r = "item_orange"; break;
      case 4: r = "item_apple"; break;
      case 5: r = "item_strawberry"; break;
      case 6: r = "item_glass_juice"; break;
      case 7: r = "item_jam"; break;
      case 8: r = "item_walnut"; break;
      case 9: r = "item_clock"; break;
    }
    return r;

  }

  public boolean chkTypeFruit(Piece piece) {
    return piece.item       != null       &&
           piece.item.type  != Type.clock &&
           piece.item.type  != Type.jam   &&
           piece.item.type  != Type.glass_fruit;
  }

  public boolean chkSpecialItem(Type type) {
    return type == Type.clock ||
           type == Type.jam   ||
           type == Type.glass_fruit;
  }

  public Piece getPieceByCoordinate(float x, float y, Piece[][] pieces) {
    for (Piece[] ps : pieces) {
      for (Piece piece : ps)
        if (x >= piece.pos.x && x <= piece.pos.x + WIDTH_PIECE &&
            y >= piece.pos.y && y <= piece.pos.y + HEIGHT_PIECE)
          return piece;
    }
    return null;
  }

  public List<Piece> getRallyPieceBy(int row, int col, Piece[][] pieces) {
    List<Piece> tmp = new ArrayList<>();
    for (int i=row; i<row+3; i++) {
      for (int j=col; j<col+3; j++)
        tmp.add(pieces[i][j]);
    }
    return tmp;
  }

  public Type getType(int id) {

    Type t = Type.blank;
    switch (id) {
      case 0: t = Type.grape; break;
      case 1: t = Type.banana; break;
      case 2: t = Type.kiwi; break;
      case 3: t = Type.orange; break;
      case 4: t = Type.apple; break;
      case 5: t = Type.strawberry; break;
      case 6: t = Type.glass_fruit; break;
      case 7: t = Type.jam; break;
      case 8: t = Type.walnut; break;
      case 9: t = Type.clock; break;
    }
    return t;

  }

  public Piece inRange(Piece[][] arr, Vector2 pos) {

    for (int i=0; i<ROW; i++) {
      for (int j = 0; j< COL; j++) {
        Vector2 p = arr[i][j].pos;
        if (pos.x >= p.x && pos.x <= p.x + WIDTH_PIECE
          && pos.y >= p.y && pos.y <= p.y + HEIGHT_PIECE)
          return arr[i][j];
      }
    }
    return null;

  }

  public Piece getPieceEmpty(Piece[][] piecess, int row, int col) {
    return piecess[row][col].isEmpty ? piecess[row][col] : null;
  }

  public Piece getPieceTypeDifferenceWith(Piece[][] pieces, Piece pStart, Piece pEnd) {
    for (Piece[] ps : pieces) {
      for (Piece p : ps)
        if (p != pStart && p != pEnd)
          return p;
    }
    return null;
  }

  public Item getItem(List<Item> items) {
    for (Item item : items) {
      if (!item.isAlive) {
        item.isAlive = true;
        return item;
      }
    }
    return null;
  }

  public boolean chkTypeIn(List<Type> types, Type typeChk) {
    for (Type type : types) {
      if (typeChk == type)
        return true;
    }
    return false;
  }

  public String rndSoundDrop() {
    int rnd = (int) (Math.round(Math.random()*2) + 1);
    return "drop_"+rnd;
  }

  public String rndSoundChew() {
    int rnd = (int) (Math.round(Math.random()*2) + 1);
    return "chew_"+rnd;
  }

  public Item getRndItem(HashMap<String, List<Item>> hm, List<Type> types) {
    int d = types.size();
    int rnd = (int) Math.floor(Math.random() * d);
    String key = "item_" + types.get(rnd).name();

    List<Item> ls = hm.get(key);
    for (Item item : ls) {
      if (!item.isAlive) {
        item.isAlive = true;
        return item;
      }
    }
    return null;
  }

  public List<Item> getLsItem(HashMap<String, List<Item>> hmItem, List<Type> types) {
    int amount = (ROW * COL) / types.size();
    List<Item> lsTmp = new ArrayList<>();

    for (Type type : types) {
      String key = "item_" + type.name();
      List<Item> ls = hmItem.get(key);

      for (int i=0; i<amount; i++)
        if (!ls.get(i).isAlive) {
          ls.get(i).isAlive = true;
          lsTmp.add(ls.get(i));
        }
    }

    int remain = ROW * COL - amount * types.size();
    if (remain >= 1)
      for (int i=0; i<remain; i++) {
        String key = getRegion((int) Math.floor(Math.random() * 5));
        Item item = getItem(hmItem.get(key));
        item.isAlive = true;
        lsTmp.add(item);
      }

    return lsTmp;
  }

  public List<Piece> filterVertically(Piece[][] arrPiece, Piece pChk) {

    List<Piece> tmpPieces = new ArrayList<>();
    int row = pChk.row,
        col = pChk.col;

    row -= 1;
    while (row >= 0) {
      Piece tmp = arrPiece[row][col];
      if (!tmp.isEmpty && !pChk.isEmpty && tmp.item.type == pChk.item.type) {
        tmpPieces.add(tmp);
        row -= 1;
      }
      else
        break;
    }

    row = pChk.row + 1;
    while (row < ROW) {
      Piece tmp = arrPiece[row][col];
      if (!tmp.isEmpty && !pChk.isEmpty && tmp.item.type == pChk.item.type) {
        tmpPieces.add(tmp);
        row += 1;
      }
      else
        break;
    }

    tmpPieces.add(pChk);
    return tmpPieces;
  }

  public List<Piece> filterHorizontally(Piece[][] arrPiece, Piece pChk) {

    List<Piece> tmpPieces = new ArrayList<>();
    int row = pChk.row,
        col = pChk.col;

    col -= 1;
    while (col >= 0) {
      Piece tmp = arrPiece[row][col];
      if (!tmp.isEmpty && !pChk.isEmpty && tmp.item.type == pChk.item.type) {
        tmpPieces.add(tmp);
        col -= 1;
      }
      else
        break;
    }

    col = pChk.col + 1;
    while (col < COL) {
      Piece tmp = arrPiece[row][col];
      if (!tmp.isEmpty && !pChk.isEmpty && tmp.item.type == pChk.item.type) {
        tmpPieces.add(tmp);
        col += 1;
      }
      else
        break;
    }

    tmpPieces.add(pChk);
    return tmpPieces;
  }

  public boolean chkContain(List<Piece> parent, List<Piece> child) {
    for (Piece piece : child) {
      if (parent.contains(piece))
        return true;
    }
    return false;
  }

  public Piece getPieceMinRow(List<Piece> pieces) {
    Piece pMinRow = pieces.get(0);
    for (Piece p : pieces) {
      if (pMinRow.row > p.row)
        pMinRow = p;
    }
    return pMinRow;
  }

  public Piece getPieceIntersectByVerAndHor(List<Piece> vers, List<Piece> hors) {
    for (Piece pV : vers) {
      for (Piece pH: hors) {
        if (pV == pH)
          return pV;
      }
    }
    return null;
  }

  public List<Piece> getLsPieceIsMatch(Piece[][] arrs, Piece target) {

    List<Piece> tmps = new ArrayList<>();
    for (int i=0; i<ROW; i++) {
      for (int j=0; j<COL; j++) {
        if (arrs[i][j].item.type == target.item.type)
          tmps.add(arrs[i][j]);
      }
    }
    return tmps;
  }

  public void removeItemAt(Piece piece, List<Piece> pieces) {
    List<Piece> tmp = new ArrayList<>();
    for (Piece p : pieces) {
      if (p == piece)
        tmp.add(p);
    }
    pieces.removeAll(tmp);
  }

  public float calDegreeBy(Piece point, Piece target) {
    float x = (target.pos.x + WIDTH_PIECE/2) - (point.pos.x + WIDTH_PIECE/2);
    float y = (target.pos.y + HEIGHT_PIECE/2) - (point.pos.y + HEIGHT_PIECE/2);
    float degree = (float) Math.toDegrees(Math.atan(y/x));

    if (x < 0)
      degree -= 180;

    return degree;
  }

  public void log(String label, Piece piece) {
    if (piece.item != null) {
      System.out.println(label + piece.pos + "ROW, COL: " + piece.row + "  " + piece.col);
      System.out.println(piece.item.name);
    }
    else
      System.out.println("EMPTY");
//    System.out.println(piece.item.getPos());
  }

  public void log(Piece piece) {
    System.out.println("ROW: " + piece.row + " COL: " + piece.col);
  }

//  public void log(List<HashMap<String, List<Piece>>> ls) {
//
//    for (HashMap<String, List<Piece>> hh : ls) {
//      if (hh.get("ver") != null) {
//        System.out.print("VER: ");
//        log(hh.get("ver").get(0));
//      }
//      if (hh.get("hor") != null) {
//        System.out.print("HOR: ");
//        log(hh.get("hor").get(0));
//      }
//    }
//
//  }

  public void log(List<Piece> l) {
    for (Piece piece : l)
      log(piece);
  }

}
