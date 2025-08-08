package com.github.alideweb.stuffshop.modules.jwt;

import com.github.alideweb.stuffshop.modules.user.enums.UserRoles;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${jwt.key}")
    private String secretKey;

    @Value("${jwt.expiresAtInHour}")
    private int expiresAtInHour;

    public String generateJwtToken(String username, UserRoles role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("username", username);
        claims.put("role", role);

        var now = new Date(System.currentTimeMillis());
        var expiresAt = new Date(System.currentTimeMillis() + ((long) expiresAtInHour * 60 * 60 * 1000));


        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiresAt)
                .signWith(getSecretKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isJwtTokenValid(String token, UserDetails userDetails) {
        final String userName = extractUserName(token);
        final UserRoles userRole = extractUserRole(token);

        String actualRole = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(role -> role.replace("ROLE_", ""))
                .findFirst().orElse(null);

        return Objects.equals(userName, userDetails.getUsername()) &&
                Objects.equals(userRole.name(), actualRole) &&
                !isTokenExpired(token);
    }

    public String extractUserName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public UserRoles extractUserRole(String token) {
        String role = extractClaim(token, claims -> claims.get("role").toString());

        return UserRoles.getRole(role);
    }

    private Key getSecretKey() {
        byte[] keyBytes = secretKey.getBytes(StandardCharsets.UTF_8);

        return new SecretKeySpec(keyBytes, SignatureAlgorithm.HS256.getJcaName());
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSecretKey())
                .build().parseClaimsJws(token).getBody();
    }
}
