package org.boot.manage.user.register.resource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

import org.boot.manage.user.register.dto.UserDetailsDTO;
import org.boot.manage.user.register.entity.UserDetails;
import org.boot.manage.user.register.service.EmailService;
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
	private EmailService emailService;
	private UserDetails userDetails = new UserDetails();

	public UserRegistrationController(UserManagementServiceImpl userService,EmailService emailService) {
		this.userService = userService;
		this.emailService = emailService;
	}

	@GetMapping("/country")
	public Map<Integer, String> getCountries() {
		return userService.findCountries();
	}

	@GetMapping("/states/{countryId}")
	public Map<Integer, String> getStates(@PathVariable Integer countryId) {
		return userService.findStates(countryId);
	}

	@GetMapping("/cities/{stateId}")
	public Map<Integer, String> getCities(@PathVariable Integer stateId) {
		return userService.findCities(stateId);
	}

	@GetMapping("/verifyEmail/{email}")
	public String checkEmailUnique(@PathVariable String email) {
		if (userService.isEmailUnique(email))
			return "true";
		return "false";
	}
	
	@PostMapping(path = "/register", consumes = "application/json")
	@ResponseBody
	public ResponseEntity<String> save(@RequestBody UserDetailsDTO user) {
		String emailUnique = checkEmailUnique(user.getEmail());
		if (emailUnique.equals("true")) {
			BeanUtils.copyProperties(user, userDetails);
			userService.saveUser(userDetails);
		
			String msg = emailService.sendMail(userDetails);
			
			return new ResponseEntity<>(msg, HttpStatus.OK);
			//return new ResponseEntity<>(userDetails, HttpStatus.OK);
		}
		return new ResponseEntity<>(user.getEmail()+" is already assigned", HttpStatus.OK);
		//return new ResponseEntity<>(userDetails, HttpStatus.OK);
	}

	//@GetMapping("/send/{email}") //don't keep here and not using
	public String sendMail(UserDetails user) {
		
		String to = user.getEmail();
		String subject = "java email sending demo";
		String body = "Demo is successfully completed and what about you ?";
		
		String emailBody = emailService.getUnlockAccEmailBody(user);
		
		//boolean sendMimeMessage = emailService.sendMimeMessage(to, subject, body, emailBody.getBytes());
		boolean sendMimeMessage = emailService.sendMimeMessage(to, subject, body, emailBody);
		/**
		boolean success = userService.sendMail(to, subject, body);
		if(success)
			return "Email is successfully send";
		
		return "sending an email is failed :";
		*/
		
		if(sendMimeMessage)
			return "Email is successfully send to your account "+user.getFirstName()+","
		            +"click on the below link to activate your account";
		else
			return "registered successfully but still your account is not active due to technical issue,"
					+ "we will send an activation link on your registered email";
	}

	//don't keep here and not using
	public String getUnlockAccEmailBody(UserDetails user){
		
		StringBuilder sb = new StringBuilder("");
		BufferedReader br = null;
		 String line ="";
	    File f = new File("MyTestFile.txt");

	    FileReader fr;
		try {
			fr = new FileReader(f);
			br = new BufferedReader(fr);
			line = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}

		
	while(line!=null){
		
		if(line.contains("{FNAME}"))
			line = line.replace("{FNAME}",user.getFirstName());

		if(line.contains("{LNAME}"))
			line = line.replace("{LNAME}",user.getLastName());
		
		if(line.contains("{TEMP-PWD}"))
		     line = line.replace("{TEMP-PWD}", user.getPassword());

		if(line.contains("{EMAIL}"))
		     line = line.replace("{EMAIL}", user.getEmail());

		sb.append(line);

		try {
			line = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
	   }
		
		return sb.toString();
	}
	
}