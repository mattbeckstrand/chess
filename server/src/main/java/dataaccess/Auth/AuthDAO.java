package dataaccess.Auth;
import dataaccess.DataAccessException;
import model.*;

public interface AuthDAO {
    AuthData addAuth(AuthData auth) throws DataAccessException;

}
