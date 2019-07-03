package de.htw.saar.frontend.controller;

import de.htw.saar.frontend.master.MasterController;
import de.htw.saar.frontend.model.Artikel;
import de.htw.saar.frontend.model.TimelineDateMonth;
import de.htw.saar.frontend.service.ArtikelService;
import de.htw.saar.frontend.service.TimelineDateService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;

@Named
@RequestMapping("/timeline")
public class TimelineController extends MasterController{

    TimelineDateService timelineDateService = new TimelineDateService();
    private ArrayList<Integer> allYear;
    private ArrayList<TimelineDateMonth> allMonth;

    private ArtikelService artikelService = new ArtikelService();
    private ArrayList<Artikel> allArtikelList;
    private ArrayList<Artikel> monthArtikelList;

    /**
     * Rufe die in der Datenbank enthaltenen Artikel ab
     * Ist der String inputQuery gesetzt soll danach gefiltert werden
     */
    public void findAllArtikel()
    {
        allArtikelList = new ArrayList<>();

        if(this.getInputQuery() != null && this.getInputQuery().length() > 0)
        {
            allArtikelList = artikelService.getArtikelBySearch(this.getInputQuery());
            this.setInputQuery("");
        }
        else
        {
            allArtikelList = artikelService.getAllArtikel();
        }
    }

    public void findAllArtikelByYearAndMonth(int year, int month)
    {
        /*
        String string = "004-034556";
        String[] parts = string.split("-");
        String part1 = parts[0]; // 004
        String part2 = parts[1]; // 034556
        */
        //allArtikelList.clear();
        findAllArtikel();
        monthArtikelList = new ArrayList<>();
        monthArtikelList.clear();
        if(!allArtikelList.isEmpty()){
            for(Artikel artikel : allArtikelList){
                int createdYear = artikel.getCreatedYearAsInt();
                int createdMonth = artikel.getCreatedMonthAsInt();
                if(createdYear == year && createdMonth == month){
                    monthArtikelList.add(artikel);
                }
            }
        }
    }

    public ArrayList<Artikel> getMonthArtikelList(){ return this.monthArtikelList; }

    /**
     * Ruft alle vordefinierten Jahre ab
     * Schreibt diese in eine Liste und gibt diese zurueck
     * @return allYear
     */
    public ArrayList<Integer> getAllYear(){
        allYear = new ArrayList<>();
        allYear.clear();
        allYear= timelineDateService.getYearList();
        return allYear;
    }

    /**
     * Ruft alle Monate ab
     * Schreibt diese ine eine Liste und gibt diese zurueck
     * @return allMonth
     */
    public ArrayList<TimelineDateMonth> getAllMonth(){
        allMonth = new ArrayList<>();
        allMonth.clear();
        allMonth = timelineDateService.getMonthList();
        return allMonth;
    }

    @RequestMapping()
    public String index()
    {
        return view("index",this);
    }
}
