package de.htw.saar.frontend.controller;

import de.htw.saar.frontend.master.MasterController;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class FavoritenController extends MasterController
{
    @RequestMapping("/favoriten")
    public String index()
    {
        return view("index",this);
    }
}
