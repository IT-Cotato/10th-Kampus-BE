package com.cotato.kampus.domain.user.dao;

import java.util.List;
import java.util.Optional;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.cotato.kampus.domain.user.domain.User;
import com.cotato.kampus.domain.user.enums.Nationality;
import com.cotato.kampus.domain.user.enums.PreferredLanguage;
import com.cotato.kampus.domain.user.enums.UserRole;
import com.cotato.kampus.domain.user.enums.UserStatus;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class UserRepositoryTest {

	@Autowired
	private UserRepository userRepository;

	@Test
	public void UserRepository_Save_ReturnSavedUser() {

		//Arrange
		User user = User.builder()
			.email("e1")
			.uniqueId("u1")
			.providerId("p1")
			.username("u1")
			.nickname("n1")
			.nationality(Nationality.KOREA)
			.preferredLanguage(PreferredLanguage.KOREAN)
			.userRole(UserRole.UNVERIFIED)
			.userStatus(UserStatus.ACTIVE)
			.build();

		//Act
		User savedUser = userRepository.save(user);

		//Assert
		Assertions.assertThat(savedUser).isNotNull();
		Assertions.assertThat(savedUser.getId()).isGreaterThan(0);
	}

	@Test
	public void UserRepository_GetAll_ReturnMoreThanOneUser() {

		User user = User.builder()
			.email("e1")
			.uniqueId("u1")
			.providerId("p1")
			.username("u1")
			.nickname("n1")
			.nationality(Nationality.KOREA)
			.preferredLanguage(PreferredLanguage.KOREAN)
			.userRole(UserRole.UNVERIFIED)
			.userStatus(UserStatus.ACTIVE)
			.build();

		User user2 = User.builder()
			.email("e2")
			.uniqueId("u2")
			.providerId("p2")
			.username("u2")
			.nickname("n2")
			.nationality(Nationality.KOREA)
			.preferredLanguage(PreferredLanguage.KOREAN)
			.userRole(UserRole.UNVERIFIED)
			.userStatus(UserStatus.ACTIVE)
			.build();

		userRepository.save(user);
		userRepository.save(user2);

		List<User> userList = userRepository.findAll();

		Assertions.assertThat(userList).isNotNull();
		Assertions.assertThat(userList.size()).isEqualTo(2);
	}

	@Test
	public void UserRepository_FindById_ReturnUser() {

		User user = User.builder()
			.email("e1")
			.uniqueId("u1")
			.providerId("p1")
			.username("u1")
			.nickname("n1")
			.nationality(Nationality.KOREA)
			.preferredLanguage(PreferredLanguage.KOREAN)
			.userRole(UserRole.UNVERIFIED)
			.userStatus(UserStatus.ACTIVE)
			.build();

		userRepository.save(user);

		User savedUser = userRepository.findById(user.getId()).get();

		Assertions.assertThat(savedUser).isNotNull();
	}


	@Test
	public void UserRepository_FindByUniqueId_ReturnUserNotNull() {

		User user = User.builder()
			.email("e1")
			.uniqueId("u1")
			.providerId("p1")
			.username("u1")
			.nickname("n1")
			.nationality(Nationality.KOREA)
			.preferredLanguage(PreferredLanguage.KOREAN)
			.userRole(UserRole.UNVERIFIED)
			.userStatus(UserStatus.ACTIVE)
			.build();

		userRepository.save(user);

		User savedUser = userRepository.findByUniqueId(user.getUniqueId()).get();

		Assertions.assertThat(savedUser).isNotNull();
	}

	@Test
	public void UserRepository_UpdateUser_ReturnUserNotnull() {

		User user = User.builder()
			.email("e1")
			.uniqueId("u1")
			.providerId("p1")
			.username("u1")
			.nickname("n1")
			.nationality(Nationality.KOREA)
			.preferredLanguage(PreferredLanguage.KOREAN)
			.userRole(UserRole.UNVERIFIED)
			.userStatus(UserStatus.ACTIVE)
			.build();

		userRepository.save(user);

		User savedUser = userRepository.findById(user.getId()).get();
		user.setUniversityId(1L);

		User updatedUser = userRepository.save(savedUser);

		Assertions.assertThat(updatedUser.getUniversityId()).isNotNull();
		Assertions.assertThat(updatedUser.getUniversityId()).isEqualTo(1L);
	}


	@Test
	public void UserRepository_UserDelete_ReturnUserIsEmpty() {

		User user = User.builder()
			.email("e1")
			.uniqueId("u1")
			.providerId("p1")
			.username("u1")
			.nickname("n1")
			.nationality(Nationality.KOREA)
			.preferredLanguage(PreferredLanguage.KOREAN)
			.userRole(UserRole.UNVERIFIED)
			.userStatus(UserStatus.ACTIVE)
			.build();

		userRepository.save(user);

		userRepository.deleteById(user.getId());

		Optional<User> userReturn = userRepository.findById(user.getId());

		Assertions.assertThat(userReturn).isEmpty();
	}

	@Test
	void existsByNickname() {
	}
}