package com.ss.data;

public class WheelData {

  private String region;
  private int id;
  private int qty;
  private String qtyText;
  private int percent;

  public WheelData(String region, int id, int qty, String qtyText, int percent) {
    this.region = region;
    this.id = id;
    this.qty = qty;
    this.qtyText = qtyText;
    this.percent = percent;
  }

  public String getRegion() {
    return region;
  }

  public int getId() {
    return id;
  }

  public int getQty() {
    return qty;
  }

  public String getQtyText() {
    return qtyText;
  }

  public int getPercent() {
    return percent;
  }
}
