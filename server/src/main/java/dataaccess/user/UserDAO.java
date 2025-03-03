package dataaccess.user;
import dataaccess.DataAccessException;
import model.UserData;

public interface UserDAO {
    void addUser(UserData user) throws DataAccessException;

    UserData findUser(String userName) throws DataAccessException;


    void clear() throws DataAccessException;

    boolean isEmpty() throws DataAccessException;

}
