package com.forex.backtest.writer;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import com.forex.backtest.common.CSVUtils;
import com.forex.backtest.common.TradeResult;

public class CSVWriter {
  public void writeCSV(String file, List<TradeResult> data) throws IOException {

    DateFormatSymbols dfs = new DateFormatSymbols();
    FileWriter writer = new FileWriter(file);
    List<String> header = createHeader();
    CSVUtils.writeLine(writer, header);

    for (TradeResult item : data) {
      List<String> list = createDataList(dfs, item);
      CSVUtils.writeLine(writer, list);
    }

    writer.flush();
    writer.close();
  }


  private List<String> createDataList(DateFormatSymbols dfs, TradeResult item) {

    List<String> list = new ArrayList<String>();
    Calendar dateTime = item.getDatetime();
    int year = dateTime.get(Calendar.YEAR);
    int month = dateTime.get(Calendar.MONTH) + 1;
    int day = dateTime.get(Calendar.DAY_OF_MONTH);
    list.add(year + "-" + String.format("%02d", month) + "-" + String.format("%02d", day));
    list.add(Integer.toString(year));
    list.add(dfs.getShortMonths()[dateTime.get(Calendar.MONTH)]);
    list.add(dfs.getShortWeekdays()[dateTime.get(Calendar.DAY_OF_WEEK)]);
    list.add(Integer.toString(dateTime.get(Calendar.WEEK_OF_MONTH)));
    int hour = dateTime.get(Calendar.HOUR_OF_DAY);
    int min = dateTime.get(Calendar.MINUTE);
    list.add(String.format("%02d", hour) + ":" + String.format("%02d", min));
    list.add(Double.toString(item.getPrice()));
    list.add(item.getAction());
    list.add(item.getReversal());
    list.add(item.getDuration());
    list.add(item.getResult());
    list.add(item.getOutcome());
    return list;
  }


  private List<String> createHeader() {
    List<String> header = new ArrayList<String>();
    header.add("DATE");
    header.add("YEAR");
    header.add("MONTH");
    header.add("DAY");
    header.add("WEEK");
    header.add("TIME");
    header.add("PRICE");
    header.add("ACTION");
    header.add("REVERSAL");
    header.add("DURATION");
    header.add("RESULT");
    header.add("OUTCOME");
    return header;
  }
}
