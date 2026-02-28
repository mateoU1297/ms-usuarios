package com.pragma.usuarios.infrastructure.repository;

import com.pragma.usuarios.infrastructure.out.jpa.entity.UserRoleEntity;
import com.pragma.usuarios.infrastructure.out.jpa.entity.UserRolePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRoleEntity, UserRolePK> {
}
