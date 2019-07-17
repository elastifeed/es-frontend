package de.htw.saar.frontend.controller;

import de.htw.saar.frontend.helper.CurrentUser;
import de.htw.saar.frontend.master.MasterController;
import de.htw.saar.frontend.model.ArtikelMetric;
import de.htw.saar.frontend.model.Categorie;
import de.htw.saar.frontend.model.SearchMetric;
import de.htw.saar.frontend.model.User;
import de.htw.saar.frontend.service.MetricDataService;
import de.htw.saar.frontend.service.RequestService;
import de.htw.saar.frontend.service.UserService;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.SessionScoped;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.annotation.SessionScope;

@org.springframework.stereotype.Controller

@Named
@SessionScoped
@RequestMapping("/user")
public class UserController extends MasterController implements Serializable
{
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    RequestService requestService = new RequestService();
    UserService userService = new UserService();

    private String email;
    private String password;

    private boolean loggedIn=isLoggedIn();
    List<Categorie> allCategories =new ArrayList<>();

    private User currentUser;

    @RequestMapping("")
    public String root()
    {
        return view("login", this);
    }

    @RequestMapping("/login")
    public String login()
    {
        return view("login",this);
    }

    @RequestMapping("/signup")
    public String signup()
    {
        return view("signup",this);
    }

    @RequestMapping("/success")
    public String success()
    {
        return view("success",this);
    }

    @RequestMapping("/check")
    public String check()
    {
        return view("check",this);
    }

    @RequestMapping("/logout")
    public String logout()
    {
        return view("logout",this);
    }

    @RequestMapping("/metric")
    public String metric() { return view("metric", this); }


    /**
     * prüfe die angaben und versuche den user mit dem server zu validieren
     * @return
     */
    public void checkLogin()
    {
        try
        {
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            currentUser = null;

            // logge den service im endpoint ein
            // ist der Vorgang erfolgreich return token
            String token = userService.login(email,password);

            // requeste alle details des nutzers und speichere sie für die session
            currentUser = userService.getUser(token);
            CurrentUser.getInstance().setUser(currentUser);


            // lade alle kategorien des benutzers
            refreshCategories();

            if (currentUser != null)
            {
                LOGGER.info("user successful for '{}'", email);
                ec.redirect(getNavigation().navigate("user","success"));
            }
            else
            {
                LOGGER.info("user failed for '{}'", email);
                FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_WARN, "Login fehlgeschlagen!","Login fehlgeschlagen!"));
            }
        }
        catch (Exception ex)
        {
            LOGGER.info("Error validating user");
            FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_WARN, "Login fehlgeschlagen!","Login fehlgeschlagen!"));
        }
    }

    public void toLogout()
    {
        try
        {
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            // invalidate the session
            LOGGER.debug("invalidating session for '{}'", email);
            ec.invalidateSession();
            LOGGER.info("logout successful for '{}'", email);
            currentUser=null;
        }
        catch (Exception ex)
        {
            LOGGER.info("toLogout Error");
        }

    }

    public boolean isLoggedIn()
    {
        return currentUser != null;

    }

    public String isLoggedInForwardHome()
    {
        try {
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            if(!isLoggedIn()) {
                ec.redirect(getNavigation().navigate("user", "check"));
            }
        }catch (Exception ex)
        {
            LOGGER.info("isLoggedInForwardHome Error");
        }

        return null;
    }

    /**
     * Aktualisiert die dem benutzer zugepordneten Kategorien
     */
    public void refreshCategories()
    {
        allCategories = new ArrayList<>();

        try{
            if(currentUser != null && currentUser.getToken() != null && currentUser.getToken().length() > 0)
            {
                allCategories = requestService.getUserCategories(currentUser.getToken());
            }
        }catch(Exception ex){
            LOGGER.info("Error Categorie result - " + ex);
        }
    }

    /** Gibt alle hinterlegten Kategroeien zurück
     * */
    public List<Categorie> getAllCategories(){
        return allCategories;
    }


    public ArrayList<SearchMetric> getSearchMetric(){
        MetricDataService metricDataService = new MetricDataService();
        return metricDataService.getSearchMetric();
    }

    public ArrayList<ArtikelMetric> getArtikelMetric(){
        MetricDataService metricDataService = new MetricDataService();
        return metricDataService.getArtikelMetric();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String username) {
        this.email = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setLoggedIn(boolean loggedIn) {
        this.loggedIn = loggedIn;
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User currentUser) {
        this.currentUser = currentUser;
    }
}
