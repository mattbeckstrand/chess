package handlers;

import com.google.gson.Gson;
import dataaccess.Auth.MemoryAuthDAO;
import dataaccess.User.MemoryUserDAO;
import exception.ResponseException;
import model.*;
import spark.Route;
import spark.Request;
import spark.Response;
import service.LoginService;

public class LoginHandler implements Route{
    private final MemoryUserDAO userDAO;
    private final MemoryAuthDAO authDAO;
    public LoginHandler(MemoryAuthDAO authDAO, MemoryUserDAO userDao){
        this.userDAO = userDao;
        this.authDAO = authDAO;
    }
    public Object handle(Request req, Response res) throws ResponseException {
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
            res.status(e.StatusCode());
            return gson.toJson(new ErrorResponse(e.getMessage()));
        }
        catch (Exception e) {
            res.status(500);
            return gson.toJson(new ErrorResponse("Internal server error: " + e.getMessage()));
        }
    }

}
