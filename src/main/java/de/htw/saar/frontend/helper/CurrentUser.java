package de.htw.saar.frontend.helper;

import de.htw.saar.frontend.model.User;

import java.net.UnknownServiceException;

public class CurrentUser {

    private User user;

    private boolean isAktiv = true;

    private static CurrentUser instance;

    private CurrentUser(){}

    public static CurrentUser getInstance(){
        if(CurrentUser.instance == null){
            CurrentUser.instance = new CurrentUser();
        }
        return CurrentUser.instance;
    }

    public void setUser(User user)
    {
        this.user = user;
    }

    public String getUserIndex()
    {
        if(isAktiv){
            return  "user-" + user.getId();
        } else {
           return "dummy_new";
        }
    }

    public User getUser()
    {
        return this.user;
    }

}
