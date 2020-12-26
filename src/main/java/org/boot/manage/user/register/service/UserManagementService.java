package org.boot.manage.user.register.service;

import java.util.Map;

import org.boot.manage.user.register.entity.UserDetails;

public interface UserManagementService {
	
	
	public Map<Integer, String> findCountries();
	
	public Map<Integer, String> findStates(Integer countryId);
	
	public Map<Integer, String> findCities(Integer stateId);
	
	public boolean isEmailUnique(String email);
	
	public boolean saveUser(UserDetails user);
	
	public String loginCheck(String email,String password);
	
	public boolean isTempPwdValid(String email,String tempPwd);
	
	public boolean unlockAccount(String email,String newPwd);
	
	public String forgotPassword(String email);
}
