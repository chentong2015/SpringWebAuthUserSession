package jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import key.SignedKey;
import key.SignedKeyLocator;

public class JwtTokenValidator {

    // 基于特定的Secret Key要验证token是否有效
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser()
              .verifyWith(SignedKey.getSigningKey())
              .requireIssuer("TOKEN Provider")
              .build()
              .parseSignedClaims(authToken);
            return true;
        } catch (SignatureException e) {
            System.out.println("Invalid JWT signature: " + e.getMessage());
        } catch (MalformedJwtException e) {
            System.out.println("Invalid JWT token: " + e.getMessage());
        } catch (ExpiredJwtException e) {
            // 标明Token验证失败的错误类型
            System.out.println("JWT token is expired: " + e.getMessage());
        } catch (UnsupportedJwtException e) {
            System.out.println("JWT token is unsupported: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("JWT claims string is empty: " + e.getMessage());
        }
        return false;
    }

    // 从KeyLocator获取key之后再验证
    public boolean validateJwtTokenWithKeyLocator(String authToken) {
        try {
            Jwts.parser()
              .keyLocator(new SignedKeyLocator())
              .requireIssuer("TOKEN Provider")
              .build()
              .parseSignedClaims(authToken);
            return true;
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
        }
        return false;
    }
}
