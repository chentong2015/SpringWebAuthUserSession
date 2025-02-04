package org.example.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Component
public class JwtProvider {

    private final JwtEncoder jwtEncoder;

    public JwtProvider(JwtEncoder jwtEncoder) {
        this.jwtEncoder = jwtEncoder;
    }

    // TODO. 将Username和Authority设置到JWT中
    public String createToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));// MUST BE space-delimited.

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(authentication.getName())
                .issuer("self")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plus(2, ChronoUnit.HOURS))
                .claim("username", userDetails.getUsername())
                .claim("authorities", authorities)
                .build();

        Jwt jwt = this.jwtEncoder.encode(JwtEncoderParameters.from(claims));
        return jwt.getTokenValue();
    }
}
