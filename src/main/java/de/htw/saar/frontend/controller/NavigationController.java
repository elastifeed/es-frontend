package de.htw.saar.frontend.controller;


import de.htw.saar.frontend.model.Categorie;
import de.htw.saar.frontend.service.CategorieService;
import javax.inject.Named;
import java.util.List;

/**
 * Handles the navigation between the controllers
 *
 */
@Named
public class NavigationController
{

    // Constants
    private static final String pathDivider = "/";

    private CategorieService categorieService=new CategorieService();
    private List<Categorie> allCategories;

    public List<Categorie> findAllCategories(){

        allCategories=categorieService.findAllCategories();
        return allCategories;
    }

    public void setAllCategories(List<Categorie> allCategories) {
        this.allCategories = allCategories;
    }

    public List<Categorie> getAllCategories() {
        return allCategories;
    }


    /**
     * Navigate to the desired page
     * requires page name and folder name
     * Infor: these parameters are not case sensitive
     * @return
     */
    public String navigateHome(String action)
    {
        return "/" + action;
    }

    /**
     * Navigate to the desired page
     * requires page name and folder name
     * Infor: these parameters are not case sensitive
     * @return
     */
    public String navigateHome()
    {
        return "/";
    }

    /**
     * Navigate to the desired page
     * requires page name and folder name
     * Infor: these parameters are not case sensitive
     * @param Controller
     * @return
     */
    public String navigate(String Controller)
    {
        return pathDivider + Controller.toLowerCase();
    }

    /**
     * Navigate to the desired page
     * requires page name and folder name
     * Infor: these parameters are not case sensitive
     * @param Controller
     * @param Action
     * @return
     */
    public String navigate(String Controller, String Action)
    {
        return pathDivider + Controller.toLowerCase() + pathDivider + Action.toLowerCase();
    }

}
