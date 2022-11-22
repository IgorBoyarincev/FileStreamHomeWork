package ru.netology;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception {

        Scanner scan = new Scanner(System.in);

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse("shop.xml");

        XPath xPath = XPathFactory.newInstance().newXPath();

        Boolean doLoad = Boolean.parseBoolean(xPath
                .compile("/config/load/enabled").evaluate(doc));
        String loadFileName = xPath
                .compile("/config/load/fileName").evaluate(doc);
        String loadFormat = xPath
                .compile("/config/load/format").evaluate(doc);

        Boolean doSave = Boolean.parseBoolean(xPath
                .compile("/config/save/enabled").evaluate(doc));
        String saveName = xPath
                .compile("/config/save/fileName").evaluate(doc);
        String saveFormat = xPath
                .compile("/config/save/format").evaluate(doc);
        System.out.println(doSave + " " + saveName + " " + saveFormat);
        if (doSave) {
            switch (saveFormat) {
                case "txt":
                    saveName = "basket.txt";
                    break;
                case "json":
                    saveName = "basket.json";
                    break;
                case "bin":
                    saveName = "basket.bin";
                    break;
            }
        } else {
            System.out.println("invalid format file");
        }

        Boolean doLog = Boolean.parseBoolean(xPath
                .compile("/config/log/enabled").evaluate(doc));
        String logFileName = xPath
                .compile("/config/log/fileName").evaluate(doc);
        System.out.println(saveName);
        File file = new File(saveName);

        String[] products = {"молоко", "хлеб", "гречка"};
        int[] prices = {50, 14, 80};
        Basket basket = new Basket(products, prices);
        ClientLog cl = new ClientLog(products, prices);
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
            cl.log(productNum, amount);
        }
        if (!file.exists()) {
            basket.printCart();
            if (doSave) {
                switch (saveFormat) {
                    case "txt":
                        basket.saveText(file);
                        break;
                    case "json":
                        basket.saveJsonFile(file);
                        break;
                    case "bin":
                        basket.saveBin(file);
                        break;
                }
            }
            if (doLog) {
                cl.exportAsCSV();
            }
        } else {
            if (doLog) {
                cl.exportAsCSV();
            }
            if (doSave) {
                switch (saveFormat) {
                    case "txt":
                        basket.saveText(file);
                        break;
                    case "json":
                        basket.saveJsonFile(file);
                        break;
                    case "bin":
                        basket.saveBin(file);
                        break;
                }
            }
            if (doLoad) {
                switch (loadFormat) {
                    case "txt":
                        Basket.loadFromTxtFile(file);
                        break;
                    case "json":
                        Basket.loadFromJSONFile(file);
                        break;
                    case "bin":
                        Basket.loadFromBinFile(file);
                }
            }
            basket.printCart();
        }
    }
}

