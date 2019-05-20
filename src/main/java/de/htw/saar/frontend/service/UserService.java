package de.htw.saar.frontend.service;

import de.htw.saar.frontend.TestData.User;

public class UserService {

    public UserService() {}

    public static User findUser(String name){
        User mo=new User(1,"mo","123"); //Zum Testen
        return mo;
    }

  //  public static User findUserById(int id){}


    public static boolean isPasswordValid(String user,String password){
        User u = findUser(user);
        try {
            if (u.getPassword().equals(password)) {
                return true;
            }
        }catch (NullPointerException e){
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Check if this User exist in Datenbank
     * */
    public static boolean isUser(String tocheck){
        return true;                // Check fehlt
     }

}
