package com.pragma.users.infrastructure.repository;

import com.pragma.users.domain.model.enums.RoleName;
import com.pragma.users.infrastructure.out.jpa.entity.RolEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RolEntity, Long> {

    RolEntity findByName(RoleName name);
}
