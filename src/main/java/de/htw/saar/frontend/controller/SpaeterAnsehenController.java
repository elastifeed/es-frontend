package de.htw.saar.frontend.controller;

import de.htw.saar.frontend.master.MasterController;
import de.htw.saar.frontend.model.ArtikelNew;
import de.htw.saar.frontend.service.ArtikelService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Named;
import java.util.ArrayList;

@Named
@RequestMapping("/spaeteransehen")
public class SpaeterAnsehenController extends MasterController
{
    private ArtikelService artikelService = new ArtikelService();
    private ArrayList<ArtikelNew> readLaterArtikelList;


    public void findReadLaterArtikel()
    {
        readLaterArtikelList = new ArrayList<>();
        readLaterArtikelList = artikelService.getReadLaterArtikel();
    }

    public ArrayList<ArtikelNew> getReadLaterArtikelList(){ return this.readLaterArtikelList; }

    @RequestMapping()
    public String index()
    {
        return view("index",this);
    }
}
