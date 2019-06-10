package de.htw.saar.frontend.controller;

import de.htw.saar.frontend.model.User;
import de.htw.saar.frontend.service.UserService;
import de.htw.saar.frontend.controller.NavigationController;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@ManagedBean(name = "userC")
@ViewScoped
public class SignupController {

    NavigationController naviController=new NavigationController();


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
        return naviController.toLogin();
    }

   /* public String deleteUser(User u){

    }
*/
    public String save(){
        User user=new User(username,password);
        user.setPassword(password);
        user.setUsername(username);
        UserService.addUser(user);
        success=true;
       return naviController.toLogin();
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
