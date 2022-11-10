package ru.netology;

import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class ClientLog {
    private int productNum;
    private int amount;
    private String[] products;
    private int[] prices;
    private int[] counts;
    List<ClientLog> list = new ArrayList<>();

    public ClientLog() {
    }

    public ClientLog(int productNum, int amount) {
        this.productNum = productNum;
        this.amount = amount;
    }

    public ClientLog(String[] products, int[] prices) {
        this.products = products;
        this.prices = prices;
        counts = new int[products.length];
    }

    public ClientLog(String[] products, int[] prices, int[] counts) {
        this.products = products;
        this.prices = prices;
        this.counts = counts;
    }

    public void log(int productNum, int amount) {
        list.add(new ClientLog(productNum, amount));
    }

    public void exportAsCSV() {
        try (Writer writer = new FileWriter("client.csv", true);) {
            ColumnPositionMappingStrategy<ClientLog> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(ClientLog.class);
            strategy.setColumnMapping("productNum", "amount");

            StatefulBeanToCsv<ClientLog> sbc = new StatefulBeanToCsvBuilder<ClientLog>(writer)
                    .withMappingStrategy(strategy)
                    .build();
            sbc.write(list);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (CsvRequiredFieldEmptyException | CsvDataTypeMismatchException e) {
            System.out.println(e.getMessage());
        }
    }


    public int getProductNum() {
        return productNum;
    }

    public int getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return productNum + " " + amount;
    }
}
