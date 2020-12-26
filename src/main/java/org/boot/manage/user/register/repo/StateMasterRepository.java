package org.boot.manage.user.register.repo;

import java.io.Serializable;
import java.util.List;

import org.boot.manage.user.register.entity.StateMaster;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StateMasterRepository extends JpaRepository<StateMaster, Serializable>{

	
	List<StateMaster> findByCountryId(Integer countryId);
}
