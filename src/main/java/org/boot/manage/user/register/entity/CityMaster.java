package org.boot.manage.user.register.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "city_names")
@AllArgsConstructor
@NoArgsConstructor
public @Data class CityMaster {

	@Id
	@Column(name = "city_id")
	private Integer cityId;
	@Column(name = "c_names")
	private String cityNames;
	@Column(name = "s_id")
	private Integer stateId;
}
