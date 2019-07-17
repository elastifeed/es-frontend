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
@RequestMapping("/spaeteransehen")
public class SpaeterAnsehenController extends MasterController
{
    private ArtikelService artikelService = new ArtikelService();
    private ArrayList<Artikel> readLaterArtikelList;


    public void findReadLaterArtikel()
    {
        readLaterArtikelList = new ArrayList<>();
        readLaterArtikelList = artikelService.getReadLaterArtikel();
    }

    public ArrayList<Artikel> getReadLaterArtikelList(){ return this.readLaterArtikelList; }

    @RequestMapping()
    public String index()
    {
        return view("index",this);
    }
}
