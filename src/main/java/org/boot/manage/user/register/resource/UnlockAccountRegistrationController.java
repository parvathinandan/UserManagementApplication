package org.boot.manage.user.register.resource;

import org.boot.manage.user.register.dto.UserDetailsDTO;
import org.boot.manage.user.register.service.UserManagementServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/activateAccount")
public class UnlockAccountRegistrationController {

	private UserManagementServiceImpl userService ;
	
	public UnlockAccountRegistrationController(UserManagementServiceImpl userService) {
		this.userService = userService;
	}
	//implemetation completed
	@PostMapping("/unlock")//take dto object and unlock account
	public ResponseEntity<String> unlockAccount(@RequestBody UserDetailsDTO user) {
		boolean b = checkUnlockAccount(user.getEmail(), user.getNewPassword(), user.getPassword());
		if(b == true)
			return new ResponseEntity<>("unlock is successful",HttpStatus.OK);
		String msg = "please enter correct temporary password that was send to your e-mail";
		return new ResponseEntity<>(msg,HttpStatus.OK);
	}
	
	private boolean checkUnlockAccount(String email,String newPassword,String tempPwd) {
		boolean tempPwdValid = userService.isTempPwdValid(email, tempPwd);
		if(tempPwdValid == true) {
			return userService.unlockAccount(email, newPassword);
		}
		return tempPwdValid;
	}
}
