package com.github.alideweb.stuffshop.modules.users;

import com.github.alideweb.stuffshop.modules.users.enums.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name = "Users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(length = 32, unique = true)
    private String username;

    @Column()
    private String password;

    @Column(unique = true)
    private String email;

    @Column(length = 32)
    private String firstName;

    @Column(length = 32)
    private String lastName;

    @Column(columnDefinition = "varchar(10) default 'User'")
    @Enumerated(EnumType.STRING)
    private UserRole role;
}
