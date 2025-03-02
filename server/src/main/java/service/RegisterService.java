package service;

import java.util.UUID;
import exception.ResponseException;
import dataaccess.User.MemoryUserDao;
import dataaccess.Auth.MemoryAuthDAO;
import model.RegisterRequest;
import model.UserData;
import model.AuthData;
import passoff.exception.ResponseParseException;

public class RegisterService {
    private final String userName;
    private final String password;
    private final String email;
    private final MemoryUserDao userDao;
    private final MemoryAuthDAO authDAO;
    private final UserData userData;


    public RegisterService(MemoryAuthDAO authDAO, MemoryUserDao userDao, UserData request) {
        this.userName = request.getUsername();
        this.password = request.getPassword();
        this.email = request.getEmail();
        this.userDao = userDao;
        this.authDAO = authDAO;
        this.userData = request;
    }
    public void checkForUser() throws ResponseException {
        UserData user = userDao.findUser(this.userName);
        if(user != null){
            throw new ResponseException(403, "Error: already taken");
        }
    }
    public void addUserData() {
        userDao.addUser(userData);
    }

    public AuthData addAuth() {
        String authToken = authDAO.generateToken();
        return new AuthData(userName, authToken);
    }


}
