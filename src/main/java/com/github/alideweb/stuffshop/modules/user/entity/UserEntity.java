package com.github.alideweb.stuffshop.modules.user.entity;

import com.github.alideweb.stuffshop.common.entity.BaseEntity;
import com.github.alideweb.stuffshop.modules.user.enums.UserRoles;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "Users")
@NoArgsConstructor
@Getter
@Setter
public class UserEntity extends BaseEntity {
    @Column(unique = true, nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRoles role;

    @Column(nullable = false)
    private String password;

    private String name;
}
