package com.fiuady.db;


public class Order {

    private int id;
    private int status_id;
    private int customer_id;
    private String date;
    private String change_log;

    public Order(int id, int status_id, int customer_id, String date, String change_log) {
        this.id = id;
        this.status_id = status_id;
        this.customer_id = customer_id;
        this.date = date;
        this.change_log = change_log;
    }

    public int getId() {return id;}

    public int getStatus_id() {return status_id;}

    public int getCustomer_id() {return customer_id;}

    public String getDate() {return date;}

    public String getChange_log() {return change_log;}

}
