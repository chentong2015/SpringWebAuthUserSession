package key;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;

public class SignedKey {

    // 用于数字签名的Key
    private static String key = "404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970";

    public static String getKey() {
        return key;
    }

    // Creates a new SecretKey instance for use with HMAC-SHA algorithms
    public static SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(key);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
