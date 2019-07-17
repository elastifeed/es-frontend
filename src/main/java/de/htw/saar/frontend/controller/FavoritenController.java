package de.htw.saar.frontend.controller;

import de.htw.saar.frontend.master.MasterController;
import de.htw.saar.frontend.model.Artikel;
import de.htw.saar.frontend.service.ArtikelService;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.faces.bean.SessionScoped;
import javax.inject.Named;
import java.util.ArrayList;

@Named
@SessionScoped
@RequestMapping("/favoriten")
public class FavoritenController extends MasterController
{
    private ArtikelService artikelService = new ArtikelService();
    private ArrayList<Artikel> favoritArtikelList;


    public void findFavoritArtikel()
    {
        favoritArtikelList = new ArrayList<>();
        favoritArtikelList = artikelService.getFavoritArtikel();
    }

    public ArrayList<Artikel> getFavoritArtikelList(){ return this.favoritArtikelList; }

    @RequestMapping()
    public String index()
    {
        return view("index",this);
    }
}
