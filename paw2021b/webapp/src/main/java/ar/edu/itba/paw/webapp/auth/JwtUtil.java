package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.Role;
import ar.edu.itba.paw.models.UserRole;
import io.jsonwebtoken.*;
import org.springframework.core.io.Resource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

public class JwtUtil {
    public static final String JWT_HEADER = "X-JWT";

    public static final String REFRESH_TOKEN_HEADER = "X-Refresh-Token";

    private final String secretKey;

    private final static int TOKEN_EXPIRATION_MILLIS = 1000 * 60 * 20 ;  //20 mins duration

    public JwtUtil(Resource secretKeyRes) throws IOException {
        this.secretKey = FileCopyUtils.copyToString(new InputStreamReader(secretKeyRes.getInputStream()));
    }

    public UserDetails decodeToken(String token) {
        try {

            final Claims claims = extractAllClaims(token);

            final String username = claims.getSubject();

            final Collection<GrantedAuthority> authorities =
                getAuthorities(claims.get("roles", String.class));

            return new org.springframework.security.core.userdetails.User(username, "", authorities);

        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
            return null;
        }
    }

    public String generateToken(User user, String baseUrl) {
        Claims claims = Jwts.claims();

        claims.put("roles", serializeRoles(user.getRoles()));
        claims.put("userUrl", baseUrl + "api/users/" + user.getIdUser());

        return "Bearer " + Jwts.builder()
            .setClaims(claims)
            .setSubject(user.getEmail())
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_MILLIS))
            .signWith(SignatureAlgorithm.HS256, secretKey)
            .compact();
    }


    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }

    private String serializeRoles(Collection<UserRole> roles) {
        final StringBuilder sr = new StringBuilder();

        for (UserRole role : roles) {
            sr.append(role.getRole().name()).append(' ');
        }

        sr.deleteCharAt(sr.length() - 1);

        return sr.toString();
    }

    private Collection<GrantedAuthority> getAuthorities(String roles) {
        return Arrays.stream(roles.split(" "))
            .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
            .collect(Collectors.toList());
    }



}
