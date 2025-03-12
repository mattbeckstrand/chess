package service;

import dataaccess.DataAccessException;
import dataaccess.auth.AuthDAO;
import dataaccess.auth.MemoryAuthDAO;
import exception.ResponseException;
import model.AuthData;

public class LogoutService {
    private final AuthDAO authDao;
    private final String authToken;

    public LogoutService(AuthDAO authDAO, String authToken){
        this.authDao = authDAO;
        this.authToken = authToken;
    }

    public void checkAuth() throws ResponseException {
        try {
            AuthData auth = authDao.findAuthByToken(authToken);
            if (auth == null) {
                throw new ResponseException(401, "Error: Unauthorized - invalid token");
            }
        } catch (DataAccessException e) {
            throw new ResponseException(500, "Database error: " + e.getMessage());
        }
    }

    public void deleteAuth() throws ResponseException{
        try {
            authDao.deleteAuthByToken(authToken);
        } catch (DataAccessException e) {
            throw new ResponseException(500, "Database error: " + e.getMessage());
        }
    }
}
