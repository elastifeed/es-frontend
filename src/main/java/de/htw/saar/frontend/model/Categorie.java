package de.htw.saar.frontend.model;
/**
 *
 * ********  Zum Testen ******
 *
 * */
public class Categorie {
private int id;
private String name;

    public Categorie(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
