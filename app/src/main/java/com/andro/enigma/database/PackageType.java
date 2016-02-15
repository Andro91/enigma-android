package com.andro.enigma.database;

/**
 * Created by Andrija on 14-Feb-16.
 */
public class PackageType {

    public PackageType(int id, String name, int size, double price) {
        this.id = id;
        this.name = name;
        this.size = size;
        this.price = price;
    }

    public int id;
    public String name;
    public int size;
    public double price;
}
