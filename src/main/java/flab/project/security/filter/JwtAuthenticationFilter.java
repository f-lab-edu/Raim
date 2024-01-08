package flab.project.security.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import flab.project.domain.User;
import flab.project.dto.UserLoginDto;
import flab.project.exception.ExceptionCode;
import flab.project.exception.KakaoException;
import flab.project.security.userDetails.UserContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        UserLoginDto userLoginDto = null;

        try {
            userLoginDto = objectMapper.readValue(request.getInputStream(), UserLoginDto.class);
        } catch (IOException e) {
            throw new KakaoException(ExceptionCode.SERVER_ERROR);
        }

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userLoginDto.getEmail(), userLoginDto.getPassword());

        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) {
        UserContext userContext = (UserContext) authResult.getPrincipal();
        User user = userContext.getUser();

        String accessToken = JWT.create()
                .withSubject(user.getName())
                .withExpiresAt(new Date(System.currentTimeMillis() + 600000))
                .withClaim("id", user.getId())
                .withClaim("authorities", user.getUserRole().getRole())
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withIssuer("Kakao-Backend")
                .sign(Algorithm.HMAC512("testtesttesttesttesttesttesttest"));

        String refreshToken = JWT.create()
                .withSubject(user.getName())
                .withExpiresAt(new Date(System.currentTimeMillis() + 864000000))
                .withIssuedAt(new Date(System.currentTimeMillis()))
                .withIssuer("Kakao-backend")
                .sign(Algorithm.HMAC512("testtesttesttesttesttesttesttest"));

        response.addHeader("Authorization", "Bearer " + accessToken);
        response.addHeader("Refresh", "Bearer " + refreshToken);
    }
}
