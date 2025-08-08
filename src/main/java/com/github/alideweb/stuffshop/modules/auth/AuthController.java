package com.github.alideweb.stuffshop.modules.auth;

import com.github.alideweb.stuffshop.common.dto.ApiResponse;
import com.github.alideweb.stuffshop.modules.auth.dto.SignUpRequestDto;
import com.github.alideweb.stuffshop.modules.auth.dto.UserResponseDto;
import com.github.alideweb.stuffshop.modules.jwt.JwtService;
import com.github.alideweb.stuffshop.modules.user.Entity.UserEntity;
import com.github.alideweb.stuffshop.modules.user.UserService;
import com.github.alideweb.stuffshop.modules.user.enums.UserRoles;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "User sign up, login and auth related operations")
public class AuthController {
    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/sign-up")
    public ResponseEntity<ApiResponse<UserResponseDto>> singUp(@Valid @RequestBody SignUpRequestDto request) {
        var user = new UserEntity();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setName(request.getName());
        user.setRole(UserRoles.USER);

        var saved = userService.registerUser(user);
        var jwt = jwtService.generateJwtToken(saved.getUsername(), saved.getRole());

        var userDto = UserResponseDto.builder()
                .username(saved.getUsername())
                .email(saved.getEmail())
                .role(saved.getRole())
                .name(saved.getName())
                .token(jwt)
                .build();

        var response = ApiResponse.<UserResponseDto>builder()
                .message("you signed up successfully")
                .status(HttpStatus.CREATED.value())
                .data(userDto)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
