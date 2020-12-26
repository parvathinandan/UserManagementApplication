package org.boot.manage.user.register.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "country_names")
@AllArgsConstructor
@NoArgsConstructor
public @Data class CountryMaster {

	@Id
	@Column(name = "c_id")
	private Integer countryId;
	@Column(name = "c_names")
	private String countryNames;
}
