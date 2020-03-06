package com.forex.backtest.common;

import java.util.Calendar;

public class BacktestData {

  public static Integer decimal;

  final Calendar datetime;

  final Double open;

  final Double high;

  final Double low;

  final Double close;

  final int volume;

  public BacktestData(Calendar datetime, Double open, Double high, Double low, Double close,
      int volume) {

    super();
    this.datetime = datetime;
    this.open = open;
    this.high = high;
    this.low = low;
    this.close = close;
    this.volume = volume;
  }

  public Calendar getDatetime() {

    return datetime;
  }

  public Double getOpen() {

    return open;
  }

  public Double getHigh() {

    return high;
  }

  public Double getLow() {

    return low;
  }

  public Double getClose() {

    return close;
  }

  public int getVolume() {

    return volume;
  }

  @Override
  public int hashCode() {

    final int prime = 31;
    int result = 1;
    result = prime * result + ((close == null) ? 0 : close.hashCode());
    result = prime * result + ((datetime == null) ? 0 : datetime.hashCode());
    result = prime * result + ((high == null) ? 0 : high.hashCode());
    result = prime * result + ((low == null) ? 0 : low.hashCode());
    result = prime * result + ((open == null) ? 0 : open.hashCode());
    result = prime * result + volume;
    return result;
  }

  @Override
  public boolean equals(Object obj) {

    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    BacktestData other = (BacktestData) obj;
    if (close == null) {
      if (other.close != null)
        return false;
    } else if (!close.equals(other.close))
      return false;
    if (datetime == null) {
      if (other.datetime != null)
        return false;
    } else if (!datetime.equals(other.datetime))
      return false;
    if (high == null) {
      if (other.high != null)
        return false;
    } else if (!high.equals(other.high))
      return false;
    if (low == null) {
      if (other.low != null)
        return false;
    } else if (!low.equals(other.low))
      return false;
    if (open == null) {
      if (other.open != null)
        return false;
    } else if (!open.equals(other.open))
      return false;
    if (volume != other.volume)
      return false;
    return true;
  }

  @Override
  public String toString() {

    return "BacktestData [datetime=" + datetime + ", open=" + open + ", high=" + high + ", low="
        + low + ", close=" + close + ", volume=" + volume + "]";
  }

}
