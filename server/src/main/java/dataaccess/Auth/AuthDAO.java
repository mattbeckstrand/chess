package dataaccess.Auth;
import dataaccess.DataAccessException;
import model.*;


public interface AuthDAO {
    void addAuth(AuthData auth) throws DataAccessException;

    AuthData findAuth(String userName) throws DataAccessException;

    AuthData findAuthByToken(String authToken) throws DataAccessException;

    void deleteAuth(String username) throws DataAccessException;

    String generateToken() throws DataAccessException;

    void clear() throws DataAccessException;
}
