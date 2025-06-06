package com.cotato.kampus.domain.user.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cotato.kampus.domain.user.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByUniqueId(String uniqueId);

	Boolean existsByNickname(String nickname);

	@Query("SELECT u.nickname FROM User u WHERE u.id = :id")
	String findNicknameById(Long id);
}
