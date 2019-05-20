package de.htw.saar.frontend.controller;

import de.htw.saar.frontend.TestData.User;
import de.htw.saar.frontend.master.MasterController;
import de.htw.saar.frontend.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import java.io.IOException;
@org.springframework.stereotype.Controller

@ManagedBean(name = "loginBean")
@ViewScoped
public class LoginController extends MasterController{

    UserService userService=new UserService();
    private String username;
    private String password;
    private int userId;

    public void checkLogin(ActionEvent actionEvent) {
        FacesContext fc = FacesContext.getCurrentInstance();
        ExternalContext ec = fc.getExternalContext();
        if(UserService.isUser(username)){
            if(UserService.isPasswordValid(username,password)){
                User u = UserService.findUser(username);
                userId = u.getUserId();

                    try {
                        ec.redirect("/view/login/success.xhtml");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }else{
                fc.addMessage(null,new FacesMessage("Password ist Falsch"));
            }
        }else{
            fc.addMessage(null,new FacesMessage("Username ist Falsch"));
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

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
