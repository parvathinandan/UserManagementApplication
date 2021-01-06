package org.boot.manage.user.register.dto;

import java.util.Date;

import lombok.Data;

public @Data class UserDetailsDTO {


	private String firstName;
	private String lastName;
	private String email;
	private Long phoneNumber;
	private Date dateOfBirth;
	private Character gender;
	private String countryName;
	private String state;
	private String city;

}
