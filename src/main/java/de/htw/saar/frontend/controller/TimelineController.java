package de.htw.saar.frontend.controller;

import de.htw.saar.frontend.master.MasterController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TimelineController extends MasterController
{
    @RequestMapping("/timeline")
    public String main()
    {
        return view("index",this);
    }
}