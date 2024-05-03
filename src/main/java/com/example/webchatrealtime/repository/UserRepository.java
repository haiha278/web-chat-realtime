package com.example.webchatrealtime.repository;

import com.example.webchatrealtime.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUserName(String username);

    boolean existsByEmail(String email);

    User findUserByEmail(String email);

    User findUserByUserName(String username);
}
