package org.boot.manage.user.register.resource;

import org.boot.manage.user.register.dto.UserDetailsDTO;
import org.boot.manage.user.register.entity.UserDetails;
import org.boot.manage.user.register.service.UserManagementServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserRegistrationController {

	private UserManagementServiceImpl userService;
	private UserDetails userDetails = new UserDetails();

	public UserRegistrationController(UserManagementServiceImpl userService) {
		this.userService = userService;
	}
	//login checking
	@PostMapping(path = "/login", consumes = "application/json")
	public ResponseEntity<String> checkLoginCredentials(@RequestBody UserDetailsDTO user) {
		String loginCheck = checkLoginCredentials(user.getEmail(), user.getPassword());
		return new ResponseEntity<>(loginCheck, HttpStatus.OK);
	}
	
	private String checkLoginCredentials(String email,String password) {
		return userService.loginCheck(email,password);
	}

	//user registration
	@PostMapping(path = "/register", consumes = "application/json")
	public ResponseEntity<String> save(@RequestBody UserDetailsDTO user) {
		boolean emailUnique = checkEmailUnique(user.getEmail());
		if(emailUnique == true) {
			BeanUtils.copyProperties(user, userDetails);
			userService.saveUser(userDetails);
			String msg = "registration successful with name :" + userDetails.getFirstName();
			return new ResponseEntity<>(msg, HttpStatus.OK);
		}
		return new ResponseEntity<>(user.getEmail()+" is already assigned", HttpStatus.OK);
	}
	
	private boolean checkEmailUnique(String email) {
		return userService.isEmailUnique(email);
	}
	
	//retrieve forgot password
	@PostMapping(path = "/retrieve", consumes = "application/json")
	public ResponseEntity<String> retrievePassword(@RequestBody UserDetailsDTO user) {
		boolean emailUnique = checkEmailUnique(user.getEmail());
		String msg = userService.forgotPassword(user.getEmail());
		if(emailUnique == true) {
			return new ResponseEntity<>(msg, HttpStatus.OK);
		}
		return new ResponseEntity<>(msg, HttpStatus.OK);
	}

}