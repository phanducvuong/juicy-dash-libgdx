package com.ss.utils;

import com.badlogic.gdx.math.Vector2;
import com.ss.config.Type;
import com.ss.objects.Item;
import com.ss.objects.Piece;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

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
      for (int j=0; j<COLUMN; j++) {
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

  public Item getItem(List<Item> items) {
    for (Item item : items) {
      if (!item.isAlive)
        return item;
    }
    return null;
  }

  public List<Item> getLsItem(HashMap<String, List<Item>> hmItem, Type ...types) {
    int amount = (ROW*COLUMN) / types.length;
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

    int remain = ROW * COLUMN - amount * types.length;
    if (remain >= 1)
      for (int i=0; i<remain; i++) {
        String key = getRegion((int) Math.floor(Math.random() * 5));
        Item item = getItem(hmItem.get(key));
        item.isAlive = true;
        lsTmp.add(item);
      }

    return lsTmp;
  }

  public void log(Piece piece) {
    System.out.println("piece start: " + piece.pos + "ROW, COL: " + piece.row + "  " + piece.col);
    System.out.println(piece.item.name);
    System.out.println(piece.item.getPos());
  }

}
