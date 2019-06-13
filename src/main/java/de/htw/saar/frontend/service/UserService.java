package de.htw.saar.frontend.service;

import de.htw.saar.frontend.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UserService {

    private static List<User> allUsers=new ArrayList<>();

    public UserService() {
     init_test(allUsers);
    }

    public static void init_test(List<User> allUsers)
    {
        User mo=new User("mo123","moh","123"); //Zum Testen
        allUsers.add(mo);
    }

    public static List<User> findAllUsers(){
        // fehlt Methode FindeAllUsers
        return allUsers;      //zum Testen
    }


    public static User findUserByName(String name){
        for (User user:findAllUsers()) {
            if (user.getUsername().equals(name)) {
                return user;
            }
        }
        return null;
    }

  //  public static User findUserById(int id){}

      /*  public static List<User> getAllUsers(){
        }
*/

    public static void addUser(User user){
    // fehlt Methode von Datenbanken zum user hinzufügen

        Random random=new Random();
        boolean available=false;
        do {
            String id =Integer.toString(random.nextInt(5000));
            for (User user1:findAllUsers()) {
                if(user1.getUserId().equals(id))
                    available=true;
                    break; }
        }while (available==true);

        allUsers.add(user);
    }

    public static boolean isPasswordValid(String user,String password){
        User u = findUserByName(user);
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
    public static boolean isUser(String username) {
        boolean valide=false;
        for (User user1 : findAllUsers()) {
            if (user1.getUsername().equals(username)) {
                valide=true;
            }}
            return valide;
        }
}


