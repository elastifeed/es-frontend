package de.htw.saar.frontend.controller;

import de.htw.saar.frontend.master.MasterController;
import de.htw.saar.frontend.model.Artikel;
import de.htw.saar.frontend.service.ElasticSearchService;
import org.primefaces.event.ToggleEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.htw.saar.frontend.controller.DateRangeBean;

@Named
@RequestMapping("/erweitertesuche")
@ManagedBean
@SessionScoped
public class ErweiterteSucheController extends MasterController
{
    ElasticSearchService elasticSearchService=new ElasticSearchService();


    @RequestMapping("")
    public String index()
    {
        return view("index",this);
    }
    private Date startDate;
    private Date endDate;
    private String text;
    private Boolean exact;
    private int pageCount;
    private String sort;
    private ArrayList<Artikel> searchArtikelList;
    List<List<Artikel>> allArtikel;

    private DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy HH:mm");

    public ErweiterteSucheController() {
        Calendar c1 = Calendar.getInstance();
        endDate = c1.getTime();
        Calendar c2 = Calendar.getInstance();
        c2.setTime(endDate);
        c2.add(Calendar.HOUR,-24);
        startDate = c2.getTime();
        sort="desc";
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Boolean getExact() {
        return exact;
    }

    public void setExact(Boolean exact) {
        this.exact = exact;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public List<List<Artikel>> getAllArtikel() {
        return allArtikel;
    }

    public void setAllArtikel(List<List<Artikel>> allArtikel) {
        this.allArtikel = allArtikel;
    }

    public ArrayList<Artikel> getSearchArtikelList() {
        return searchArtikelList;
    }

    public void setSearchArtikelList(ArrayList<Artikel> searchArtikelList) {
        this.searchArtikelList = searchArtikelList;
    }

    public void handleToggle(ToggleEvent event) {
        FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Toggled", "Visibility:" + event.getVisibility());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void searchResult(){
       searchArtikelList= elasticSearchService.getArtikelPagedDateRange(1000,0,startDate,endDate,text,exact,sort);
    }

    public void pageSort(){
        if(searchArtikelList.size()%20!=0) {
            pageCount = searchArtikelList.size()/20+1;
        }else{
            pageCount=searchArtikelList.size()/20;
        }
        allArtikel=new ArrayList<List<Artikel>>();
        int index=0;
        while (pageCount!=0){
            int restArtikel=searchArtikelList.size()-index+1;
            if(restArtikel>=20){
                List<Artikel> pageArtikel = searchArtikelList.subList(index, index + 20);
                allArtikel.add(pageArtikel);
            }else {
                List<Artikel> pageArtikel = searchArtikelList.subList(index,index+restArtikel-1);
                allArtikel.add(pageArtikel);
            }
            index+=20;
            pageCount--;
        }
    }

    public Boolean isFound(){
        if (searchArtikelList!=null) {
            if (searchArtikelList.size() != 0) {
                return true;
            } else {
                return false;
            }
        }
        return true;
    }
public Boolean tabViewVisible(){
    if (searchArtikelList!=null) {
        if (searchArtikelList.size() != 0) {
            return false;
        } else {
            return true;
        }
    }
    return true;
}

    public String getStartDateRangeString() {
        return String.format("%s", formatter.format(startDate));
    }
    public String getEndDateRangeString() {
        return String.format("%s", formatter.format(endDate));
    }

    }


