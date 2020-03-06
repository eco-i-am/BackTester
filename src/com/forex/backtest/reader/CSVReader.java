package com.forex.backtest.reader;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Scanner;
import com.forex.backtest.common.BacktestData;

public class CSVReader {

  public List<BacktestData> getForexData(File file) {

    List<BacktestData> result = new ArrayList<BacktestData>();
    try {
      Scanner sc = new Scanner(file);
      while (sc.hasNextLine()) {
        String[] line = sc.nextLine().split(",");
        String date = line[0];
        String time = line[1];
        if (BacktestData.decimal == null) {
          BacktestData.decimal = line[2].split("\\.")[1].length();
        }
        Calendar datetime = null;
        if (time.length() == 4) {
          datetime = new GregorianCalendar(Integer.parseInt(date.substring(0, 4)),
              Integer.parseInt(date.substring(5, 7)) - 1, Integer.parseInt(date.substring(8, 10)),
              Integer.parseInt("0" + time.substring(0, 1)), Integer.parseInt(time.substring(2, 4)));
        } else {
          datetime = new GregorianCalendar(Integer.parseInt(date.substring(0, 4)),
              Integer.parseInt(date.substring(5, 7)) - 1, Integer.parseInt(date.substring(8, 10)),
              Integer.parseInt(time.substring(0, 2)), Integer.parseInt(time.substring(3, 5)));
        }
        result.add(new BacktestData(datetime, Double.parseDouble(line[2]),
            Double.parseDouble(line[3]), Double.parseDouble(line[4]), Double.parseDouble(line[5]),
            Integer.parseInt(line[6])));

      }
      sc.close();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }

    return result;
  }
}
