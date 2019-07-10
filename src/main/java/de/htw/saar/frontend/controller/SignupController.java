package de.htw.saar.frontend.controller;

import de.htw.saar.frontend.master.MasterController;
import de.htw.saar.frontend.model.User;
import de.htw.saar.frontend.service.RequestService;
import de.htw.saar.frontend.service.UserService;
import de.htw.saar.frontend.controller.NavigationController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@ManagedBean(name = "SignupC")
@SessionScoped
public class SignupController extends MasterController implements Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    private String username;
    private String password;
    private String email;
    private boolean success;
    FacesContext fc = FacesContext.getCurrentInstance();
    ExternalContext ec = fc.getExternalContext();

    UserService userService = new UserService();

    @PostConstruct
    public void initial(){
    success=false;
    }

    /**
     * abbruch der registrierung
     */
    public void cancel()
    {
        try {
            ec.redirect(getNavigation().navigate("user", "login"));
        }catch (Exception ex)
        {
            LOGGER.info("Signup Cancel Error");
        }
    }

    /**
     * leitet den Benutzer zum login weiter
     */
    public void showLogin()
    {
        try {
            ec.redirect(getNavigation().navigate("user", "login"));
        }catch (Exception ex)
        {
            LOGGER.info("Signup go to login Error");
        }
    }

    /**
     * Den Benutezr mit den Angbaben registrieren
     * @return
     */
    public void registrieren()
    {
        try{
            // benutzer regisrteren
            userService.register(email,password,username);
            // erfolgreich page redirect
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Erfolgreich! Bitte loggen Sie sich ein!","Erfolgreich! Bitte loggen Sie sich ein!"));
        }catch(Exception ex){
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, ex.getMessage(),ex.getMessage()));
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getEmail(){
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
