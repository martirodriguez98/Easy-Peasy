package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.models.Role;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserService us;

    @Override
    public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
        final Optional<User> user_found = us.findByUsername(username);
        if (!user_found.isPresent()) {
            throw new UsernameNotFoundException("No user by the name " + username);
        }
        User user = user_found.get();
        final Collection<? extends GrantedAuthority> authorities = getRoles(us.getRoles(user));
        return new org.springframework.security.core.userdetails.User(username, user.getPassword(), authorities);
    }

    public static Collection<? extends GrantedAuthority> getRoles(Collection<UserRole> roles){
        List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
        for(UserRole role: roles){
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRole().getRoleName()));
        }
        return authorities;
    }

    private Collection<? extends GrantedAuthority> getRolesAndVerified(Collection<UserRole> roles){
        List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
        for(UserRole role: roles){
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getRole().getRoleName()));
        }
        authorities.add(new SimpleGrantedAuthority("ROLE_" + Role.VERIFIED.getRoleName()));
        return authorities;
    }

    public void updateVerifiedAuthority(final String username){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final Optional<User> user_found = us.findByUsername(username);
        if (!user_found.isPresent()) {
            throw new UsernameNotFoundException("No user by the name " + username);
        }
        User user = user_found.get();
        final Collection<? extends GrantedAuthority> authorities = getRolesAndVerified(us.getRoles(user));
        Authentication newAuth = new UsernamePasswordAuthenticationToken(auth.getPrincipal(), auth.getCredentials(), authorities);
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }



}

