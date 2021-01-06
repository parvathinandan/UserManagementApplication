package org.boot.manage.user.register.resource;

import org.boot.manage.user.register.dto.ForgotPassword;
import org.boot.manage.user.register.service.EmailService;
import org.boot.manage.user.register.service.UserManagementServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/retrievePassword")
@CrossOrigin("*")
public class ForgotPasswordController {

	private UserManagementServiceImpl userService;
	private EmailService emailService;
	
	public ForgotPasswordController(UserManagementServiceImpl userService ,EmailService emailService) {
		this.userService = userService;
		this.emailService = emailService;
	}
	
	@PostMapping(path = "/retrieve", consumes = "application/json")
	@ResponseBody
	public ResponseEntity<String> retrievePassword(@RequestBody ForgotPassword user) {
		
		boolean emailUnique2 = userService.isEmailUnique(user.getEmail());
		String password = userService.forgotPassword(user.getEmail());
		
		if (!emailUnique2) {
			
			boolean sendMail = emailService.sendMessage(user.getEmail(), "retrieve password", "The password is : "+password);
			
			if(sendMail)
				return new ResponseEntity<>("password successfully send to your mail", HttpStatus.OK);
		}
		return new ResponseEntity<>("Enter corect email", HttpStatus.OK);
		}
}
