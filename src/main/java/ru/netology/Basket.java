package ru.netology;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;

public class Basket implements Serializable {
    private String[] products;
    private int[] prices;
    private int[] counts;
    JSONObject obj = new JSONObject();

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

    public void saveText(File textFile) {
        try (PrintWriter writer = new PrintWriter(textFile);) {
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
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public void saveBin(File file) {
        try (
                ObjectOutputStream writer = new ObjectOutputStream(new FileOutputStream(file));
        ) {
            writer.writeObject(new Basket(products, prices, counts));
        } catch (IOException e) {
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

    public static Basket loadFromTxtFile(File textFile) {
        String[] products = null;
        int[] prices;
        int[] counts;
        Basket basket = null;
        try (
                BufferedReader reader = new BufferedReader(new FileReader(textFile));
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
            //basket.load(textFile,products,prices,counts);
            basket = new Basket(products, prices, counts);
            basket.printCart();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return basket;
    }

    public static void loadFromBinFile(File file) {
        try (
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));
        ) {
            Basket basket = (Basket) in.readObject();
            basket.printCart();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void loadFromJSONFile(File file) {
        String[] products;
        int[] prices;
        int[] counts;
        Basket basket = null;
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
            basket = new Basket(products, prices, counts);
            basket.printCart();
        } catch (IOException | ParseException e) {
            System.out.println(e.getMessage());
        }
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
