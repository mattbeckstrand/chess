package dataaccess.User;
import model.*;

import java.util.HashMap;

public class MemoryUserDAO implements UserDAO{
    final private HashMap<String, UserData> userData = new HashMap<>();

    @Override
    public void addUser(UserData user){
        userData.put(user.getUsername(), user);
    }

    @Override
    public UserData findUser( String username) {
        return userData.get(username);
    }

    @Override
    public void deleteUser(String username){
        userData.remove(username);
    }

    @Override
    public void clear() {
        userData.clear();
    }

    @Override
    public boolean isEmpty() {
        return userData.isEmpty();
    }

}
