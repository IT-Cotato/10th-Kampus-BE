package com.cotato.kampus.domain.user.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cotato.kampus.domain.user.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByUniqueId(String uniqueId);
}
