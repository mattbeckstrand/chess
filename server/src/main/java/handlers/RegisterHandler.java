package handlers;

import com.google.gson.Gson;
import dataaccess.User.MemoryUserDao;
import dataaccess.Auth.MemoryAuthDAO;
import exception.ResponseException;
import model.*;
import service.RegisterService;
import spark.Route;
import spark.Request;
import spark.Response;

public class RegisterHandler implements Route{
    private final MemoryUserDao userDao;
    private final MemoryAuthDAO authDAO;

    public RegisterHandler(MemoryAuthDAO authDAO, MemoryUserDao userDao){
        this.userDao = userDao;
        this.authDAO = authDAO;
    }

    @Override
    public Object handle(Request req, Response res) throws ResponseException {
        Gson gson = new Gson();

        UserData userData = gson.fromJson(req.body(), UserData.class);
        RegisterService service = new RegisterService(authDAO, userDao, userData);
        service.checkForUser();
        service.addUserData();
        AuthData authData = service.addAuth();
        var serializer = new Gson();
        return serializer.toJson(authData);
    }
}
