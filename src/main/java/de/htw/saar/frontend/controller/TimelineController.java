package de.htw.saar.frontend.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import de.htw.saar.frontend.helper.ElasticSearchManager;
import de.htw.saar.frontend.master.MasterController;
import de.htw.saar.frontend.model.Artikel;
import de.htw.saar.frontend.model.TimelineDateMonth;
import de.htw.saar.frontend.service.ArtikelService;
import de.htw.saar.frontend.service.TimelineDateService;
import org.omnifaces.util.Json;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.persistence.criteria.CriteriaBuilder;
import java.util.ArrayList;

@Named
@RequestMapping("/timeline")
public class TimelineController extends MasterController{

    TimelineDateService timelineDateService = new TimelineDateService();

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
            allArtikelList = artikelService.getAllArtikelInIndex("dummy",10000);
        }
    }

    /**
     * Sucht in der allArtikelList nach allen Artikeln die dem uebergebenen Jahr
     * und Monat entsprechen und schreibt diese in monthArtikelList
     * @param year
     * @param month
     */
    public void findAllArtikelByYearAndMonth(int year, int month)
    {
        if(allArtikelList == null || allArtikelList.size() == 0)
        {
            findAllArtikel();
        }

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
        return timelineDateService.getYearList();
    }

    /**
     * Ruft alle Monate ab
     * Schreibt diese ine eine Liste und gibt diese zurueck
     * @return allMonth
     */
    public ArrayList<TimelineDateMonth> getAllMonth(){
        return timelineDateService.getMonthList();
    }

    @RequestMapping()
    public String index()
    {
        return view("index",this);
    }

    @RequestMapping(value = "/loadmoreartikel", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ArrayList<Artikel> loadmoreartikel(@RequestParam String year, @RequestParam String month)
    {
        ElasticSearchManager manager = new ElasticSearchManager();
        ArrayList<Artikel> result = manager.getArtikelPaged(50,0);

        return result;
    }
}
