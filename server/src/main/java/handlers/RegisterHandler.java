package handlers;

import com.google.gson.Gson;
import dataaccess.User.MemoryUserDAO;
import dataaccess.Auth.MemoryAuthDAO;
import exception.ResponseException;
import model.*;
import service.RegisterService;
import spark.Route;
import spark.Request;
import spark.Response;

public class RegisterHandler implements Route{
    private final MemoryUserDAO userDao;
    private final MemoryAuthDAO authDAO;

    public RegisterHandler(MemoryAuthDAO authDAO, MemoryUserDAO userDao){
        this.userDao = userDao;
        this.authDAO = authDAO;
    }

    @Override
    public Object handle(Request req, Response res) throws ResponseException {
        Gson gson = new Gson();
        try {
            UserData userData = gson.fromJson(req.body(), UserData.class);
            RegisterService service = new RegisterService(authDAO, userDao, userData);
            service.checkForUser();
            service.addUserData();
            AuthData authData = service.addAuth();

            res.type("application/json");
            res.status(200);
            Gson serializer = new Gson();
            return serializer.toJson(authData);
        } catch (ResponseException e) {
            res.status(400);
            return gson.toJson(new ErrorResponse("error: " + e.getMessage()));
        } catch (Exception e) {
            res.status(500);
            return gson.toJson(new ErrorResponse("internal server error"));
        }
    }
}
