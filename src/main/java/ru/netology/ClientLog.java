package ru.netology;

import au.com.bytecode.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ClientLog {
    private int productNum;
    private int amount;
    private String[]productNumAmount=new String[2];
    public ClientLog(int productNum, int amount){
        this.productNum=productNum;
        this.amount=amount;
    }
    public String[] log(int productNum,int amount){
        productNumAmount[0]=productNum+" ";
        productNumAmount[1]=amount+" ";

     return productNumAmount;
    }
    public void exportAsCSV(File file){
        try(
                CSVWriter writer=new CSVWriter(new FileWriter(file,true));
           ){
            writer.writeNext(log(productNum,amount));
        }catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int getProductNum() {
        return productNum;
    }

    public int getAmount() {
        return amount;
    }

    public String[] getProductNumAmount() {
        return productNumAmount;
    }
}
