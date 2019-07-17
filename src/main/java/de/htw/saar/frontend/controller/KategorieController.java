package de.htw.saar.frontend.controller;

import de.htw.saar.frontend.master.MasterController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;

@Named
@SessionScoped
@RequestMapping("/kategorie")
public class KategorieController extends MasterController
{
    @RequestMapping("")
    public String index(String id)
    {
        return view("index",this);
    }

}
