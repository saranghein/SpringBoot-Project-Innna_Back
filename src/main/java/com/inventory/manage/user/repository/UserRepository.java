package com.inventory.manage.user.repository;

import java.util.Optional;

import com.inventory.manage.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUserId(String UserId);

    boolean existsByUserId(String userId);

    boolean existsByNickname(String nickname);

    void deleteByUserId(String userId);

    @Query("SELECT COUNT(u) FROM User u")
    Long countTotalUser();
}
