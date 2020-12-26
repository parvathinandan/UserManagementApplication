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
import org.springframework.stereotype.Service;

@Service
public class UserManagementServiceImpl implements UserManagementService {

	private UserDetailsReposirory userDetailsReposirory;
	private CountryMasterRepository countryRepository;
	private StateMasterRepository stateRepository;
	private CityMasterRepository cityRepository;
	private HashMap<Integer, String> hashMap = new HashMap<>();
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
		countryList.forEach(country -> hashMap.put(country.getCountryId(), country.getCountryNames()));
		return hashMap;
	}

	@Override
	public Map<Integer, String> findStates(Integer countryId) {
		List<StateMaster> stateList = stateRepository.findByCountryId(countryId);
		stateList.forEach(state -> hashMap.put(state.getCountryId(), state.getStateNames()));
		return hashMap;
	}

	@Override
	public Map<Integer, String> findCities(Integer stateId) {
		List<CityMaster> cityList = cityRepository.findByStateId(stateId);
		cityList.forEach(city -> hashMap.put(city.getStateId(), city.getCityNames()));
		return hashMap;
	}

	@Override
	public boolean isEmailUnique(String email) {
		optionalUser = userDetailsReposirory.findByEmail(email);
		return optionalUser.isPresent();
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
			return "your account password is :"+user.getPassword();
		}
		return "Invalid Email :"+email;
	}

}
