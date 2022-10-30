package ru.netology;

import java.io.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        File file = new File("basket.bin");
        Scanner scan = new Scanner(System.in);
        String[] products = {"молоко", "хлеб", "гречка"};
        int[] prices = {50, 14, 80};
        Basket basket = new Basket(products, prices);
        System.out.println("список продуктов для покупок: ");
        for (int i = 0; i < products.length; i++) {
            System.out.println("\t" + (i + 1) + ". " + products[i] + " по " + prices[i] + " р/шт");
        }
        String inputStr;
        while (true) {
            System.out.print("введите номер продукта и колличество через пробел или end: ");
            inputStr = scan.nextLine();
            if (inputStr.equals("end")) {
                break;
            }
            int productNum;
            int amount;
            String[] parts = inputStr.split(" ");
            productNum = Integer.parseInt(parts[0]) - 1;
            amount = Integer.parseInt(parts[1]);
            basket.addToCart(productNum, amount);
        }
            if (!file.exists()) {
                basket.printCart();
                basket.saveText(file);
            } else {
                basket.saveText(file);
                loadFromTxtFile(file);
            }
        }


    public static Basket loadFromTxtFile(File textFile) {
        String[] products;
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
            basket = new Basket(products, prices, counts);
            basket.printCart();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return basket;
    }

}

