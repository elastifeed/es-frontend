package de.htw.saar.frontend.controller;
import org.springframework.web.bind.annotation.RequestMapping;

@org.springframework.stereotype.Controller
public class Controller {
    @RequestMapping("/")
    public String main(){
        return "/view/shared/_layout.xhtml";
    }
}