package org.boot.manage.user.register.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "USER_MASTER")
@AllArgsConstructor
@NoArgsConstructor
public @Data class UserDetails implements Serializable{

	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue
	private Integer usId;
	private String firstName;
	private String lastName;
	private String email;
	private Long phoneNumber;
	private Date dateOfBirth;
	private Character gender;
	private String countryName;
	private String state;
	private String city;
	private String password;
	private String accountStatus;
}
