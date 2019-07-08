package de.htw.saar.frontend.controller;

import de.htw.saar.frontend.helper.MinifyObject;
import de.htw.saar.frontend.master.MasterController;
import de.htw.saar.frontend.model.Artikel;
import de.htw.saar.frontend.model.ArtikelDisplay;
import de.htw.saar.frontend.model.TimelineDateMonth;
import de.htw.saar.frontend.service.ElasticSearchService;
import de.htw.saar.frontend.service.TimelineDateService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Named;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

@Named
@RequestMapping("/timeline")
public class TimelineController extends MasterController
{
    ElasticSearchService service = new ElasticSearchService();
    TimelineDateService timelineDateService = new TimelineDateService();
    MinifyObject minifyObject = new MinifyObject();

    // Speichert den bereits geladenen content
    //Map<String,Integer> loadedContentMap = new IdentityHashMap<>();
    Map<String,Integer> loadedContentMap = new HashMap<>();

    @RequestMapping()
    public String index()
    {
        return view("index",this);
    }

    /**
     * Mehr Artikel
     * @param year
     * @param month
     * @return
     */
    @RequestMapping(value = "/loadmoreartikel", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public ArrayList<ArtikelDisplay> loadmoreartikel(@RequestParam String year, @RequestParam String month, @RequestParam Boolean isinit)
    {
        if(year.isEmpty() || month.isEmpty())
            return null;

        String key = year + "_" + month;
        int requestSize = 50;
        int startingFrom = 0;

        // Ausgabe zum Test des Key Value
        System.out.println("Key Value: " + loadedContentMap.get(key));

        // Suche den Eintrag in der loadedContentMap
        if(loadedContentMap.containsKey(key) == false)
        {
            loadedContentMap.put(key,startingFrom + requestSize);
        }
        else
        {
            // Es existiert schon die Anzeige für die Artikel
            // Wurde aber als init angefordert
            // return empty array
            if(isinit)
            {
                return new ArrayList<ArtikelDisplay>();
            }

            startingFrom = loadedContentMap.get(key).intValue();
            //Update entry
            loadedContentMap.put(key,startingFrom + requestSize);
        }


        ArrayList<Artikel> resultRequest = service.getArtikelPagedByYearAndMonth(requestSize,startingFrom,year,month);
        ArrayList<ArtikelDisplay> result = new ArrayList<>();
        if(resultRequest != null && resultRequest.size() != 0) {
            resultRequest.forEach(x -> result.add(minifyObject.getMinifyArtikel(x)));
        }

        return result;
    }

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
}
