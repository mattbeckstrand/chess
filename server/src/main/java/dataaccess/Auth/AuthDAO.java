package dataaccess.Auth;
import dataaccess.DataAccessException;
import model.*;

import javax.xml.crypto.Data;

public interface AuthDAO {
    void addAuth(AuthData auth) throws DataAccessException;

    AuthData findAuth(String userName) throws DataAccessException;

    AuthData findAuthByToken(String authToken) throws DataAccessException;

    void deleteAuth(String username) throws DataAccessException;

    String generateToken() throws DataAccessException;
}
