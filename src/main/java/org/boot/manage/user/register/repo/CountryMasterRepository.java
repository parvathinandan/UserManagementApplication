package org.boot.manage.user.register.repo;

import java.io.Serializable;

import org.boot.manage.user.register.entity.CountryMaster;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryMasterRepository extends JpaRepository<CountryMaster, Serializable>{

	
}
