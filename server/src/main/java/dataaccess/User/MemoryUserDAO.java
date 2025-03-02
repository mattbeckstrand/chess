package dataaccess.User;
import model.*;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO{
    final private HashMap<String, UserData> userData = new HashMap<>();

    public void addUser(UserData user){
        userData.put(user.getUsername(), user);
    }

    public UserData findUser( String username) {
        return userData.get(username);
    }

    public void deleteUser(String username){
        userData.remove(username);
    }
}
