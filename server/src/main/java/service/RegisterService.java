package service;

import model.RegisterRequest;

public class RegisterService {
    private String userName;
    private String password;
    private String email;

    public RegisterService(RegisterRequest request) {
        this.userName = request.username();
        this.password = request.password();
        this.email = request.email();

    }


    }
}
