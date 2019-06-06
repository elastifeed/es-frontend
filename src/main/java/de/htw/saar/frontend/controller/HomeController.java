package de.htw.saar.frontend.controller;

import de.htw.saar.frontend.master.MasterController;
import de.htw.saar.frontend.model.Artikel;
import de.htw.saar.frontend.service.ArtikelService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.util.ArrayList;

@ManagedBean(name = "Home")
@ViewScoped
public class HomeController extends MasterController
{
    private ArtikelService artikelService = new ArtikelService();

    private String aktuellerArtikel;
    private ArrayList<Artikel> allArtikelList;

    public String getAktuellerArtikel(){
        return this.aktuellerArtikel;
    }

    public void findAllArtikel()
    {
        allArtikelList = new ArrayList<>();
        allArtikelList = artikelService.getAllArtikel();
    }

    public ArrayList<Artikel> getAllArtikel()
    {
        return allArtikelList;
    }

    public String toHome() {

            return "/view/home/home.xhtml";

        //return view("home",this);
    }




    /*
    @RequestMapping("/artikel")
    public String artikel(String id)
    {
        if (id == null)
        {
            return index();
        }
        else
        {
            this.aktuellerArtikel = id;
            return view("artikel",this);
        }

    }
    */
}
