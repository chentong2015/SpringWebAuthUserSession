package backend.cookie_session;

import backend.model.bean.TokenState;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jwt.JwtTokenProvider;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api", produces = MediaType.APPLICATION_JSON_VALUE)
public class SessionController {

    @GetMapping("/session/refresh")
    public ResponseEntity<?> refreshAuthenticationToken(HttpServletRequest request, HttpServletResponse response) {
        String authToken = TokenProcessor.fetchToken(request);
        if (authToken == null) {
            TokenState tokenState = new TokenState();
            return ResponseEntity.accepted().body(tokenState);
        }

        String refreshedToken = JwtTokenProvider.refreshJwtToken(authToken);
        CookieManager.addTokenToResponse(response, refreshedToken);

        TokenState tokenState = new TokenState(refreshedToken, 600);
        return ResponseEntity.ok().body(tokenState);
    }
}
