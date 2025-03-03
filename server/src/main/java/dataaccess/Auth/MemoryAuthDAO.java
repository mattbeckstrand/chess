package dataaccess.Auth;

import java.util.*;

import model.AuthData;
import model.UserData;

public class MemoryAuthDAO implements AuthDAO {
    final private HashMap<String, Set<String>> userToTokens = new HashMap<>();  // Stores latest token per user
    final private HashMap<String, AuthData> tokenToAuth = new HashMap<>();

    @Override
    public void addAuth(AuthData auth){
        userToTokens.putIfAbsent(auth.username(), new HashSet<>());  // Ensure user has a set of tokens
        userToTokens.get(auth.username()).add(auth.authToken());     // Add new token to user's set
        tokenToAuth.put(auth.authToken(), auth);
    }
    @Override
    public AuthData findAuthByUsername( String username) {
        Set<String> tokens = userToTokens.get(username);
        if (tokens == null || tokens.isEmpty()) return null;
        String latestToken = tokens.iterator().next();
        return tokenToAuth.get(latestToken);
    }

    @Override
    public void deleteAuthByUsername(String username) {
        Set<String> tokens = userToTokens.get(username);
        if (tokens != null) {
            for (String token : tokens) {
                tokenToAuth.remove(token);  // Remove all tokens for this user
            }
            userToTokens.remove(username);
        }
    }

    @Override
    public void deleteAuthByToken(String authToken) {
        AuthData auth = tokenToAuth.remove(authToken);  // Remove from tokenToAuth
        if (auth != null) {
            Set<String> tokens = userToTokens.get(auth.username());
            if (tokens != null) {
                tokens.remove(authToken);  // Remove only this token from the user's set
                if (tokens.isEmpty()) {
                    userToTokens.remove(auth.username());  // Remove the user entry if no tokens left
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
