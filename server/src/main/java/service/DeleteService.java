package service;

import dataaccess.Auth.MemoryAuthDAO;
import dataaccess.GameData.MemoryGameDataDao;
import dataaccess.User.MemoryUserDAO;

public class DeleteService {
    private final MemoryAuthDAO authDAO;
    private final MemoryUserDAO userDAO;
    private final MemoryGameDataDao gameDAO;

    public DeleteService(MemoryGameDataDao gameDAO, MemoryAuthDAO authDAO, MemoryUserDAO userDAO){
        this.authDAO = authDAO;
        this.userDAO = userDAO;
        this.gameDAO = gameDAO;
    }

    public void delete(){
        authDAO.clear();
        userDAO.clear();
        gameDAO.clear();
    }
}
