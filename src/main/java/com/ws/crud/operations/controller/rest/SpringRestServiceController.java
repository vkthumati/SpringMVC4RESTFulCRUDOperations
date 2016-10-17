package com.ws.crud.operations.controller.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.ws.crud.operations.service.UserService;
import com.ws.crud.operations.types.User;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;
import io.swagger.annotations.Contact;
import io.swagger.annotations.ExternalDocs;
import io.swagger.annotations.Info;
import io.swagger.annotations.SwaggerDefinition;


@Api(
        basePath = "/api", 
        value = "User CRUD Operations", 
        description = "CRUD Operations on User", 
        produces = "application/json",
        authorizations={@Authorization(
             value="user_auth",
                scopes={@AuthorizationScope(description="User authorization scope", scope="user")})
           }
        
        )
@SwaggerDefinition(
        basePath = "",
        externalDocs = @ExternalDocs(value = "Wiki" , 
            url = "http://localhost:8080/RestCrudOperations/api/application.wadl"),
        host = "localhost",
        info = @Info(
                title = "User CRUD Operations", 
                version = "1.12",
                contact=@Contact(name="User CRUD Operations Support", email="info2vasanth@gmail.com", 
                    url="http://localhost:8080/RestCrudOperations/api/application.wadl"),
                description =  "CRUD Operations on User Service"
                )
        )
@RestController
@RequestMapping(value="/api")
public class SpringRestServiceController {

	@Autowired
    UserService userService; 
    
	@Lazy(false)
	@ApiOperation(nickname = "listAllUsers", value = "List All Users", notes = "This Api retrieves all users.", response = User.class)
	@ApiResponses(value = {
			@ApiResponse(code = 400, message = "Invalid request", response = com.ws.crud.operations.exceptions.ErrorResponse.class),
			@ApiResponse(code = 500, message = "Error processing request", response = com.ws.crud.operations.exceptions.ErrorResponse.class), })
    @RequestMapping(value = "/allUsers", method = RequestMethod.GET)
    public ResponseEntity<List<User>> listAllUsers() {
        List<User> users = userService.findAllUsers();
        if(users.isEmpty()){
            return new ResponseEntity<List<User>>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<List<User>>(users, HttpStatus.OK);
    }
    
    @RequestMapping(value="/getUserById/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUserByIdPathVariable(@PathVariable(required=true, name="id") long id){
    	User user = userService.findById(id);
    	ResponseEntity<User> response = null;
    	if(user==null){
    		System.out.println("GetUserByIdPathVariable - User with id : "+id+" Not Found!");
    		response = new ResponseEntity<User>(HttpStatus.NO_CONTENT);
    	}else{
    		response = new ResponseEntity<User>(user, HttpStatus.OK);
    	}
    	return response;
    }
	
    @RequestMapping(value="/getUserById", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getUserByIdRequestParam(@RequestParam(required=true, name="id") long id){
    	User user = userService.findById(id);
    	ResponseEntity<User> response = null;
    	if(user==null){
    		System.out.println("GetUserByIdRequestParam - User with id : "+id+" Not Found!");
    		response = new ResponseEntity<User>(HttpStatus.NO_CONTENT);
    	}else{
    		response = new ResponseEntity<User>(user, HttpStatus.OK);
    	}	
    	return response;
    }
    
    @RequestMapping(value="/getUserByName", method = RequestMethod.GET, produces = {MediaType.APPLICATION_XML_VALUE,  MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<User> getUserByNameRequestParam(@RequestParam(required=true, name="name") String name){
    	User user = userService.findByName(name);
    	ResponseEntity<User> response = null;
    	if(user==null){
    		System.out.println("GetUserByNameRequestParam - User with name : "+name+" Not Found!");
    		response = new ResponseEntity<User>(HttpStatus.NO_CONTENT);
    	}else{
    		response = new ResponseEntity<User>(user, HttpStatus.OK);
    	}	
    	return response;
    }
     
    @RequestMapping(value = "/createUser", method = RequestMethod.POST)
    public ResponseEntity<Void> createUser(@RequestBody User user,  UriComponentsBuilder ucBuilder) {
        System.out.println("Creating User " + user.getName());
 
        if (userService.isUserExist(user.getId())) {
            System.out.println("A User with name " + user.getName() + " already exist");
            return new ResponseEntity<Void>(HttpStatus.CONFLICT);
        }
 
        userService.saveUser(user);
 
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/createUser/{id}").buildAndExpand(user.getId()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }
     
    @RequestMapping(value = "/updateUser/{id}", method = RequestMethod.PUT)
    public ResponseEntity<User> updateUser(@PathVariable("id") long id, @RequestBody User user) {
        System.out.println("Updating User " + id);
         
        User currentUser = userService.findById(id);
         
        if (currentUser==null) {
            System.out.println("User with id " + id + " not found");
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
 
        currentUser.setName(user.getName());
        currentUser.setAge(user.getAge());
        currentUser.setSalary(user.getSalary());
         
        userService.updateUser(currentUser);
        return new ResponseEntity<User>(currentUser, HttpStatus.OK);
    }
     
    @RequestMapping(value = "/deleteUser/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<User> deleteUser(@PathVariable("id") long id) {
        System.out.println("Fetching & Deleting User with id " + id);
 
        User user = userService.findById(id);
        if (user == null) {
            System.out.println("Unable to delete. User with id " + id + " not found");
            return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
        }
 
        userService.deleteUserById(id);
        return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
    }
     
    @RequestMapping(value = "/deleteAllUsers", method = RequestMethod.DELETE)
    public ResponseEntity<User> deleteAllUsers() {
        System.out.println("Deleting All Users");
 
        userService.deleteAllUsers();
        return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
    }
}
