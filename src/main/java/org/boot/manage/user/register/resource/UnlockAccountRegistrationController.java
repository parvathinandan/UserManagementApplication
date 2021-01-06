package org.boot.manage.user.register.resource;

import org.boot.manage.user.register.dto.UnLockAccount;
import org.boot.manage.user.register.service.UserManagementServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/activateAccount")
@CrossOrigin("*")
public class UnlockAccountRegistrationController {

	private UserManagementServiceImpl userService ;
	
	public UnlockAccountRegistrationController(UserManagementServiceImpl userService) {
		this.userService = userService;
	}
	
	@PostMapping("/unlock")//take dto object and unlock account
	public ResponseEntity<String> unlockAccount(@RequestBody UnLockAccount user) {
		boolean unlock = checkUnlockAccount(user.getEmail(), user.getNewPassword(), user.getPassword());
		if(unlock)
			return new ResponseEntity<>("unlock is successful",HttpStatus.OK);
		else {
			String msg = "please enter correct temporary password that was send to your e-mail";
			return new ResponseEntity<>(msg,HttpStatus.OK);
		}
		
	}
	
	private boolean checkUnlockAccount(String email,String newPassword,String tempPwd) {
		boolean tempPwdValid = userService.isTempPwdValid(email, tempPwd);
		if(tempPwdValid) {
			return userService.unlockAccount(email, newPassword);
		}
		else
			return tempPwdValid;
	}
	
	@GetMapping("/unlockAcc/email")
	public ResponseEntity<String> sendToUnlockForm(@RequestParam String email) {
		String unlock = userService.unLockByEmail(email);
		return new ResponseEntity<String>(unlock,HttpStatus.OK);
		
	}
}
