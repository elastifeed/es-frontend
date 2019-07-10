package de.htw.saar.frontend.service;

import de.htw.saar.frontend.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UserService
{
    RequestService requestService = new RequestService();

    public String login(String email, String password) throws Exception
    {
        return requestService.userLogin(email, password);
    }

    public String register(String email, String password,String name) throws Exception
    {
        return requestService.userRegister(email,password,name);
    }

    public User getUser(String token) throws Exception
    {
        return requestService.getUser(token);
    }
}


