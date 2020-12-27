package org.boot.manage.user.register.resource;

import org.boot.manage.user.register.dto.UserDetailsDTO;
import org.boot.manage.user.register.service.UserManagementServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/retrievePassword")
public class ForgotPasswordController {

	private UserManagementServiceImpl userService;
	
	public ForgotPasswordController(UserManagementServiceImpl userService) {
		this.userService = userService;
	}
	
	@PostMapping(path = "/retrieve", consumes = "application/json")
	@ResponseBody
	public ResponseEntity<String> retrievePassword(@RequestBody UserDetailsDTO user) {
		boolean emailUnique2 = userService.isEmailUnique(user.getEmail());
		String msg = userService.forgotPassword(user.getEmail());
		if (!emailUnique2) {
			return new ResponseEntity<>(msg, HttpStatus.OK);
		}
		else
			return new ResponseEntity<>("Enter corect email", HttpStatus.OK);
		}
}
