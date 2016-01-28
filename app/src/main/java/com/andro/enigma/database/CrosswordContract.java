package com.andro.enigma.database;

/**
 * Created by Andrija on 28-Aug-15.
 */
public class CrosswordContract {

    public CrosswordContract() {}

    public static abstract class CrosswordEntry{
        public static final String TABLE_NAME = "crosswords";
        public static final String COLUMN_NAME_ID = "id";
        public static final String COLUMN_NAME_PACKAGE_ID = "packageId";
        public static final String COLUMN_NAME_CROSSWORD_NUMBER = "crosswordNumber";
        public static final String COLUMN_NAME_TEXT = "text";
        public static final String COLUMN_NAME_SOLVED = "solved";
        public static final String COLUMN_NAME_TIME = "time";
        public static final String COLUMN_NAME_LOCALE = "locale";
    }
    }
