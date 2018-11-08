package com.syswin.temail.media.bank.bean;

public class Range {

  private int pos = 0;
  private int end = 0;
  private boolean isRange = true;

  public Range(String Range) {
    if (!Range.matches("^bytes=[0-9]{1,}-([0-9]+)?")) {
      isRange = false;
    }
    try {
      String[] tmpStr = Range.replaceAll("bytes=", "").split("-");
      pos = Integer.parseInt(tmpStr[0]);
      if (tmpStr.length > 1) {
        end = Integer.parseInt(tmpStr[1]);
      }
    } catch (NumberFormatException e) {
      isRange = false;
    }
  }

  public int getPos() {
    return pos;
  }

  public void setPos(int pos) {
    this.pos = pos;
  }

  public int getEnd() {
    return end;
  }

  public void setEnd(int end) {
    this.end = end;
  }

  public boolean isRange() {
    return isRange;
  }

  public void setRange(boolean range) {
    isRange = range;
  }

  @Override
  public String toString() {
    return "Range{" +
        "pos=" + pos +
        ", end=" + end +
        ", isRange=" + isRange +
        '}';
  }
}
