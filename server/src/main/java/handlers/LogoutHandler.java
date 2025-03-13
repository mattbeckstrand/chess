package handlers;


import dataaccess.auth.AuthDAO;
import dataaccess.auth.MemoryAuthDAO;

import exception.ResponseException;
import model.ErrorResponse;
import service.LogoutService;
import spark.Route;
import spark.Request;
import spark.Response;
import com.google.gson.Gson;

public class LogoutHandler implements Route {
    private final AuthDAO authDAO;

    public LogoutHandler(AuthDAO authDAO){
        this.authDAO = authDAO;
    }
    @Override
    public Object handle(Request req, Response res) {
        String authToken = req.headers("Authorization");
        Gson gson = new Gson();
        try {
            LogoutService service = new LogoutService(authDAO, authToken);
            service.checkAuth();
            service.deleteAuth();
            res.status(200);
            return "{}";
        } catch (ResponseException e){
            res.status(e.statusCode());
            return gson.toJson(new ErrorResponse(e.getMessage()));
        } catch (Exception e){
            res.status(500);
            return gson.toJson( new ErrorResponse("Error: " + e.getMessage()));
        }
    }
}
