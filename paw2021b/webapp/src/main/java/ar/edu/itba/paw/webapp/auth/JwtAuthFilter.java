package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.exceptions.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

import static ar.edu.itba.paw.webapp.auth.UserDetailsServiceImpl.getRoles;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    @Autowired
    private URL appBaseUrl;

    private static final Base64.Decoder BASE_64_DECODER = Base64.getDecoder();

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthFilter.class);

    private static final RequestMatcher LOGIN_ENDPOINT_MATCHER = new OrRequestMatcher(
        new AntPathRequestMatcher("/api/login"),
        new AntPathRequestMatcher("/api/login/")
    );

    private static final RequestMatcher REFRESH_ENDPOINT_MATCHER = new OrRequestMatcher(
        new AntPathRequestMatcher("/api/refreshToken"),
        new AntPathRequestMatcher("/api/refreshToken/")
    );

    private static final RequestMatcher AUTH_ENDPOINTS_MATCHER = new OrRequestMatcher(
        REFRESH_ENDPOINT_MATCHER,
        LOGIN_ENDPOINT_MATCHER
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            if (checkAuthenticationError(request, response)) {
                return;
            }
            chain.doFilter(request, response);
            return;
        }

        final AuthorizationType authType = parseAuthorizationType(authHeader);
        if (authType == null) {
            if (checkAuthenticationError(request, response)) {
                return;
            }
            chain.doFilter(request, response);
            return;
        }

        final String payload = authHeader.split(" ")[1].trim();

        Authentication auth;

        try {
            if (authType == AuthorizationType.BASIC) {
                if (checkRefreshPath(request, response)) {
                    return;
                }
                auth = tryBasicAuthentication(payload, response);
            } else {
                if (checkLoginPath(request, response)) {
                    return;
                }
                auth = tryBearerAuthentication(payload, response);
            }

            if (checkAuthenticationSuccess(request, response)) {
                return;
            }

            SecurityContextHolder
                .getContext()
                .setAuthentication(auth);

            LOGGER.debug("Populated security context with authorization: {}", auth);
        } catch (AuthenticationException e) {
            if (checkAuthenticationError(request, response)) {
                return;
            }
            LOGGER.debug("{} Setting default unauthorized user", e.getMessage());
        }

        chain.doFilter(request, response);
    }

    private Authentication tryBearerAuthentication(final String payload, final HttpServletResponse response) throws AuthenticationException {

        //Try first with JWT
        UserDetails userDetails = jwtUtil.decodeToken(payload);
        if (userDetails != null) {
            return new UsernamePasswordAuthenticationToken(
                userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
        }

        //If JWT is invalid try with refresh token
        final Optional<User> userOpt = userService.getUserByRefreshToken(payload);

        if (!userOpt.isPresent()) {
            throw new AuthenticationCredentialsNotFoundException("Invalid refresh token.");
        }

        final User user = userOpt.get();

        addAuthorizationHeader(response, user);

        return new UsernamePasswordAuthenticationToken
            (user.getUsername(), user.getPassword(), getRoles(user.getRoles()));
    }

    private Authentication tryBasicAuthentication(final String payload, final HttpServletResponse response) throws AuthenticationException {

        String[] decodedCredentials;

        try {
            decodedCredentials = new String(BASE_64_DECODER.decode(payload), StandardCharsets.UTF_8).split(":");
            if (decodedCredentials.length != 2) {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            throw new AuthenticationCredentialsNotFoundException("Invalid credentials for basic authorization.");
        }

        final String email = decodedCredentials[0];
        final String password = decodedCredentials[1];

        Authentication auth = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(email, password)
        );

        final User user = userService.findByUsername(auth.getName()).orElseThrow(BadRequestException::new);

        addAuthorizationHeader(response, user);
        addSessionRefreshTokenHeader(response, user);

        return auth;
    }

    private void addAuthorizationHeader(final HttpServletResponse response, final User user) {
        response.addHeader(JwtUtil.JWT_HEADER, jwtUtil.generateToken(user, appBaseUrl.toString()));
    }

    private void addSessionRefreshTokenHeader(final HttpServletResponse response, final User user) {
        response.addHeader(JwtUtil.REFRESH_TOKEN_HEADER, userService.getSessionRefreshToken(user).getValue());
    }

    private AuthorizationType parseAuthorizationType(final String authHeader) {
        if (authHeader.startsWith("Bearer ")) {
            return AuthorizationType.BEARER;
        } else if (authHeader.startsWith("Basic ")) {
            return AuthorizationType.BASIC;
        }

        return null;
    }

    private boolean checkAuthenticationError(final HttpServletRequest request, final HttpServletResponse response) {
        if (AUTH_ENDPOINTS_MATCHER.matches(request)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return true;
        }

        return false;
    }

    private boolean checkAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response) {
        if (AUTH_ENDPOINTS_MATCHER.matches(request)) {
            response.setStatus(HttpServletResponse.SC_OK);
            return true;
        }

        return false;
    }

    private boolean checkRefreshPath(final HttpServletRequest request, final HttpServletResponse response) {
        if (REFRESH_ENDPOINT_MATCHER.matches(request)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return true;
        }

        return false;
    }


    private boolean checkLoginPath(final HttpServletRequest request, final HttpServletResponse response) {
        if (LOGIN_ENDPOINT_MATCHER.matches(request)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return true;
        }

        return false;
    }

    private enum AuthorizationType {
        BASIC, BEARER
    }
}
