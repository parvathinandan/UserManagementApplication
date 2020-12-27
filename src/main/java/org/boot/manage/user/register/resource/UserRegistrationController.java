package org.boot.manage.user.register.resource;

import java.util.Map;

import org.boot.manage.user.register.dto.UserDetailsDTO;
import org.boot.manage.user.register.entity.UserDetails;
import org.boot.manage.user.register.service.UserManagementServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@CrossOrigin("*")
public class UserRegistrationController {

	private UserManagementServiceImpl userService;
	private UserDetails userDetails = new UserDetails();

	public UserRegistrationController(UserManagementServiceImpl userService) {
		this.userService = userService;
	}
	
	@GetMapping("/country")
	public Map<Integer, String> getCountries() {
		
		return userService.findCountries();
	}
	@GetMapping("/states/{countryId}")
	public Map<Integer, String> getStates( @PathVariable Integer countryId) {
		
		return userService.findStates(countryId);
	}

	@GetMapping("/states/{stateId}")
	public Map<Integer, String> getCities(@PathVariable Integer stateId) {
	
	return userService.findCities(stateId);
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
	@ResponseBody
	public ResponseEntity<UserDetails> save(@RequestBody UserDetailsDTO user) {
		String emailUnique = checkEmailUnique(user.getEmail());
		if(emailUnique.equals("true")) {
			BeanUtils.copyProperties(user, userDetails);
			userService.saveUser(userDetails);
			String msg = "registration successful with name :" + userDetails.getFirstName();
			//return new ResponseEntity<>(msg, HttpStatus.OK);
			return new ResponseEntity<>(userDetails, HttpStatus.OK);
		}
		//return new ResponseEntity<>(user.getEmail()+" is already assigned", HttpStatus.OK);
		return new ResponseEntity<>(userDetails, HttpStatus.OK);
	}
	@GetMapping("/verifyEmail")
	public String checkEmailUnique(String email) {	
		if(userService.isEmailUnique(email))
			return "true";
		return "false";
	}
	
	//retrieve forgot password
	@PostMapping(path = "/retrieve", consumes = "application/json")
	public ResponseEntity<String> retrievePassword(@RequestBody UserDetailsDTO user) {
		String emailUnique = checkEmailUnique(user.getEmail());
		String msg = userService.forgotPassword(user.getEmail());
		if(emailUnique.equals("true")) {
			return new ResponseEntity<>(msg, HttpStatus.OK);
		}
		return new ResponseEntity<>("Enter corect email", HttpStatus.NO_CONTENT);
	}

}