package service;

import dataaccess.auth.MemoryAuthDAO;
import exception.ResponseException;
import model.AuthData;

public class LogoutService {
    private final MemoryAuthDAO authDao;
    private final String authToken;

    public LogoutService(MemoryAuthDAO authDAO, String authToken){
        this.authDao = authDAO;
        this.authToken = authToken;
    }
    public void checkAuth() throws ResponseException {
        AuthData auth = authDao.findAuthByToken(authToken);

        if (auth == null) {
            throw new ResponseException(401, "Error: unauthorized check auth null");
        }
    }
    public void deleteAuth(){
        authDao.deleteAuthByToken(authToken);

    }
}
