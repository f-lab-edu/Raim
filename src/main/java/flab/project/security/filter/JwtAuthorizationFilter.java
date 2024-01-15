package flab.project.security.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import flab.project.domain.User;
import flab.project.exception.ExceptionCode;
import flab.project.exception.KakaoException;
import flab.project.repository.UserRepository;
import flab.project.security.userDetails.UserContext;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final UserRepository userRepository;
    private final SecurityContextRepository securityContextRepository;
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        return authorization == null || !authorization.startsWith("Bearer ");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        verifyJwtToken(request, response);

        filterChain.doFilter(request, response);
    }

    private void verifyJwtToken(HttpServletRequest request, HttpServletResponse response) {
        String jwt = request.getHeader("Authorization").replace("Bearer ", "");
        DecodedJWT decodedJWT;

        try {
            decodedJWT = JWT.require(Algorithm.HMAC512("testtesttesttesttesttesttesttest"))
                    .build().verify(jwt);

        } catch (JWTVerificationException e) {
            log.info("유저의 토큰 정보가 잘못되었습니다.");
            throw new KakaoException(ExceptionCode.SERVER_ERROR);
        }
        Long userId = decodedJWT.getClaim("id").asLong();

        User user = userRepository.findById(userId).get();

        List<GrantedAuthority> roles = new ArrayList<>();
        roles.add(new SimpleGrantedAuthority(user.getUserRole().getRole()));

        UserContext userContext = new UserContext(user, roles);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userContext, null, roles);

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);
        securityContextRepository.saveContext(securityContext, request,response);
    }
}
