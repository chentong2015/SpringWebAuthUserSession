import jwt.JwtTokenProvider;
import jwt.JwtTokenValidator;

public class JsonWebToken {

    public static void main(String[] args) {
        JwtTokenProvider tokenProvider = new JwtTokenProvider();
        String token = tokenProvider.generateJwtToken("chen");
        System.out.println(token);
        System.out.println(tokenProvider.getUserNameFromJwtToken(token));

        JwtTokenValidator tokenValidator = new JwtTokenValidator();
        System.out.println(tokenValidator.validateJwtToken(token));
    }
}