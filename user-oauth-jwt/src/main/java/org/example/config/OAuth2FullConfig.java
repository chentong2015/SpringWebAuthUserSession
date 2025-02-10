package org.example.config;

import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;

import java.security.interfaces.RSAPublicKey;
import java.util.List;

import static org.springframework.security.oauth2.core.OAuth2TokenIntrospectionClaimNames.AUD;

public class OAuth2FullConfig {

    private RSAPublicKey publicKey;

    /**
     * Build and configure the JwtDecoder that will be used when we receive a Jwt Token.
     * Here we take in a {@see RSAPublicKey} but you can also supply a JWK uri, or a {@see SecretKey}.
     * By default, the decoder will always verify the signature with the given key
     * and validate the timestamp to check if the JWT is still valid.
     *
     * Our decoder can be customized with several options.
     * We can for instance do custom validation on claims, do rename, add and remove claims,
     * and even change the datatype that the claim is mapped too.
     *
     * All we do below is to add some custom validation to the "issuer" claim
     * and a custom validator to validate the aud claim,
     * remember we must add the default timestamp validation back as we have overridden defaults.
     * By default a Public key will set the algorithm to RS256.
     * @return JwtDecoder
     */
    // @Bean
    public JwtDecoder jwtDecoder() {
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withPublicKey(this.publicKey).build();
        decoder.setJwtValidator(tokenValidator());
        return decoder;
    }

    /**
     * We can write custom validators to validate different parts of the JWT.
     * By default, the framework will always validate the timestamp, but we can add validators to enhance security.
     * For instance you should always validate the issuer to make sure that the JWT was issued from a known source.
     * Remember that if we customise the validation we need to re-add the timestamp validator.
     *
     * Here we crate a list of validators. The {@see JwtTimestampValidator} and the {@see JwtIssuerValidator} are
     * from the spring security framework, but we have also added a custom one. Remember if you add a custom list, you
     * must always remember to add timestamp validation or else this will be removed.
     * @return Oauth2TokenValidator<Jwt>
     */
    public OAuth2TokenValidator<Jwt> tokenValidator() {
        final List<OAuth2TokenValidator<Jwt>> validators =
                List.of(new JwtTimestampValidator(), new JwtIssuerValidator("http://xxx.com"), audienceValidator());
        return new DelegatingOAuth2TokenValidator<>(validators);
    }

    /**
     * You can write a custom validation by adding a {@see JwtClaimValidator} for instance
     * we add a custom validator to the aud (audience) claim. And check that it contains a certain string.
     * {@see OAuth2TokenIntrospectionClaimNames} contains static string names of several default claims.
     * Below we are referencing the {@see OAuth2TokenIntrospectionClaimNames#AUD} string.
     *
     * @return Oauth2TokenValidator<T>
     */
    public OAuth2TokenValidator<Jwt> audienceValidator() {
        return new JwtClaimValidator<List<String>>(AUD, aud -> aud.contains("foobar"));
    }
}
