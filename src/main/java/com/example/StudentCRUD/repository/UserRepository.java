package com.example.StudentCRUD.repository;

import com.example.StudentCRUD.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findByName(String name);
    void deleteByName(String name);
    Optional<User> findByEmail(String email);
}
