package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.models.Role;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            final Collection<GrantedAuthority> verifauthority = getAuth(Collections.singletonList(Role.VERIFIED));
            final Collection<GrantedAuthority> bannedauth = getAuth(Collections.singletonList(Role.BANNED));

            final Collection<? extends GrantedAuthority> currentAuthorities = auth.getAuthorities();
            for(GrantedAuthority ga : bannedauth){
                if(currentAuthorities.contains(ga)){
                    httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/banned");
                    return;
                }
            }
            for(GrantedAuthority ga : verifauthority){
                if(!currentAuthorities.contains(ga)){
                    httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/accountNotVerified");
                    return;
                }
            }
            httpServletResponse.sendRedirect(httpServletRequest.getContextPath() + "/403");
        }
    }

    private Collection<GrantedAuthority> getAuth(Collection<Role> roles) {
        return roles.
                stream()
                .map((role) -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
    }
}

