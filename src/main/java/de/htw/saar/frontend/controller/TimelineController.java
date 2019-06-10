package de.htw.saar.frontend.controller;

import de.htw.saar.frontend.master.MasterController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean(name ="Timeline")
@ViewScoped
public class TimelineController extends MasterController
{

}
