package jwt;

import io.jsonwebtoken.Jwts;
import key.SignedKey;

import java.util.Date;

// JWT生成代码: 签名算法 + Payload信息设置 + SecretKey
public class JwtTokenProvider {

    // ISSUER 发行人的名称
    private static final String ISSUER_NAME = "TOKEN Provider";

    private JwtTokenProvider() {
    }

    // TODO. 基于用户账号进行签名
    public static String generateJwtToken(String username) {
        return Jwts.builder()
               .subject(username)   // Payload 用户名信息
               .issuer(ISSUER_NAME) // Payload 由谁签发
               .issuedAt(TimeHelper.generateCurrentDate()) // Payload 签发时间
               .expiration(TimeHelper.generateExpirationDate()) // Payload 过期时间
               .signWith(SignedKey.getSigningKey()) // Signature 使用key签名
               .compact();
    }

    // 刷新Token的过期时间
    public static String refreshJwtToken(String jwtToken) {
        if (canTokenBeRefreshed(jwtToken)) {
            String username = getUsernameFromJwtToken(jwtToken);
            return generateJwtToken(username);
        }
        return jwtToken;
    }

    // 只有当前时间在Token过期时间之前，才能刷新Token
    public static boolean canTokenBeRefreshed(String jwtToken) {
        Date expirationDate = getExpirationFromJwtToken(jwtToken);
        Date currentDate = TimeHelper.generateCurrentDate();
        return currentDate.compareTo(expirationDate) < 0;
    }

    private static Date getExpirationFromJwtToken(String jwtToken) {
        return Jwts.parser()
               .verifyWith(SignedKey.getSigningKey())
               .build()
               .parseSignedClaims(jwtToken)
               .getPayload()
               .getExpiration();
    }

    // 从签名后的Token中获取用户信息(必须使用相同SecretKey)
    // Sets the signature verification SecretKey used to verify all encountered JWS signatures.
    public static String getUsernameFromJwtToken(String jwtToken) {
        return Jwts.parser()
                .verifyWith(SignedKey.getSigningKey())
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload()  // TODO. 替换废弃的getBody()方法
                .getSubject(); // TODO. 从Payload Claims中获取签名信息
    }
}