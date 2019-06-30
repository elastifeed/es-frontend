package de.htw.saar.frontend.controller;

import de.htw.saar.frontend.master.MasterController;
import de.htw.saar.frontend.model.TimelineDateMonth;
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
