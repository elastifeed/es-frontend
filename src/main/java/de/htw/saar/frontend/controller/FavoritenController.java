package de.htw.saar.frontend.controller;

import de.htw.saar.frontend.helper.MinifyObject;
import de.htw.saar.frontend.master.MasterController;
import de.htw.saar.frontend.model.Artikel;
import de.htw.saar.frontend.model.ArtikelNew;
import de.htw.saar.frontend.service.ArtikelService;
import de.htw.saar.frontend.service.ElasticSearchService;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;
import java.util.ArrayList;

@Named
@RequestMapping("/favoriten")
public class FavoritenController extends MasterController
{
    private ArtikelService artikelService = new ArtikelService();
    private ArrayList<ArtikelNew> favoritArtikelList;


    public void findFavoritArtikel()
    {
        favoritArtikelList = new ArrayList<>();
        favoritArtikelList = artikelService.getFavoritArtikel();
    }

    public ArrayList<ArtikelNew> getFavoritArtikelList(){ return this.favoritArtikelList; }

    @RequestMapping()
    public String index()
    {
        return view("index",this);
    }
}
