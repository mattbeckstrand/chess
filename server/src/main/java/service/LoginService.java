package service;

import dataaccess.DataAccessException;
import dataaccess.auth.AuthDAO;
import dataaccess.user.MemoryUserDAO;
import dataaccess.auth.MemoryAuthDAO;
import dataaccess.user.UserDAO;
import exception.ResponseException;
import model.*;
import org.mindrot.jbcrypt.BCrypt;

public class LoginService {
    private final UserDAO userDao;
    private final AuthDAO authDAO;
    private final LoginRequest request;
    private final UserData userData;

    public LoginService(AuthDAO authDAO, UserDAO userDao, LoginRequest loginRequest) throws ResponseException {
        this.userDao = userDao;
        this.authDAO = authDAO;
        this.request = loginRequest;
        try {
            this.userData = userDao.findUser(request.username());
        } catch (DataAccessException e) {
            throw new ResponseException(500, "Database error: " + e.getMessage());
        }

    }

    public void checkUserDataExists() throws ResponseException {
        if(this.userData == null){
            throw new ResponseException(401, "Error: unauthorized data doesn't exist");
        }
    }
    public void checkUserPassword() throws ResponseException{
        String storedPassword = this.userData.getPassword();
        String loginPassword = this.request.password();

        if (!BCrypt.checkpw(loginPassword, storedPassword)) {
            throw new ResponseException(401, "Error: Unauthorized incorrect password");
        }
    }

    public AuthData addAuth() throws ResponseException {
        String authToken = authDAO.generateToken();
        AuthData auth = new AuthData(request.username(), authToken);
        try {
            authDAO.addAuth(auth);
        } catch (DataAccessException e) {
            throw new ResponseException(500, "Database error: " + e.getMessage());
        }
        return auth;
    }
}
