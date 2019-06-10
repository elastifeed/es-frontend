package de.htw.saar.frontend.controller;

import de.htw.saar.frontend.master.MasterController;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name ="Artikel")
@ViewScoped
public class ArtikelController extends MasterController
{
    private String artikelId;
    public String getArtikelId()
    {
        return this.artikelId;
    }


    public String test()
    {
        System.out.println("Ohne Parameter");
        return this.getNavigationController().toFavoriten();
    }

    public String showArtikel()
    {
        System.out.println("Ohne Parameter");
        return this.getNavigationController().toArtikel();
    }

    public String showArtikel(String id)
    {
        System.out.println("Mit parameter: " + id);

        if (id == null)
        {
            return this.getNavigationController().toHome();
        }
        else
        {
            this.artikelId = id;
            return this.getNavigationController().toArtikel();
        }
    }
}
