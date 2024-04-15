package com.example.events.Repository;

import com.example.events.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);
    @Query("SELECT u FROM User u WHERE u.username = :username and u.password = :password")
    Optional<User> findUserByUsernameAndPassword(@Param("username") String username,
                                                 @Param("password") String password);
    @Query("SELECT u FROM User u WHERE u.username = :username")
    Optional<User> findUserByUsername(@Param("username") String username);

    @Query("SELECT u FROM User u WHERE u.username = ?#{principal.username}")
    Optional<User> findLoginUser();
    boolean existsByEmailAddress(String emailAddress);
}
