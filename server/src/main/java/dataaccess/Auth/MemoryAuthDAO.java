package dataaccess.Auth;

import java.util.*;

import model.AuthData;
import model.UserData;

public class MemoryAuthDAO implements AuthDAO {
    final private HashMap<String, AuthData> authData = new HashMap<>();
    @Override
    public void addAuth(AuthData auth){
        authData.put(auth.username(), auth);
    }
    @Override
    public AuthData findAuth( String username) {
        return authData.get(username);
    }
    @Override
    public void deleteAuth(String username){
        authData.remove(username);
    }
    @Override
    public String generateToken(){
        return UUID.randomUUID().toString();
    }
}
