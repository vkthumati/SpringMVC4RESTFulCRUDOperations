package com.ws.crud.operations.controller.soap;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import com.ws.crud.operations.service.UserService;
import com.ws.crud.operations.types.CreateUserRequest;
import com.ws.crud.operations.types.CreateUserResponse;
import com.ws.crud.operations.types.DeleteAllUsersRequest;
import com.ws.crud.operations.types.DeleteAllUsersResponse;
import com.ws.crud.operations.types.DeleteUserRequest;
import com.ws.crud.operations.types.DeleteUserResponse;
import com.ws.crud.operations.types.GetAllUsersRequest;
import com.ws.crud.operations.types.GetAllUsersResponse;
import com.ws.crud.operations.types.GetUserRequest;
import com.ws.crud.operations.types.GetUserResponse;
import com.ws.crud.operations.types.UpdateUserRequest;
import com.ws.crud.operations.types.UpdateUserResponse;
import com.ws.crud.operations.types.User;
import com.ws.crud.operations.types.UserExistRequest;
import com.ws.crud.operations.types.UserExistResponse;

@Endpoint
public class SpringSoapServiceController {
	private static final String NAMESPACE_URI = "http://crud.ws.com/operations/types";
	
	@Autowired
    UserService userService;  
     
	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "getUserRequest")
	@ResponsePayload
	public GetUserResponse getUser(@RequestPayload GetUserRequest request){
		User user = userService.findById(request.getId());
		GetUserResponse response = new GetUserResponse();
		response.setUser(user);
		return response;
	}
	
	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "getAllUsersRequest")
	@ResponsePayload
	public GetAllUsersResponse getAllUsers(@RequestPayload GetAllUsersRequest request) {
        List<User> users = userService.findAllUsers();
        GetAllUsersResponse response = new GetAllUsersResponse();
        response.getUsers().addAll(users);
        return response;
    }
    
	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "createUserRequest")
	@ResponsePayload
	public CreateUserResponse createUser(@RequestPayload CreateUserRequest request) {
        userService.saveUser(request.getUser());
        return new CreateUserResponse();
    }

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "updateUserRequest")
	@ResponsePayload
	public UpdateUserResponse updateUser(@RequestPayload UpdateUserRequest request) {
        userService.updateUser(request.getUser());
        return new UpdateUserResponse();
    }
	
	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "userExistRequest")
	@ResponsePayload
	public UserExistResponse isUserExist(@RequestPayload UserExistRequest request) {
        boolean userExist = userService.isUserExist(request.getId());
        UserExistResponse response = new UserExistResponse();
        response.setIsUserExist(userExist);
        return response;
    }
	
	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteUserRequest")
	@ResponsePayload
	public DeleteUserResponse deleteUser(@RequestPayload DeleteUserRequest request) {
        userService.deleteUserById(request.getId());
        return new DeleteUserResponse();
    }
	
	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteAllUsersRequest")
	@ResponsePayload
	public DeleteAllUsersResponse deleteAllUsers(@RequestPayload DeleteAllUsersRequest request) {
        userService.deleteAllUsers();
        return new DeleteAllUsersResponse();
    }
}
