package com.forex.backtest.common;

import java.util.Calendar;

public class TradeResult {

  final Double support;

  final Double resistance;


  final Calendar datetime;

  final Double price;

  final String action;

  final String reversal;

  final String duration;

  final String result;

  final String outcome;

  public TradeResult(Double support, Double resistance, Calendar datetime, Double price,
      String action, String reversal, String duration, String result, String outcome) {
    super();
    this.support = support;
    this.resistance = resistance;
    this.datetime = datetime;
    this.price = price;
    this.action = action;
    this.reversal = reversal;
    this.duration = duration;
    this.result = result;
    this.outcome = outcome;
  }

  public Double getSupport() {
    return support;
  }

  public Double getResistance() {
    return resistance;
  }

  public Calendar getDatetime() {
    return datetime;
  }

  public Double getPrice() {
    return price;
  }

  public String getAction() {
    return action;
  }

  public String getReversal() {
    return reversal;
  }

  public String getDuration() {
    return duration;
  }

  public String getResult() {
    return result;
  }

  public String getOutcome() {
    return outcome;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((action == null) ? 0 : action.hashCode());
    result = prime * result + ((datetime == null) ? 0 : datetime.hashCode());
    result = prime * result + ((duration == null) ? 0 : duration.hashCode());
    result = prime * result + ((outcome == null) ? 0 : outcome.hashCode());
    result = prime * result + ((price == null) ? 0 : price.hashCode());
    result = prime * result + ((resistance == null) ? 0 : resistance.hashCode());
    result = prime * result + ((this.result == null) ? 0 : this.result.hashCode());
    result = prime * result + ((reversal == null) ? 0 : reversal.hashCode());
    result = prime * result + ((support == null) ? 0 : support.hashCode());
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
    TradeResult other = (TradeResult) obj;
    if (action == null) {
      if (other.action != null)
        return false;
    } else if (!action.equals(other.action))
      return false;
    if (datetime == null) {
      if (other.datetime != null)
        return false;
    } else if (!datetime.equals(other.datetime))
      return false;
    if (duration == null) {
      if (other.duration != null)
        return false;
    } else if (!duration.equals(other.duration))
      return false;
    if (outcome == null) {
      if (other.outcome != null)
        return false;
    } else if (!outcome.equals(other.outcome))
      return false;
    if (price == null) {
      if (other.price != null)
        return false;
    } else if (!price.equals(other.price))
      return false;
    if (resistance == null) {
      if (other.resistance != null)
        return false;
    } else if (!resistance.equals(other.resistance))
      return false;
    if (result == null) {
      if (other.result != null)
        return false;
    } else if (!result.equals(other.result))
      return false;
    if (reversal == null) {
      if (other.reversal != null)
        return false;
    } else if (!reversal.equals(other.reversal))
      return false;
    if (support == null) {
      if (other.support != null)
        return false;
    } else if (!support.equals(other.support))
      return false;
    return true;
  }

  @Override
  public String toString() {
    return "TradeResult [support=" + support + ", resistance=" + resistance + ", datetime="
        + datetime + ", price=" + price + ", action=" + action + ", reversal=" + reversal
        + ", duration=" + duration + ", result=" + result + ", outcome=" + outcome + "]";
  }

}
