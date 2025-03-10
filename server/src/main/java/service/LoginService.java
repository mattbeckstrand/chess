package service;

import dataaccess.user.MemoryUserDAO;
import dataaccess.auth.MemoryAuthDAO;
import exception.ResponseException;
import model.*;

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
            throw new ResponseException(401, "Error: unauthorized data doesn't exist");
        }
    }
    public void checkUserPassword() throws ResponseException{
        String storedPassword = this.userData.getPassword();
        String loginPassword = this.request.password();

        if(!storedPassword.equals(loginPassword)){
            throw new ResponseException(401, "Error: unauthorized password checked");
        }
        System.out.println("Password checked and good");
    }

    public AuthData addAuth() throws ResponseException{
        String authToken = authDAO.generateToken();
        AuthData auth = new AuthData(request.username(), authToken);
        authDAO.addAuth(auth);
        System.out.println("Auth added here: " + request.username() +  " " + authToken);
        return auth;
    }
}
