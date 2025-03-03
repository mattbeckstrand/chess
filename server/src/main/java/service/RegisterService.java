package service;

import exception.ResponseException;
import dataaccess.User.MemoryUserDAO;
import dataaccess.Auth.MemoryAuthDAO;
import model.UserData;
import model.AuthData;

public class RegisterService {
    private final String userName;
    private final String password;
    private final String email;
    private final MemoryUserDAO userDao;
    private final MemoryAuthDAO authDAO;
    private final UserData userData;


    public RegisterService(MemoryAuthDAO authDAO, MemoryUserDAO userDao, UserData request) {
        this.userName = request.getUsername();
        this.password = request.getPassword();
        this.email = request.getEmail();
        this.userDao = userDao;
        this.authDAO = authDAO;
        this.userData = request;
    }
    public void checkForUser() throws ResponseException {
        UserData user = userDao.findUser(this.userName);
        if (this.userName == null || this.userName.isBlank() ||
                this.password == null || this.password.isBlank() ||
                this.email == null || this.email.isBlank()) {
            throw new ResponseException(400, "Error: bad request");
        }
        if(user != null){
            throw new ResponseException(403, "Error: already taken");
        }
    }
    public void addUserData() throws ResponseException{
        checkForUser();
        userDao.addUser(userData);
    }

    public AuthData addAuth() {
        String authToken = authDAO.generateToken();
        AuthData auth = new AuthData(userName, authToken);
        authDAO.addAuth(auth);
        return auth;
    }


}
