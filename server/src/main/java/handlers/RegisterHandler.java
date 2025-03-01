package handlers;

import com.google.gson.Gson;
import model.*;
import service.RegisterService;
import spark.Route;
import spark.Request;
import spark.Response;

public class RegisterHandler implements Route{
    @Override
    public Object handle(Request req, Response res){
        Gson gson = new Gson();

        RegisterRequest request = gson.fromJson(req.body(), RegisterRequest.class);
        RegisterService service = new RegisterService(request);
        RegisterResponse response = service.register(request);
    }
}
