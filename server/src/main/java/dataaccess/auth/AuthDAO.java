package dataaccess.auth;
import dataaccess.DataAccessException;
import model.*;


public interface AuthDAO{
    void addAuth(AuthData auth) throws DataAccessException;

    AuthData findAuthByToken(String authToken) throws DataAccessException;

    void deleteAuthByToken(String username) throws DataAccessException;

    String generateToken();

    void clear() throws DataAccessException;

    boolean isEmpty() throws DataAccessException;
}
