package com.github.osvaldsoza.model;

public class ConversionResponse {
    private String from;
    private String to;
    private double amount;
    private double converted;

    public ConversionResponse(String from, String to, double amount, double converted) {
        this.from = from;
        this.to = to;
        this.amount = amount;
        this.converted = converted;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getConverted() {
        return converted;
    }

    public void setConverted(double converted) {
        this.converted = converted;
    }
}
