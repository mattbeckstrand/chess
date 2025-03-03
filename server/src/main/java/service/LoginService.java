package service;

import dataaccess.User.MemoryUserDAO;
import dataaccess.Auth.MemoryAuthDAO;
import exception.ResponseException;
import model.*;
import exception.ResponseException;

public class LoginService {
    private final MemoryUserDAO userDao;
    private final MemoryAuthDAO authDAO;
    private final LoginRequest request;
    private final UserData userData;

    public LoginService(MemoryAuthDAO authDAO, MemoryUserDAO userDao, LoginRequest loginRequest){
        this.userDao = userDao;
        this.authDAO = authDAO;
        this.request = loginRequest;
        this.userData = userDao.findUser(request.username());
    }

    public void checkUserDataExists() throws ResponseException {
        if(this.userData == null){
            throw new ResponseException(401, "Error: unauthorized");
        }
    }
    public void checkUserPassword() throws ResponseException{
        String storedPassword = this.userData.getPassword();
        String loginPassword = this.request.password();
        if(!storedPassword.equals(loginPassword)){
            throw new ResponseException(401, "Error: unauthorized");
        }
    }

    public AuthData addAuth() throws ResponseException{
        authDAO.deleteAuth(request.username());
        String authToken = authDAO.generateToken();
        String username = request.username();
        AuthData auth = new AuthData(username, authToken);
        authDAO.addAuth(auth);
        return auth;
    }
}
