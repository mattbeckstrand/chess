package dataaccess.User;
import dataaccess.DataAccessException;
import model.UserData;

public interface UserDAO {
    void addUser(UserData user) throws DataAccessException;

    UserData findUser(String userName) throws DataAccessException;

    void deleteUser(String userName) throws DataAccessException;

    void clear() throws DataAccessException;
}
