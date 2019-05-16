package de.htw.saar.frontend.service;

import de.htw.saar.frontend.TestData.Categorie;

import java.util.ArrayList;
import java.util.List;

public class CategorieService {

    List<Categorie> allCategories=new ArrayList<>();

    /** Test Methode gibt alle Kategorien Zurück
     * */
    public List<Categorie> findAllCategories(){

        Categorie c1=new Categorie(1,"Sport");
        Categorie c2=new Categorie(2,"Politic");
        Categorie c3=  new Categorie(3,"Social");
        Categorie c4=new Categorie(4,"Cultural");

        allCategories.add(c1);
        allCategories.add(c2);
        allCategories.add(c3);
        allCategories.add(c4);

        return allCategories;
    }
}