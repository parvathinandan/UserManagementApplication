package org.boot.manage.user.register.repo;

import java.io.Serializable;
import java.util.Optional;

import org.boot.manage.user.register.entity.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserDetailsReposirory extends JpaRepository<UserDetails, Serializable>{

	Optional<UserDetails> findByEmail(String email);
	Optional<UserDetails> findByEmailAndPassword(String email,String password);
}
