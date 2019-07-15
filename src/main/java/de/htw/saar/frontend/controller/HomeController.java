package de.htw.saar.frontend.controller;

import de.htw.saar.frontend.master.MasterController;
import de.htw.saar.frontend.model.Artikel;
import de.htw.saar.frontend.model.PageNavigation;
import de.htw.saar.frontend.service.ArtikelService;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.util.ArrayList;

@Named
@RequestMapping("/home")
public class HomeController extends MasterController
{
    private ArtikelService artikelService = new ArtikelService();

    /* Pagnition */
    private int totalArtikelCount = 0;
    private int pageNumber = 0;
    private int size = 50;

    private String aktuellerArtikel;
    private String suchanfrage;
    private ArrayList<PageNavigation> pageNavigation;
    private ArrayList<Artikel> allArtikelList;
    private ArrayList<Artikel> yearArtikelList;
    private ArrayList<Artikel> monthArtikelList;

    public String getAktuellerArtikel(){
        return this.aktuellerArtikel;
    }

    public String getSuchanfrage()
    {
        return suchanfrage;
    }

    /**
     * Rufe die in der Datenbank enthaltenen Artikel ab
     * Ist der String inputQuery gesetzt soll danach gefiltert werden
     */
    public void findAllArtikel()
    {
        allArtikelList = new ArrayList<>();
        pageNavigation = new ArrayList<>();
        // clear navigation list
        pageNavigation.clear();

        if(this.getInputQuery() != null && this.getInputQuery().length() > 0)
        {
            allArtikelList = artikelService.getArtikelBySearch(this.getInputQuery());
            suchanfrage = "Suchanfrage: " + this.getInputQuery();
            this.setInputQuery("");
        }
        else
        {
            fillPageNavigationList();
            suchanfrage = "";
            allArtikelList = artikelService.getArtikelPaged(this.size,this.pageNumber * this.size);
        }
    }

    /**
     * Füllt die Liste für die Page Navigation
     */
    private void fillPageNavigationList()
    {
        totalArtikelCount = artikelService.getArtikelPageCount();
        int totalPageCount = (int)Math.ceil((double)totalArtikelCount / this.size) - 1;

        ArrayList<PageNavigation> pNavigation = new ArrayList<>();

        int startPage = pageNumber - 10;
        int targetPage = pageNumber + 11;

        if (startPage < 0)
        {
            startPage = 0;
        }

        if (targetPage >= totalPageCount)
        {
            targetPage = totalPageCount;
        }

        int counter = 0;
        for (int i = startPage; i < targetPage;i++)
        {
            // Erste Seite, falls nicht included
            if(startPage != 0 && counter == 0)
            {
                pNavigation.add(generatePageNavigation(counter));
            }

            pNavigation.add(generatePageNavigation(i));

            // Wenn letztes Item, adde letzte Seite
            if(i+1 == targetPage)
            {
                pNavigation.add(generatePageNavigation(totalPageCount));
            }

            counter++;
        }

        this.setPageNavigation(pNavigation);
    }

    private PageNavigation generatePageNavigation(int number)
    {
        PageNavigation page = new PageNavigation();
        page.setUrl("/home?page=" + number);
        page.setIndex(number);

        if(number == pageNumber)
        {
            page.setActive(true);
        }

        return page;
    }

    /**
     * Sucht in der allArtikelList nach allen Artikeln die dem uebergebenen Jahr
     * entsprechen und schreibt diese in yearArtikelList
     * @param year
     */
    //Derzeit nicht benutzt
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

    /**
     * Sucht in der yearArtikelList nach allen Artikeln die dem uebergebenen Monat
     * entsprechen und schreibt diese in monthArtikelList, wenn die yearArtikelList
     * nicht leer ist
     * @param month
     */
    //Derzeit nicht benutzt
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

    /**
     * Gibt die allArtikelList zurueck
     * @return allArtikelList
     */
    public ArrayList<Artikel> getAllArtikel() { return allArtikelList; }

    /**
     * Gibt die yearArtikelList zurueck
     * @return yearArtikelList
     */
    public ArrayList<Artikel> getYearArtikel() { return yearArtikelList; }

    /**
     * Gibt die monthArtikelList zurueck
     * @return monthArtikelList
     */
    public ArrayList<Artikel> getMonthArtikel() { return monthArtikelList; }

    @RequestMapping("")
    public String index(String page)
    {
        if(page == null || page.length() < 1)
        {
            this.pageNumber = 0;
        }
        else
        {
            this.pageNumber = Integer.parseInt(page);
        }

        return view("index",this);
    }

    @RequestMapping("/suche")
    public String suche(String q)
    {
        return view("index",this);
    }

    /**
     * initialisiert die suche und leitet an die entsprechende Seite weiter
     * @param query
     * @return
     */
    public String initSuche(String query)
    {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();

        if (query == null || query == "")
        {
            return view("index",this);
        }
        else
        {
            try{
                ec.redirect(getNavigation().navigate("home","suche") + "?q=" + query);
                return "";
            } catch(Exception ex) {
                return "";
            }

        }
    }

    public ArrayList<PageNavigation> getPageNavigation() {
        return pageNavigation;
    }

    public void setPageNavigation(ArrayList<PageNavigation> pageNavigation) {
        this.pageNavigation = pageNavigation;
    }
}
