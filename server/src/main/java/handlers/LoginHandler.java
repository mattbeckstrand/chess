package handlers;

import com.google.gson.Gson;
import dataaccess.auth.AuthDAO;
import dataaccess.auth.MemoryAuthDAO;
import dataaccess.user.MemoryUserDAO;
import dataaccess.user.UserDAO;
import exception.ResponseException;
import model.*;
import spark.Route;
import spark.Request;
import spark.Response;
import service.LoginService;

public class LoginHandler implements Route{
    private final UserDAO userDAO;
    private final AuthDAO authDAO;
    public LoginHandler(AuthDAO authDAO, UserDAO userDao){
        this.userDAO = userDao;
        this.authDAO = authDAO;
    }
    public Object handle(Request req, Response res) {
        Gson gson = new Gson();
        try {
            LoginRequest loginRequest = gson.fromJson(req.body(), LoginRequest.class);
            LoginService service = new LoginService(authDAO, userDAO, loginRequest);
            service.checkUserDataExists();
            service.checkUserPassword();
            AuthData auth = service.addAuth();
            res.type("application/json");
            res.status(200);
            return gson.toJson(auth);
        } catch (ResponseException e) {
            res.status(e.statusCode());
            return gson.toJson(new ErrorResponse(e.getMessage()));
        }
        catch (Exception e) {
            res.status(500);
            return gson.toJson(new ErrorResponse("Internal server error: " + e.getMessage()));
        }
    }

}
