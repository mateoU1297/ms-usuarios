package com.pragma.usuarios.infrastructure.repository;

import com.pragma.usuarios.infrastructure.out.jpa.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    @Query("SELECT DISTINCT u FROM UserEntity u " +
            "LEFT JOIN FETCH u.userRoles ur " +
            "LEFT JOIN FETCH ur.role " +
            "WHERE u.email = :email")
    Optional<UserEntity> findByEmailWithRoles(@Param("email") String email);
}
