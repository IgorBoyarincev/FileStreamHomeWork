package ru.netology;

import com.google.gson.Gson;
import org.json.simple.JSONObject;

import java.io.*;

public class Basket implements Serializable {
    private String[] products;
    private int[] prices;
    private int[] counts;
    JSONObject obj = new JSONObject();

    public Basket() {
    }

    public Basket(String[] products, int[] prices) {
        this.products = products;
        this.prices = prices;
        counts = new int[products.length];
    }

    public Basket(String[] products, int[] prices, int[] counts) {
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
        for (int i = 0; i < counts.length; i++) {
            if (counts[i] == 0) {
                continue;
            }
            productPrice = prices[i] * counts[i];
            System.out.println("\t" + products[i] + " " + counts[i] + " шт по " + prices[i] + " р/шт" + productPrice + " руб в сум");
            sum += productPrice;
        }
        System.out.println("итого: " + sum + " руб");
    }

    public void saveText(File file) throws FileNotFoundException {
        try (PrintWriter writer = new PrintWriter(file);) {
            for (String product : products) {
                writer.print(product + " ");
            }
            writer.println();
            for (int price : prices) {
                writer.print(price + " ");
            }
            writer.println();
            for (int count : counts) {
                writer.print(count + " ");
            }
        }
    }

    public void saveBin(File file) throws IOException {
        try (
                ObjectOutputStream writer = new ObjectOutputStream(new FileOutputStream(file));
        ) {
            writer.writeObject(new Basket(products, prices, counts));
        }
    }

    public void saveJsonFile(File file) throws IOException {
        Basket basket = new Basket(products, prices, counts);
        try (PrintWriter writer = new PrintWriter(file)) {
            Gson gson = new Gson();
            String json = gson.toJson(basket);
            writer.println(json);
        }
    }

    public static Basket loadFromTxtFile(File file) throws IOException {
        String[] products = null;
        int[] prices;
        int[] counts;
        Basket basket = null;
        try (
                BufferedReader reader = new BufferedReader(new FileReader(file));
        ) {
            products = reader.readLine().split(" ");
            String[] priceStr = reader.readLine().trim().split(" ");
            prices = new int[priceStr.length];
            for (int i = 0; i < prices.length; i++) {
                prices[i] = Integer.parseInt(priceStr[i]);
            }
            String[] countStr = reader.readLine().trim().split(" ");
            counts = new int[countStr.length];
            for (int i = 0; i < counts.length; i++) {
                counts[i] = Integer.parseInt(countStr[i]);
            }
            basket = new Basket(products, prices, counts);
        }
        return basket;
    }

    public static Basket loadFromBinFile(File file) throws IOException, ClassNotFoundException {
        try (
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
        ) {
            return (Basket) in.readObject();
        }
    }

    public static Basket loadFromJSONFile(File file) throws IOException {
        Basket basket = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            Gson gson = new Gson();
            String line = reader.readLine();
            basket = gson.fromJson(line, Basket.class);
        }
        return basket;
    }

    public String[] getProducts() {
        return products;
    }

    public int[] getPrices() {
        return prices;
    }

    public int[] getCounts() {
        return counts;
    }
}
