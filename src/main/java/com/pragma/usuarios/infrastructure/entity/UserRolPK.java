package com.pragma.usuarios.infrastructure.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class UserRolPK {

    @Column(name = "user_id")
    private int userId;

    @Column(name = "role_id")
    private int rolId;
}
