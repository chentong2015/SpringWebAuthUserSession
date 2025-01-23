import jwt.JwtTokenProvider;
import jwt.JwtTokenValidator;

public class JsonWebToken {

    public static void main(String[] args) {
        String token = JwtTokenProvider.generateJwtToken("chen");
        System.out.println(token);

        String username = JwtTokenProvider.getUsernameFromJwtToken(token);
        System.out.println(username);

        JwtTokenValidator tokenValidator = new JwtTokenValidator();
        System.out.println(tokenValidator.validateJwtToken(token));
    }
}