package org.boot.manage.user.register.repo;

import java.io.Serializable;
import java.util.List;

import org.boot.manage.user.register.entity.CityMaster;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityMasterRepository extends JpaRepository<CityMaster, Serializable>{

	List<CityMaster> findByStateId(Integer stateId);
}
