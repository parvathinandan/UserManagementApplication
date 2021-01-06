package org.boot.manage.user.register.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.boot.manage.user.register.entity.CityMaster;
import org.boot.manage.user.register.entity.CountryMaster;
import org.boot.manage.user.register.entity.StateMaster;
import org.boot.manage.user.register.entity.UserDetails;
import org.boot.manage.user.register.repo.CityMasterRepository;
import org.boot.manage.user.register.repo.CountryMasterRepository;
import org.boot.manage.user.register.repo.StateMasterRepository;
import org.boot.manage.user.register.repo.UserDetailsReposirory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class UserManagementServiceImpl implements UserManagementService {

	private UserDetailsReposirory userDetailsReposirory;
	private CountryMasterRepository countryRepository;
	private StateMasterRepository stateRepository;
	private CityMasterRepository cityRepository;
	@Autowired
	private JavaMailSender mailSender;
	private Optional<UserDetails> optionalUser = Optional.empty();
	private UserDetails user = null;
	
	public UserManagementServiceImpl(UserDetailsReposirory userDetailsReposirory,
									 CountryMasterRepository countryRepository,
									 StateMasterRepository stateRepository,
									 CityMasterRepository cityRepository
			
									) {
		this.userDetailsReposirory = userDetailsReposirory;
		this.countryRepository = countryRepository;
		this.stateRepository = stateRepository;
		this.cityRepository = cityRepository;
	}

	@Override
	public Map<Integer, String> findCountries() {
		List<CountryMaster> countryList = countryRepository.findAll();
		HashMap<Integer, String> countryMap = new HashMap<>();
		countryList.forEach(country -> countryMap.put(country.getCountryId(), country.getCountryNames()));
		return countryMap;
	}

	@Override
	public Map<Integer, String> findStates(Integer countryId) {
		List<StateMaster> stateList = stateRepository.findByCountryId(countryId);
		HashMap<Integer, String> statesMap = new HashMap<>();
		stateList.forEach(state -> statesMap.put(state.getStateId(),state.getStateNames()));
		return statesMap;
	}

	@Override
	public Map<Integer, String> findCities(Integer stateId) {
		List<CityMaster> cityList = cityRepository.findByStateId(stateId);
		HashMap<Integer, String> citiesMap = new HashMap<>();
		cityList.forEach(city -> citiesMap.put(city.getCityId(),city.getCityNames()));
		return citiesMap;
	}

	@Override
	public boolean isEmailUnique(String email) {
		optionalUser = userDetailsReposirory.findByEmail(email);
		return !optionalUser.isPresent();
	}

	@Override
	public boolean saveUser(UserDetails userData) {
		userData.setAccountStatus("locked");
		userData.setPassword(UUID.randomUUID().toString());
		user = userDetailsReposirory.save(userData);
		return user.getUsId() != null;
	}

	@Override
	public String loginCheck(String email, String password) {
		optionalUser = userDetailsReposirory.findByEmailAndPassword(email, password);
		if(!optionalUser.isEmpty()) {
			user = optionalUser.get();
			String accountStatus = user.getAccountStatus();
			if(accountStatus.equalsIgnoreCase("locked")) 
				return"The account is currently locked";
			else
				return "Valid Credentials";
		}
		return "Not valid credentials";
	}

	@Override
	public boolean isTempPwdValid(String email, String tempPwd) {
		optionalUser = userDetailsReposirory.findByEmail(email);
		if(optionalUser.isPresent()) {
			user = optionalUser.get(); 
			if(user.getPassword().equals(tempPwd))
				return true;
		}
		return false;
	}

	@Override
	public boolean unlockAccount(String email, String newPwd) {
		optionalUser = userDetailsReposirory.findByEmail(email);
		if(optionalUser.isPresent()) 
			user = optionalUser.get();
		user.setAccountStatus("unlocked");
		user.setPassword(newPwd);
		try {
			userDetailsReposirory.save(user);
			return true;
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	        		
	}

	@Override
	public String forgotPassword(String email) {
		optionalUser = userDetailsReposirory.findByEmail(email);
		if(!optionalUser.isEmpty()) {
			user = optionalUser.get();
			return user.getPassword();
		}
		return "Invalid Email :"+email;
	}
	
	@Override
	public boolean sendMail(String email,String subject,String body) {
		boolean success = false;
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setTo(email);
			message.setSubject(subject);
			message.setText(body);
			mailSender.send(message);
			success = true;
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return success;
	}
	
	public void getByEmail(String email) {
		optionalUser = userDetailsReposirory.findByEmail(email);
		if(optionalUser.isPresent()) {
			UserDetails userDetails = optionalUser.get();
			if(userDetails.getAccountStatus().equalsIgnoreCase("unlocked"))
				System.out.println("account is already unlocked........");
			else
				System.out.println("account is locked and redirect to unlock form page.....");
		}
	}
	
	public String unLockByEmail(String email) {
		optionalUser = userDetailsReposirory.findByEmail(email);
		user = optionalUser.get();
		user.setAccountStatus("unlocked");
		return "your account is activated";
	}
}
