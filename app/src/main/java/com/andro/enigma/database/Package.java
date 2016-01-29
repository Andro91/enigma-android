package com.andro.enigma.database;

/**
 * Created by Internet on 28-Jan-16.
 */
public class Package {

    public Package(String id, String title, String lang, String date_created, String published, String id_type) {
        this.id = id;
        this.title = title;
        this.lang = lang;
        this.date_created = date_created;
        this.published = published;
        this.id_type = id_type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getDate_created() {
        return date_created;
    }

    public void setDate_created(String date_created) {
        this.date_created = date_created;
    }

    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        this.published = published;
    }

    public String getIdType() {
        return id_type;
    }

    public void setIdType(String id_type) {
        this.id_type = id_type;
    }

    private String id;
    private String title;
    private String lang;
    private String date_created;
    private String published;
    private String id_type;
}
