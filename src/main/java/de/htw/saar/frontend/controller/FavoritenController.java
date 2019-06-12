package de.htw.saar.frontend.controller;

import de.htw.saar.frontend.master.MasterController;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Named;

@Named
@RequestMapping("/favoriten")
public class FavoritenController extends MasterController
{
    @RequestMapping()
    public String index()
    {
        return view("index",this);
    }
}
