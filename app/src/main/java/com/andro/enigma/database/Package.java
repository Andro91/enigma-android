package com.andro.enigma.database;

/**
 * Created by Internet on 28-Jan-16.
 */
public class Package {

    public Package(int id, String title, String lang, String date_created, int idType) {
        this.id = id;
        this.title = title;
        this.lang = lang;
        this.dateCreated = date_created;
        this.idType = idType;
    }

    public Package(int id, String title, String lang, String date_created, int idType,
                   int enigmaCount, int solvedCount, int purchased, double price) {
        this.id = id;
        this.title = title;
        this.lang = lang;
        this.dateCreated = date_created;
        this.idType = idType;
        this.enigmaCount = enigmaCount;
        this.solvedCount = solvedCount;
        this.purchased = purchased;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getDateCreated() {return dateCreated;}

    public void setDateCreated(String date_created) {this.dateCreated = date_created;}

    public int getIdType() {
        return idType;
    }

    public void setIdType(int id_type) {
        this.idType = id_type;
    }

    public int getEnigmaCount() {
        return enigmaCount;
    }

    public void setEnigmaCount(int enigmaCount) {
        this.enigmaCount = enigmaCount;
    }

    public int getSolvedCount() {
        return solvedCount;
    }

    public void setSolvedCount(int solvedCount) {
        this.solvedCount = solvedCount;
    }

    public int getPurchased() {
        return purchased;
    }

    public void setPurchased(int purchased) {
        this.purchased = purchased;
    }

    private int id;
    private String title;
    private String lang;
    private int idType;
    private String dateCreated;
    private int enigmaCount;
    private int solvedCount;
    private int purchased;
    private double price;


    //Packages table contract
    public static final String TABLE_NAME = "packages";

    public static final String COLUMN_NAME_ID = "id";
    public static final String COLUMN_NAME_TITLE = "title";
    public static final String COLUMN_NAME_LANG = "lang";
    public static final String COLUMN_NAME_ID_TYPE = "idType";
    public static final String COLUMN_NAME_DATE_CREATED = "dateCreated";
    public static final String COLUMN_NAME_ENIGMA_COUNT = "enigmaCount";
    public static final String COLUMN_NAME_SOLVED_COUNT = "solvedCount";
    public static final String COLUMN_NAME_PURCHASED = "purchased";
    public static final String COLUMN_NAME_PRICE = "price";


    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
