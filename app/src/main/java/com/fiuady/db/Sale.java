package com.fiuady.db;

public class Sale {
    private int order_id;
    private int assembly_id;
    private String name;
    private String date;
    private int price;

    public Sale(int order_id, int assembly_id, String name, String date, int price) {
        this.order_id = order_id;
        this.assembly_id = assembly_id;
        this.name = name;
        this.date = date;
        this.price = price;
    }

    public int getOrder_id() {
        return order_id;
    }

    public int getAssembly_id() {
        return assembly_id;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public int getPrice() {
        return price;
    }
}
