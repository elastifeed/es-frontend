package de.htw.saar.frontend.controller;

import de.htw.saar.frontend.master.MasterController;
import de.htw.saar.frontend.model.User;
import de.htw.saar.frontend.service.UserService;
import de.htw.saar.frontend.controller.NavigationController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
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

    public static final String HOME_PAGE_REDIRECT = "/view/home/index.html?faces-redirect=true";
    public static final String LOGIN_PAGE_REDIRECT = "/view/user/user.xhtml?faces-redirect=true";


    private List<User> allUsers;
    private String username;
    private String password;
    private User user;
    private boolean success;

    @PostConstruct
    public void initial(){
    success=false;
    }

    public String back(){
        return LOGIN_PAGE_REDIRECT;

    }

   /* public String deleteUser(User u){

    }
*/
    public String save(){
        FacesContext fc = FacesContext.getCurrentInstance();
        User user=new User(username,password);
        if (UserService.isUser(username)==true){
            fc.addMessage(null,new FacesMessage("Username ist schon Vorhanden!"));
            LOGGER.info("Signup failed for '{}'", username);
            FacesContext.getCurrentInstance().addMessage
                    (null,new FacesMessage(FacesMessage.SEVERITY_WARN, "Signup failed",
                            "Falsche Info"));
            return null;
        }else {
            user.setPassword(password);
            user.setUsername(username);
            UserService.addUser(user);
            success = true;
            return back();
        }
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
