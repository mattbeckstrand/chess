package handlers;

import com.google.gson.Gson;
import dataaccess.*;
import dataaccess.auth.AuthDAO;
import dataaccess.user.UserDAO;
import exception.ResponseException;
import model.*;
import service.RegisterService;
import spark.Route;
import spark.Request;
import spark.Response;

public class RegisterHandler implements Route{
    private final UserDAO userDao;
    private final AuthDAO authDAO;

    public RegisterHandler(AuthDAO authDAO, UserDAO userDao){
        this.userDao = userDao;
        this.authDAO = authDAO;
    }

    @Override
    public Object handle(Request req, Response res) {
        Gson gson = new Gson();
        try {
            UserData userData = gson.fromJson(req.body(), UserData.class);
            RegisterService service = new RegisterService(authDAO, userDao, userData);
            service.addUserData();
            AuthData authData = service.addAuth();

            res.type("application/json");
            res.status(200);
            return gson.toJson(authData);
        } catch (ResponseException e) {
            res.status(e.statusCode());
            return gson.toJson(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            res.status(500);
            return gson.toJson(new ErrorResponse("internal server error"));
        }
    }
}
