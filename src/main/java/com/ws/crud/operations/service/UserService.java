package com.ws.crud.operations.service;

import java.util.List;

import com.ws.crud.operations.types.User;

public interface UserService {
    
    User findById(long id);
     
    User findByName(String name);
     
    void saveUser(User user);
     
    void updateUser(User user);
     
    void deleteUserById(long id);
 
    List<User> findAllUsers(); 
     
    void deleteAllUsers();
     
    public boolean isUserExist(long id);
     
}
