package de.htw.saar.frontend.controller;

import de.htw.saar.frontend.master.MasterController;
import de.htw.saar.frontend.model.User;
import de.htw.saar.frontend.service.UserService;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import java.io.IOException;
import java.io.Serializable;
import javax.faces.bean.SessionScoped;
import javax.inject.Named;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;

@org.springframework.stereotype.Controller

@Named
@RequestMapping("/user")
public class UserController extends MasterController implements Serializable
{
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    public  final String HOME_PAGE_REDIRECT = view("index","home");
    public  final String LOGIN_PAGE_REDIRECT = view("login",this);


    UserService userService = new UserService();
    private String username;
    private String password;
    private String userId;
    private boolean loggedIn=isLoggedIn();

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

    public String checkLogin()
    {
        try
        {
            FacesContext fc = FacesContext.getCurrentInstance();
            ExternalContext ec = fc.getExternalContext();
            currentUser=null;

            if(UserService.isUser(username))
            {
                if(UserService.isPasswordValid(username,password))
                {
                    User u = UserService.findUserByName(username);
                    userId = u.getUserId();
                    currentUser=u;
                }
                else
                {
                    fc.addMessage(null,new FacesMessage("Password ist Falsch"));
                }
            }
            else
            {
                fc.addMessage(null,new FacesMessage("Username ist Falsch"));
            }
            if (currentUser != null)
            {
                LOGGER.info("user successful for '{}'", username);
                ec.redirect(getNavigation().navigate("user","success"));
                return null;
            }
            else
            {
                LOGGER.info("user failed for '{}'", userId);
                FacesContext.getCurrentInstance().addMessage(null,new FacesMessage(FacesMessage.SEVERITY_WARN, "Login failed","Falsche Info"));
                return null;
            }
        }
        catch (Exception ex)
        {
            LOGGER.info("Error validating user");
            return "";
        }
    }

    public void toLogout()
    {
        try
        {
        String identifier = userId;
        // invalidate the session
        LOGGER.debug("invalidating session for '{}'", identifier);
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        LOGGER.info("logout successful for '{}'", identifier);
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

    public String isLoggedInForwardHome() {
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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