package de.htw.saar.frontend.controller;

import de.htw.saar.frontend.master.MasterController;
import de.htw.saar.frontend.model.User;
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
    private static final Logger LOGGER =
            LoggerFactory.getLogger(UserController.class);

    private List<User> allUsers;
    private String username;
    private String password;
    private User user;
    private boolean success;
    FacesContext fc = FacesContext.getCurrentInstance();
    ExternalContext ec = fc.getExternalContext();

    @PostConstruct
    public void initial(){
    success=false;
    }

    public String back(){
try {
    ec.redirect(getNavigation().navigate("user", "login"));
}catch (Exception ex)
{
    LOGGER.info("Signup Cancel Error");
}
    return null;
    }

   /* public String deleteUser(User u){

    }
*/
    public String save(){
       try {
           User user = new User(username, password);
           if (UserService.isUser(username) == true) {
               fc.addMessage(null, new FacesMessage("Username ist schon Vorhanden!"));
               LOGGER.info("Signup failed for '{}'", username);
               FacesContext.getCurrentInstance().addMessage
                       (null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Signup failed",
                               "Falsche Info"));
           } else {
               user.setPassword(password);
               user.setUsername(username);
               UserService.addUser(user);
               ec.redirect(getNavigation().navigate("user", "success"));
           }
       }
     catch (Exception ex)
           {
               LOGGER.info("Signup Error ");
           }
       return null;
    }



    /* public void findAllUsers(){
        allUsers= UserService.getAllUsers();
    }
*/

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

    public List<User> getAllUsers() {
        return allUsers;
    }

    public void setAllUsers(List<User> allUsers) {
        this.allUsers = allUsers;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
