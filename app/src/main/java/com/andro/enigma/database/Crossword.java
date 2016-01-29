package com.andro.enigma.database;

/**
 * Created by andrija.karadzic on 01.09.2015.
 * Class to define the basic properties of crosswords
 */
public class Crossword {

    public Crossword(int ID, int crosswordNumber, int packageId, String text, String solved, String time, String locale) {
        this.ID = ID;
        this.crosswordNumber = crosswordNumber;
        this.packageId = packageId;
        this.text = text;
        this.solved = solved;
        this.time = time;
        this.locale = locale;
    }

    public int ID;
    public int crosswordNumber;
    public int packageId;
    public String text;
    public String solved;
    public String time;
    public String locale;

}