package de.htw.saar.frontend.controller;

import de.htw.saar.frontend.master.MasterController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SpaeterAnsehenController extends MasterController
{
    @RequestMapping("/spaeteransehen")
    public String main()
    {
        return view("index",this);
    }
}