package ru.netology;

import java.io.File;
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
            basket.saveBin(file);
            Basket.loadFromBinFile(file);
        }
    }


}

