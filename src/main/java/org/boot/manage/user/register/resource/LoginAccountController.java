package org.boot.manage.user.register.resource;

import org.boot.manage.user.register.dto.LoginCredentials;
import org.boot.manage.user.register.service.UserManagementServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/login")
@CrossOrigin("*")
public class LoginAccountController {

	private UserManagementServiceImpl userService;
	
	public LoginAccountController(UserManagementServiceImpl userService) {
		this.userService = userService;
	}
	
	@PostMapping(path = "/login", consumes = "application/json")
	@ResponseBody
	public ResponseEntity<String> checkLoginCredentials(@RequestBody LoginCredentials user) {
		String loginCheck = userService.loginCheck(user.getEmail(), user.getPassword());
		return new ResponseEntity<>(loginCheck, HttpStatus.OK);
		}
}
