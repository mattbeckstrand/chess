package dataaccess.auth;

import java.util.*;

import model.AuthData;

public class MemoryAuthDAO implements AuthDAO {
    final private HashMap<String, Set<String>> userToTokens = new HashMap<>();
    final private HashMap<String, AuthData> tokenToAuth = new HashMap<>();

    @Override
    public void addAuth(AuthData auth){
        userToTokens.putIfAbsent(auth.username(), new HashSet<>());
        userToTokens.get(auth.username()).add(auth.authToken());
        tokenToAuth.put(auth.authToken(), auth);
    }

    @Override
    public void deleteAuthByToken(String authToken) {
        AuthData auth = tokenToAuth.remove(authToken);
        if (auth != null) {
            Set<String> tokens = userToTokens.get(auth.username());
            if (tokens != null) {
                tokens.remove(authToken);
                if (tokens.isEmpty()) {
                    userToTokens.remove(auth.username());
                }
            }
        }
    }

    @Override
    public AuthData findAuthByToken(String authToken){
        AuthData authData = tokenToAuth.get(authToken);
        if (authData == null) {
            System.out.println("Auth Token Not Found: " + authToken);
            return null;
        } return authData;

    }
    @Override
    public String generateToken(){
        return UUID.randomUUID().toString();
    }
    @Override
    public void clear() {
        tokenToAuth.clear();
        userToTokens.clear();
    }
    @Override
    public boolean isEmpty() {
        return (tokenToAuth.isEmpty() && userToTokens.isEmpty());
    }


}
