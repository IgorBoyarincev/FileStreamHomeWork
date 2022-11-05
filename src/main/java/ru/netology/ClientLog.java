package ru.netology;

import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ClientLog {
    private int productNum;
    private int amount;
    private String[] products;
    private int[] prices;
    private int[] counts;
    List<ClientLog> list = new ArrayList<>();
    JSONObject obj = new JSONObject();

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

    public void addToCart(int productNum, int amount) {
        counts[productNum] += amount;
        System.out.println(products[productNum] + " " + counts[productNum] + " шт по " + prices[productNum] + " р/шт");
    }

    public void printCart() {
        int productPrice;
        int sum = 0;
        System.out.println("ваша корзина: ");
        for (int i = 0; i < products.length; i++) {
            if (counts[i] == 0) {
                continue;
            }
            productPrice = prices[i] * counts[i];
            System.out.println("\t" + products[i] + " " + counts[i] + " шт по " + prices[i] + " р/шт " + productPrice + " руб в сум");
            sum += productPrice;
        }
        System.out.println("итого: " + sum + " руб");
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

    public void saveJsonFile(File file) {
        JSONArray listCount = new JSONArray();
        for (int count : counts) {
            listCount.add(count);
        }
        obj.put("counts", listCount);
        JSONArray listPrice = new JSONArray();
        for (int price : prices) {
            listPrice.add(price);
        }
        obj.put("prices", listPrice);
        JSONArray listProduct = new JSONArray();
        for (String product : products) {
            listProduct.add(product);
        }
        obj.put("products", listProduct);
        try (FileWriter writer = new FileWriter(file);) {
            writer.write(obj.toJSONString());
            writer.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void loadFromJSONFile(File file) {
        String[] products;
        int[] prices;
        int[] counts;
        ClientLog cl = new ClientLog();
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader(file));
            JSONObject jsonObject = (JSONObject) obj;
            //System.out.println(jsonObject);
            JSONArray jsonListProducts = (JSONArray) jsonObject.get("products");
            products = new String[jsonListProducts.size()];
            for (int i = 0; i < products.length; i++) {
                products[i] = String.valueOf(jsonListProducts.get(i));
            }
            JSONArray jsonListPrices = (JSONArray) jsonObject.get("prices");
            prices = new int[jsonListPrices.size()];
            for (int i = 0; i < prices.length; i++) {
                prices[i] = Integer.valueOf(jsonListPrices.get(i).toString());
            }
            JSONArray jsonListCounts = (JSONArray) jsonObject.get("counts");
            counts = new int[jsonListCounts.size()];
            for (int i = 0; i < counts.length; i++) {
                counts[i] = Integer.valueOf(jsonListCounts.get(i).toString());
            }
            cl = new ClientLog(products, prices, counts);
            cl.printCart();
        } catch (IOException | ParseException e) {
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
