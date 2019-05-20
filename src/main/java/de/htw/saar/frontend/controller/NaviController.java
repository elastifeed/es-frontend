package de.htw.saar.frontend.controller;


import de.htw.saar.frontend.TestData.Categorie;
import de.htw.saar.frontend.service.CategorieService;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.util.List;

@ManagedBean (name ="NaviC")
@ViewScoped
public class NaviController {

    private CategorieService categorieService=new CategorieService();
    private List<Categorie> allCategories;

    public List<Categorie> findAllCategories(){

        allCategories=categorieService.findAllCategories();
        return allCategories;
    }

    public CategorieService getCategorieService() {
        return categorieService;
    }

    public void setCategorieService(CategorieService categorieService) {
        this.categorieService = categorieService;
    }

    public void setAllCategories(List<Categorie> allCategories) {
        this.allCategories = allCategories;
    }

    public List<Categorie> getAllCategories() {
        return allCategories;
    }


    public String toLogin(){
        return "/view/login/login.xhtml";
    }
}
