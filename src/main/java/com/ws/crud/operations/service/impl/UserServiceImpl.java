package com.ws.crud.operations.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Service;

import com.ws.crud.operations.exceptions.ApplicationException;
import com.ws.crud.operations.types.User;
import com.ws.crud.operations.service.UserService;

import org.springframework.transaction.annotation.Transactional;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService{
     
    private static final AtomicLong counter = new AtomicLong();
     
    private static List<User> users;
     
    static{
        users= populateDummyUsers();
    }
 
    public List<User> findAllUsers() {
    	if(users.size()>0)
    		return users;
    	else
    		throw new ApplicationException("1004", "No user is available");
    }
     
    public User findById(long id) {
        for(User user : users){
            if(user.getId() == id){
                return user;
            }
        }
        throw new ApplicationException("1001", "User "+id+" is not existed!");
    }
     
    public User findByName(String name) {
        for(User user : users){
            if(user.getName().equalsIgnoreCase(name)){
                return user;
            }
        }
        throw new ApplicationException("1002", "User "+name+" is not existed!");
    }
     
    public void saveUser(User user) {
        user.setId(counter.incrementAndGet());
        users.add(user);
    }
 
    public void updateUser(User user) {
        int index = users.indexOf(user);
        if(index!=-1)
        	users.set(index, user);
        else
        	throw new ApplicationException("1003", "User "+user.getId()+" is not existed for updation!");
    }
 
    public void deleteUserById(long id) {
        for (Iterator<User> iterator = users.iterator(); iterator.hasNext(); ) {
            User user = iterator.next();
            if (user.getId() == id) {
                iterator.remove();
                return;
            }
        }
        throw new ApplicationException("1005", "User "+id+" is not existed for deletion!");
    }
 
    public boolean isUserExist(long id) {
        return findById(id)!=null;
    }
 
    private static List<User> populateDummyUsers(){
        List<User> users = new ArrayList<User>();
        users.add(new User(counter.incrementAndGet(),"Sam",30, 70000));
        users.add(new User(counter.incrementAndGet(),"Tom",40, 50000));
        users.add(new User(counter.incrementAndGet(),"Jerome",45, 30000));
        users.add(new User(counter.incrementAndGet(),"Silvia",50, 40000));
        return users;
    }
 
    public void deleteAllUsers() {
        users.clear();
    }
 
}
