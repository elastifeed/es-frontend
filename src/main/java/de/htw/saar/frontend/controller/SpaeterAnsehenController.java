package de.htw.saar.frontend.controller;

import de.htw.saar.frontend.master.MasterController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.inject.Named;

@Named
@RequestMapping("/spaeteransehen")
public class SpaeterAnsehenController extends MasterController
{
    @RequestMapping()
    public String index()
    {
        return view("index",this);
    }
}
