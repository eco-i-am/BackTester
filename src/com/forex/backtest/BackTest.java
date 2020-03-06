package com.forex.backtest;

import java.awt.Desktop;
import java.io.File;
import java.util.List;
import java.util.Scanner;
import com.forex.backtest.common.BacktestData;
import com.forex.backtest.common.TradeResult;
import com.forex.backtest.processor.BackTestProcessor;
import com.forex.backtest.reader.CSVReader;
import com.forex.backtest.writer.CSVWriter;

public class BackTest {

  public static void main(String[] args) {

    Scanner scanner = new Scanner(System.in);

    try {
      File file = getSourceFile(scanner);
      System.out.print("TP?");
      String profit = scanner.nextLine();
      if (profit.isBlank()) {
        profit = "400";
      }
      System.out.print("SL? ");
      String loss = scanner.nextLine();

      if (loss.isBlank()) {
        loss = "2000";
      }

      System.out.print("Save in?");

      String targetFile = scanner.nextLine();
      if (targetFile.isBlank()) {
        targetFile = "result";
      }


      List<BacktestData> data = new CSVReader().getForexData(file);
      int takeProfit = Integer.parseInt(profit);
      int stopLoss = Integer.parseInt(loss);
      List<TradeResult> result = new BackTestProcessor().process(data, takeProfit, stopLoss);

      new CSVWriter().writeCSV(targetFile + ".csv", result);
      System.out.print("TADA!!!");

      Desktop desktop = Desktop.getDesktop();
      desktop.open(new File(targetFile + ".csv"));
    } catch (Exception e) {

      e.printStackTrace();

    }
  }

  private static String getTimeFrame(Scanner scanner) {

    System.out.print("TimeFrame? ");
    String timeframe = scanner.nextLine();

    return timeframe;
  }

  private static File getSourceFile(Scanner scanner) {

    System.out.print("Source file?");
    String filename = scanner.nextLine();
    if (filename.isBlank()) {
      filename = "XAUUSD60.csv";
    }
    File file = new File(filename);

    while (!file.exists() || !filename.contains("csv")) {
      System.out.println("Missing source file.");
      filename = scanner.nextLine();
      file = new File(filename);
    }
    return file;
  }
}
