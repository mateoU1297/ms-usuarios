package com.pragma.users.infrastructure.repository;

import com.pragma.users.infrastructure.out.jpa.entity.UserRoleEntity;
import com.pragma.users.infrastructure.out.jpa.entity.UserRolePK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRoleEntity, UserRolePK> {
}
