package com.fiuady.db;

import java.util.List;

public class OrderAssembly {

    private int id;
    private int assembly_id;
    private int qty;
    private CompuStore compuStore;

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

    public void setId(int id) {
        this.id = id;
    }

    public void setAssembly_id(int assembly_id) {
        this.assembly_id = assembly_id;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public Assembly getAssemblyById(int assembly_id){
        this.assembly_id = assembly_id;
        return compuStore.getAssemblyById(assembly_id);
    }

}
