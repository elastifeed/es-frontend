package de.htw.saar.frontend.controller;


import de.htw.saar.frontend.model.Categorie;
import de.htw.saar.frontend.service.CategorieService;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.util.List;

/**
 * Handles the navigation between the controllers
 *
 */
@ManagedBean (name ="Navigation")
@ViewScoped
public class NavigationController
{

    // Constants
    private static final String basePath = "/view/";
    private static final String pathDivider = "/";
    private static final String fileExtension = ".xhtml";

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
        return createNavigationLink("login","login");
    }

    public String toHome(){
        return createNavigationLink("home","index");
    }

    public String toTimeline(){
        return createNavigationLink("timeline","index");
    }

    public String toFavoriten(){
        return createNavigationLink("favoriten","index");
    }

    public String toSpaeteransehen(){
        return createNavigationLink("spaeteransehen","index");
    }

    public String toKategorie(){
        return createNavigationLink("kategorie","index");
    }

    public String toArtikel() {
        return createNavigationLink("shared","artikel");
    }

    /**
     * Navigate to the desired page
     * requires page name and folder name
     * Infor: these parameters are not case sensitive
     * @param Page
     * @param Folder
     * @return
     */
    public String createNavigationLink(String Folder, String Page)
    {
        return basePath + Folder.toLowerCase() + pathDivider + Page.toLowerCase();
    }
}
