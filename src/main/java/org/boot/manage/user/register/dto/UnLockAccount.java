package org.boot.manage.user.register.dto;

import lombok.Data;

@Data
public class UnLockAccount {

	private String email;
	private String password;
	private String newPassword;
}
