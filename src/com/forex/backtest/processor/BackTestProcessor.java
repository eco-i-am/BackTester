package com.forex.backtest.processor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import com.forex.backtest.common.BacktestData;
import com.forex.backtest.common.TradeResult;

public class BackTestProcessor {

  public List<TradeResult> process(List<BacktestData> data, int profit, int loss) {
    int stopLoss = loss;
    int takeProfit = profit;
    double currentSupport = 0;
    double currentResistance = 0;
    double nextSupport = 0;
    double nextResistance = 0;
    List<TradeResult> result = new ArrayList();


    boolean hasBuy = false;
    boolean hasSell = false;
    boolean firstHourOfDay = false;
    for (int i = 0; i < data.size(); i++) {
      BacktestData currentCandle = data.get(i);
      if (firstHourOfDay) {

        // second candle
        // System.out.println(currentCandle.getDatetime());
        firstHourOfDay = false;
        // get S&P, from previous next
        currentSupport = nextSupport;
        currentResistance = nextResistance;

        // reset to next S&P to first candle of the day (1:00/0:00)
        if (i != 0) {
          if (data.get(i - 1).getClose() > data.get(i - 1).getOpen()) {

            nextSupport = data.get(i - 1).getOpen();
            nextResistance = data.get(i - 1).getClose();
          } else {

            nextSupport = data.get(i - 1).getClose();
            nextResistance = data.get(i - 1).getOpen();
          }
        }
        nextResistance = updateNextResistance(nextResistance, currentCandle);
        nextSupport = updateNextSupport(nextSupport, currentCandle);
        hasBuy = false;
        hasSell = false;
      } else {

        // updating next resistance
        nextResistance = updateNextResistance(nextResistance, currentCandle);
        // updating next support
        nextSupport = updateNextSupport(nextSupport, currentCandle);

      }
      if (i != 0 && currentCandle.getDatetime().get(Calendar.DAY_OF_MONTH) != data.get(i - 1)
          .getDatetime().get(Calendar.DAY_OF_MONTH)) {
        // first candle of the day
        firstHourOfDay = true;

      } else {


        if (currentSupport != 0 && currentResistance != 0) {

          // BUY
          if (!hasBuy && (currentCandle.getClose() > currentResistance
              || currentCandle.getOpen() > currentResistance)) {
            // No buy trade for this duration yet
            if (currentCandle.getClose() > currentCandle.getOpen()) {
              // BULL CANDLE
              if (Math.abs(currentCandle.getClose() - currentResistance) > Math
                  .abs(currentCandle.getOpen() - currentResistance)) {
                // candle is at least 50% over resistance
                if (i + 1 != data.size()) {
                  // had next candle
                  // trigger buy

                  // BUY on next candle
                  // get datetime of next candle
                  int tradingCandle = i + 1;
                  Calendar tradetime = data.get(tradingCandle).getDatetime();
                  Double tradePrice = data.get(tradingCandle).getOpen();
                  Double maxPrice = data.get(tradingCandle).getHigh();
                  Double reversalPrice = data.get(tradingCandle).getLow();
                  // set action to buy
                  String action = "BUY";
                  String outcome;
                  String tradeResult;


                  // check outcome
                  for (int indexOutcome = 0; (tradingCandle + indexOutcome) <= data.size()
                      - 1; indexOutcome++) {
                    // get lowest price
                    if (data.get(tradingCandle + indexOutcome).getLow() < reversalPrice) {
                      reversalPrice = data.get(tradingCandle + indexOutcome).getLow();
                    }

                    if (data.get(tradingCandle + indexOutcome).getHigh() > maxPrice) {
                      maxPrice = data.get(tradingCandle + indexOutcome).getHigh();
                    }
                    // check if highest - trading price > take profit
                    if ((data.get(tradingCandle + indexOutcome).getHigh() - tradePrice)
                        * Math.pow(10, 2) > takeProfit) {
                      tradeResult = "PROFIT";
                      outcome = Integer.toString(takeProfit);
                      double reversal =
                          (tradePrice - reversalPrice) * Math.pow(10, BacktestData.decimal);
                      result.add(new TradeResult(currentSupport, currentResistance, tradetime,
                          tradePrice, action, Double.toString(reversal),
                          Integer.toString(indexOutcome), outcome, tradeResult));
                      //
                      // System.out.println("currentSupport:" + currentSupport + "
                      // currentResistance:"
                      // + currentResistance);
                      // System.out.println("TIME:" + tradetime + " PRICE:" + tradePrice + "
                      // ACTION:"
                      // + action + " RESULT:" + tradeResult + " DURATION:" + indexOutcome);
                      hasBuy = true;
                      break;
                    }

                    // profit or trading price - lowest > stop loss
                    if ((tradePrice - data.get(tradingCandle + indexOutcome).getLow())
                        * Math.pow(10, 2) > stopLoss) {

                      tradeResult = "LOSS";
                      outcome = "-" + Integer.toString(stopLoss);
                      double reversal = (maxPrice - tradePrice) * Math.pow(10, 2);
                      result.add(new TradeResult(currentSupport, currentResistance, tradetime,
                          tradePrice, action, Double.toString(reversal),
                          Integer.toString(indexOutcome), outcome, tradeResult));
                      //
                      // System.out.println("currentSupport:" + currentSupport + "
                      // currentResistance:"
                      // + currentResistance);
                      // System.out.println("TIME:" + tradetime + " PRICE:" + tradePrice + "
                      // ACTION:"
                      // + action + " RESULT:" + tradeResult + " DURATION:" + indexOutcome);
                      hasBuy = true;
                      break;
                    }
                    if (tradingCandle + indexOutcome == data.size() - 1) {
                      double reversal =
                          (tradePrice - reversalPrice) * Math.pow(10, BacktestData.decimal);

                      tradeResult = "OPEN";
                      outcome = "OPEN";
                      result.add(new TradeResult(currentSupport, currentResistance, tradetime,
                          tradePrice, action, Double.toString(reversal),
                          Integer.toString(indexOutcome), outcome, tradeResult));
                      // result.add(new TradeResult(tradetime, tradePrice, action,
                      // Double.toString(reversal), Integer.toString(indexResult),
                      // Integer.toString(0), OPEN_TRADE, OPEN_TRADE));
                      //
                      // System.out.println("currentSupport:" + currentSupport + "
                      // currentResistance:"
                      // + currentResistance);
                      // System.out.println("TIME:" + tradetime + " PRICE:" + tradePrice + "
                      // ACTION:"
                      // + action + " RESULT:" + tradeResult + " DURATION:" + indexOutcome);
                      hasBuy = true;
                      break;
                    }
                  }



                }
              }
            } else {
              // BEAR CANDLE
              if (Math.abs(currentCandle.getOpen() - currentResistance) > Math
                  .abs(currentCandle.getClose() - currentResistance)) {
                if (i + 1 != data.size()) {

                  // had next candle
                  // trigger buy

                  // BUY on next candle
                  // get datetime of next candle
                  int tradingCandle = i + 1;
                  Calendar tradetime = data.get(tradingCandle).getDatetime();
                  Double tradePrice = data.get(tradingCandle).getOpen();
                  Double maxPrice = data.get(tradingCandle).getHigh();
                  Double reversalPrice = data.get(tradingCandle).getLow();
                  // set action to buy
                  String action = "BUY";
                  String outcome;
                  String tradeResult;


                  // check outcome
                  for (int indexOutcome = 0; (tradingCandle + indexOutcome) <= data.size()
                      - 1; indexOutcome++) {
                    // get lowest price
                    if (data.get(tradingCandle + indexOutcome).getLow() < reversalPrice) {
                      reversalPrice = data.get(tradingCandle + indexOutcome).getLow();
                    }
                    if (data.get(tradingCandle + indexOutcome).getHigh() > maxPrice) {
                      maxPrice = data.get(tradingCandle + indexOutcome).getHigh();
                    }
                    // check if highest - trading price > take profit
                    if ((data.get(tradingCandle + indexOutcome).getHigh() - tradePrice)
                        * Math.pow(10, 2) > takeProfit) {
                      tradeResult = "PROFIT";
                      outcome = Integer.toString(takeProfit);
                      double reversal = (tradePrice - reversalPrice) * Math.pow(10, 2);
                      result.add(new TradeResult(currentSupport, currentResistance, tradetime,
                          tradePrice, action, Double.toString(reversal),
                          Integer.toString(indexOutcome), outcome, tradeResult));
                      //
                      // System.out.println("currentSupport:" + currentSupport + "
                      // currentResistance:"
                      // + currentResistance);
                      // System.out.println("TIME:" + tradetime + " PRICE:" + tradePrice + "
                      // ACTION:"
                      // + action + " RESULT:" + tradeResult + " DURATION:" + indexOutcome);
                      hasBuy = true;
                      break;
                    }

                    // profit or trading price - lowest > stop loss
                    if ((tradePrice - data.get(tradingCandle + indexOutcome).getLow())
                        * Math.pow(10, 2) > stopLoss) {

                      tradeResult = "LOSS";
                      outcome = "-" + Integer.toString(stopLoss);
                      double reversal =
                          (maxPrice - tradePrice) * Math.pow(10, BacktestData.decimal);
                      result.add(new TradeResult(currentSupport, currentResistance, tradetime,
                          tradePrice, action, Double.toString(reversal),
                          Integer.toString(indexOutcome), outcome, tradeResult));
                      //
                      // System.out.println("currentSupport:" + currentSupport + "
                      // currentResistance:"
                      // + currentResistance);
                      // System.out.println("TIME:" + tradetime + " PRICE:" + tradePrice + "
                      // ACTION:"
                      // + action + " RESULT:" + tradeResult + " DURATION:" + indexOutcome);
                      hasBuy = true;
                      break;
                    }
                    if (tradingCandle + indexOutcome == data.size() - 1) {

                      tradeResult = "OPEN";
                      outcome = "OPEN";
                      double reversal =
                          (tradePrice - reversalPrice) * Math.pow(10, BacktestData.decimal);

                      result.add(new TradeResult(currentSupport, currentResistance, tradetime,
                          tradePrice, action, Double.toString(reversal),
                          Integer.toString(indexOutcome), outcome, tradeResult));
                      // System.out.println("currentSupport:" + currentSupport + "
                      // currentResistance:"
                      // + currentResistance);
                      // System.out.println("TIME:" + tradetime + " PRICE:" + tradePrice + "
                      // ACTION:"
                      // + action + " RESULT:" + tradeResult + " DURATION:" + indexOutcome);
                      hasBuy = true;
                      break;
                    }
                  }



                }
              }
            }

          }



          // SELL
          if (!hasSell && (currentCandle.getClose() < currentSupport
              || currentCandle.getOpen() < currentSupport)) {
            // No sell trade for this duration yet
            // trigger SELL
            if (currentCandle.getClose() > currentCandle.getOpen()) {
              // BULL CANDLE

              if (Math.abs(currentCandle.getOpen() - currentSupport) > Math
                  .abs(currentCandle.getClose() - currentSupport)) {
                if (i + 1 != data.size()) {
                  // SELL on next candle

                  // get datetime of next candle
                  int tradingCandle = i + 1;
                  Calendar tradetime = data.get(tradingCandle).getDatetime();

                  // get trading price
                  Double tradePrice = data.get(tradingCandle).getOpen();
                  Double reversalPrice = data.get(tradingCandle).getHigh();
                  Double maxPrice = data.get(tradingCandle).getLow();
                  // set action to sell
                  String action = "SELL";

                  String outcome;
                  String tradeResult;
                  // iterate to the next candles

                  for (int indexOutcome = 0; (tradingCandle + indexOutcome) <= data.size()
                      - 1; indexOutcome++) {
                    if (data.get(tradingCandle + indexOutcome).getHigh() > reversalPrice)
                      reversalPrice = data.get(tradingCandle + indexOutcome).getHigh();

                    if (data.get(tradingCandle + indexOutcome).getLow() < maxPrice)
                      maxPrice = data.get(tradingCandle + indexOutcome).getLow();

                    // check if trading price - lowest > take profit
                    if ((tradePrice - data.get(tradingCandle + indexOutcome).getLow())
                        * Math.pow(10, BacktestData.decimal) > takeProfit) {
                      tradeResult = "PROFIT";
                      outcome = Integer.toString(takeProfit);
                      double reversal =
                          (reversalPrice - tradePrice) * Math.pow(10, BacktestData.decimal);

                      result.add(new TradeResult(currentSupport, currentResistance, tradetime,
                          tradePrice, action, Double.toString(reversal),
                          Integer.toString(indexOutcome), outcome, tradeResult));
                      // System.out.println("currentSupport:" + currentSupport + "
                      // currentResistance:"
                      // + currentResistance);
                      // System.out.println("TIME:" + tradetime + " PRICE:" + tradePrice + "
                      // ACTION:"
                      // + action + " RESULT:" + tradeResult + " DURATION:" + indexOutcome);
                      hasSell = true;
                      break;
                    }

                    // profit or highest - trading price > stop loss
                    if ((data.get(tradingCandle + indexOutcome).getHigh() - tradePrice)
                        * Math.pow(10, BacktestData.decimal) > stopLoss) {

                      tradeResult = "LOSS";
                      outcome = "-" + Integer.toString(stopLoss);
                      double reversal =
                          (tradePrice - maxPrice) * Math.pow(10, BacktestData.decimal);

                      result.add(new TradeResult(currentSupport, currentResistance, tradetime,
                          tradePrice, action, Double.toString(reversal),
                          Integer.toString(indexOutcome), outcome, tradeResult));
                      hasSell = true;
                      break;
                    }
                    if (tradingCandle + indexOutcome == data.size() - 1) {
                      double reversal =
                          (reversalPrice - tradePrice) * Math.pow(10, BacktestData.decimal);

                      tradeResult = "OPEN";
                      outcome = "OPEN";
                      result.add(new TradeResult(currentSupport, currentResistance, tradetime,
                          tradePrice, action, Double.toString(reversal),
                          Integer.toString(indexOutcome), outcome, tradeResult));
                      hasSell = true;
                      break;
                    }
                  }
                }
              }
            } else {
              // BEAR CANDLE
              if (Math.abs(currentCandle.getClose() - currentSupport) > Math
                  .abs(currentCandle.getOpen() - currentSupport)) {
                if (i + 1 != data.size()) {
                  // SELL on next candle

                  // get datetime of next candle
                  int tradingCandle = i + 1;
                  Calendar tradetime = data.get(tradingCandle).getDatetime();

                  // get trading price
                  Double tradePrice = data.get(tradingCandle).getOpen();
                  Double reversalPrice = data.get(tradingCandle).getHigh();
                  Double maxPrice = data.get(tradingCandle).getLow();
                  // set action to sell
                  String action = "SELL";

                  String outcome;
                  String tradeResult;
                  // iterate to the next candles

                  for (int indexOutcome = 0; (tradingCandle + indexOutcome) <= data.size()
                      - 1; indexOutcome++) {
                    if (data.get(tradingCandle + indexOutcome).getHigh() > reversalPrice)
                      reversalPrice = data.get(tradingCandle + indexOutcome).getHigh();

                    if (data.get(tradingCandle + indexOutcome).getLow() < maxPrice)
                      maxPrice = data.get(tradingCandle + indexOutcome).getLow();

                    // check if trading price - lowest > take profit
                    if ((tradePrice - data.get(tradingCandle + indexOutcome).getLow())
                        * Math.pow(10, BacktestData.decimal) > takeProfit) {
                      tradeResult = "PROFIT";
                      outcome = Integer.toString(takeProfit);
                      double reversal =
                          (reversalPrice - tradePrice) * Math.pow(10, BacktestData.decimal);

                      result.add(new TradeResult(currentSupport, currentResistance, tradetime,
                          tradePrice, action, Double.toString(reversal),
                          Integer.toString(indexOutcome), outcome, tradeResult));
                      hasSell = true;
                      break;
                    }

                    // profit or highest - trading price > stop loss
                    if ((data.get(tradingCandle + indexOutcome).getHigh() - tradePrice)
                        * Math.pow(10, BacktestData.decimal) > stopLoss) {

                      tradeResult = "LOSS";
                      outcome = "-" + Integer.toString(stopLoss);
                      double reversal =
                          (tradePrice - maxPrice) * Math.pow(10, BacktestData.decimal);

                      result.add(new TradeResult(currentSupport, currentResistance, tradetime,
                          tradePrice, action, Double.toString(reversal),
                          Integer.toString(indexOutcome), outcome, tradeResult));
                      hasSell = true;
                      break;
                    }
                    if (tradingCandle + indexOutcome == data.size() - 1) {
                      double reversal =
                          (reversalPrice - tradePrice) * Math.pow(10, BacktestData.decimal);

                      tradeResult = "OPEN";
                      outcome = "OPEN";
                      result.add(new TradeResult(currentSupport, currentResistance, tradetime,
                          tradePrice, action, Double.toString(reversal),
                          Integer.toString(indexOutcome), outcome, tradeResult));
                      hasSell = true;
                      break;
                    }
                  }
                }
              }
            }
          }

        }
      }

      // System.out.println(currentCandle.getDatetime() + " SUPPORT: " + currentSupport
      // + " RESISTANCE: " + currentResistance);
    }
    return result;
  }



  protected double updateNextSupport(double nextSupport, BacktestData currentCandle) {

    if (nextSupport == 0 || currentCandle.getOpen() < nextSupport
        || currentCandle.getClose() < nextSupport) {
      if (currentCandle.getOpen() < currentCandle.getClose()) {
        nextSupport = currentCandle.getOpen();
      } else {
        nextSupport = currentCandle.getClose();
      }
    }
    return nextSupport;
  }

  protected double updateNextResistance(double nextResistance, BacktestData currentCandle) {

    if (nextResistance == 0 || currentCandle.getOpen() > nextResistance
        || currentCandle.getClose() > nextResistance) {
      if (currentCandle.getOpen() > currentCandle.getClose()) {
        nextResistance = currentCandle.getOpen();
      } else {
        nextResistance = currentCandle.getClose();
      }
    }
    return nextResistance;
  }
}
