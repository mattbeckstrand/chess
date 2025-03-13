package service;

import dataaccess.DataAccessException;
import dataaccess.auth.AuthDAO;
import dataaccess.user.UserDAO;
import exception.ResponseException;
import dataaccess.user.MemoryUserDAO;
import dataaccess.auth.MemoryAuthDAO;
import model.UserData;
import model.AuthData;

public class RegisterService {
    private final String userName;
    private final String password;
    private final String email;
    private final UserDAO userDao;
    private final AuthDAO authDAO;
    private final UserData userData;


    public RegisterService(AuthDAO authDAO, UserDAO userDao, UserData request) {
        this.userName = request.getUsername();
        this.password = request.getPassword();
        this.email = request.getEmail();
        this.userDao = userDao;
        this.authDAO = authDAO;
        this.userData = request;
    }
    public void checkForUser() throws ResponseException, DataAccessException {
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
    public void addUserData() throws ResponseException, DataAccessException {
        checkForUser();
        System.out.println("Attempting to add user: " + userData.getUsername());
        userDao.addUser(userData);
    }

    public AuthData addAuth() throws ResponseException {
        String authToken = authDAO.generateToken();
        AuthData auth = new AuthData(userName, authToken);

        try {
            authDAO.addAuth(auth);
        } catch (DataAccessException e) {
            throw new ResponseException(500, "Database error: " + e.getMessage());
        }

        return auth;
    }


}
