package de.htw.saar.frontend.controller;

import de.htw.saar.frontend.master.MasterController;
import de.htw.saar.frontend.model.Artikel;
import de.htw.saar.frontend.service.ArtikelService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;
import java.util.ArrayList;

@Named
@RequestMapping("/")
public class HomeController extends MasterController
{
    @RequestMapping()
    public String index()
    {
        return view("index",this);
    }

    private ArtikelService artikelService = new ArtikelService();

    private String aktuellerArtikel;
    private ArrayList<Artikel> allArtikelList;
    private ArrayList<Artikel> yearArtikelList;
    private ArrayList<Artikel> monthArtikelList;

    public String getAktuellerArtikel(){
        return this.aktuellerArtikel;
    }

    public void findAllArtikel()
    {
        allArtikelList = new ArrayList<>();
        allArtikelList = artikelService.getAllArtikel();
    }

    public void findAllArtikelByYear(int year)
    {
        allArtikelList = new ArrayList<>();
        allArtikelList = artikelService.getAllArtikel();
        yearArtikelList = new ArrayList<>();
        yearArtikelList.clear();
        for (Artikel artikel : allArtikelList) {
            int createdYear = artikel.getCreatedYearAsInt();
            if(createdYear == year){
                yearArtikelList.add(artikel);
            }
        }
    }

    public void findAllArtikelByMonth(int month)
    {
        monthArtikelList = new ArrayList<>();
        monthArtikelList.clear();
        if(!yearArtikelList.isEmpty()){
            for (Artikel artikel : yearArtikelList){
                int createdMonth = artikel.getCreatedMonthAsInt();
                if(createdMonth == month){
                    monthArtikelList.add(artikel);
                }
            }
        }
    }

    public ArrayList<Artikel> getAllArtikel()
    {
        return allArtikelList;
    }

    public ArrayList<Artikel> getYearArtikel() { return yearArtikelList; }

    public ArrayList<Artikel> getMonthArtikel() { return monthArtikelList; }

}
