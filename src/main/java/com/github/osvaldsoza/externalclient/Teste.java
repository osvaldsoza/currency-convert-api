package com.github.osvaldsoza.externalclient;

public class Teste {

    public static void main(String[] args) {
        ExchangeRateClient client = new ExchangeRateClient();
        try {
            double convertedAmount = client.convert("BRL", "USD", 100.0);
            System.out.println("100 USD to EUR: " + convertedAmount);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
