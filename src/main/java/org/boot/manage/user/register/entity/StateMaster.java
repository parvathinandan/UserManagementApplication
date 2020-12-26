package org.boot.manage.user.register.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "state_names")
@AllArgsConstructor
@NoArgsConstructor
public @Data class StateMaster {

	@Id
	@Column(name = "s_id")
	private Integer stateId;
	@Column(name = "s_names")
	private String stateNames;
	@Column(name = "c_id")
	private Integer countryId;
}
