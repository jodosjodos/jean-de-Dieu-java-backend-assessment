package com.teletrack360.user.repository;

import com.teletrack360.common.enums.UserRole;
import com.teletrack360.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
    
    Page<User> findByRole(UserRole role, Pageable pageable);
    
    Page<User> findByStatus(String status, Pageable pageable);
    
    @Query("SELECT u FROM User u WHERE u.status = 'ACTIVE'")
    Page<User> findAllActiveUsers(Pageable pageable);
}
