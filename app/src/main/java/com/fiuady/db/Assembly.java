package com.fiuady.db;

public class Assembly {
    private int id;
    private String description;

    public Assembly(int id, String description){
        this.id = id;
        this.description=description;
    }

    public int getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String descripcion) {
        this.description = description;
    }
}
