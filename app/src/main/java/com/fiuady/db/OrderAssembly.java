package com.fiuady.db;


public class OrderAssembly {

    private int id;
    private int assembly_id;
    private int qty;

    public OrderAssembly(int id, int assembly_id, int qty) {
        this.id = id;
        this.assembly_id = assembly_id;
        this.qty = qty;
    }

    public int getId() {
        return id;
    }

    public int getAssembly_id() {
        return assembly_id;
    }

    public int getQty() {
        return qty;
    }
}
