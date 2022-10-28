package ru.netology;

import java.io.*;

public class Basket implements Serializable {
    private String[] products;
    private int[] prices;
    private int[] counts;

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
    public void saveBin(File file){
        try(
          ObjectOutputStream writer=new ObjectOutputStream(new FileOutputStream(file));
           ){
            writer.writeObject(new Basket(products,prices,counts));
        }catch(IOException e){
            System.out.println(e.getMessage());
        }
    }
    public void load(File file,String[] products,int[]prices,int[]counts){
        //String[]products;
        //int[]prices;
        //int[]counts;
        try(
            BufferedReader reader=new BufferedReader(new FileReader(file));
           ){
            products=reader.readLine().split(" ");
            String[]priceStr=reader.readLine().trim().split("");
            prices=new int[priceStr.length];
            for(int i=0;i<prices.length;i++){
                prices[i]=Integer.parseInt(priceStr[i]);
            }
            String[]countStr=reader.readLine().trim().split(" ");
            counts=new int[countStr.length];
            for (int i=0;i<counts.length;i++){
                counts[i]=Integer.parseInt(countStr[i]);
            }
            //Basket basket=new Basket(products,prices,counts);
        }catch(IOException e){
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
