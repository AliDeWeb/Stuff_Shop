package com.github.alideweb.stuffshop.modules.auth.dto;

import com.github.alideweb.stuffshop.modules.user.enums.UserRoles;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserResponseDto {
    private String username;
    private String email;
    private String name;
    private UserRoles role;
    private String token;
}
