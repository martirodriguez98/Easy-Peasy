package ar.edu.itba.paw.webapp.config;

import ar.edu.itba.paw.webapp.auth.CustomAccessDeniedHandler;
import ar.edu.itba.paw.webapp.auth.CustomAuthenticationErrorHandler;
import ar.edu.itba.paw.webapp.auth.JwtAuthFilter;
import ar.edu.itba.paw.webapp.auth.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.header.HeaderWriter;
import org.springframework.security.web.header.writers.CacheControlHeadersWriter;
import org.springframework.security.web.header.writers.DelegatingRequestMatcherHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.NegatedRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableWebSecurity
@ComponentScan("ar.edu.itba.paw.webapp.auth")

public class WebAuthConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtAuthFilter jwtAuthFilter;

    @Value("classpath:auth/auth_key.pem")
    private Resource authKey;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler(){
        return new CustomAccessDeniedHandler();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new CustomAuthenticationErrorHandler();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {

         RequestMatcher notResourcesMatcher = new NegatedRequestMatcher(
            new OrRequestMatcher(
                new AntPathRequestMatcher("/api/recipes/{id:[\\d]+}/images/{id:[\\d]+}"),
                new AntPathRequestMatcher("/api/recipes/{id:[\\d]+}/images/{id:[\\d]+}/")
            )
        );

        HeaderWriter notResourcesHeaderWriter = new DelegatingRequestMatcherHeaderWriter(notResourcesMatcher, new CacheControlHeadersWriter());


        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint())
                .accessDeniedHandler(accessDeniedHandler())
                .and()
                .headers()
                .cacheControl().disable()
                .addHeaderWriter(notResourcesHeaderWriter)
                .and().authorizeRequests()
                //users
                .antMatchers(HttpMethod.PUT,
                        "/api/users/{id:[\\d]+}/profileImage").authenticated()
                .antMatchers(HttpMethod.PUT,
                "api/users/verification").hasRole("UNVERIFIED")
                .antMatchers(HttpMethod.PUT,
                        "/api/users/admin/{id:[\\d]+}",
                        "/api/users/banned/{id:[\\d]+}").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE,
                        "/api/users/banned/{id:[\\d]+}",
                        "/api/users/reports/{id:[\\d]+}",
                        "/api/users/{id:[\\d]+}/reports").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE,
                        "/api/users/admin/{id:[\\d]+}").hasRole("MANAGER")
                .antMatchers(HttpMethod.POST,
                        "/api/users/verification",
                        "/api/users/reports"
                        ).authenticated()
                .antMatchers(HttpMethod.GET,
                        "/api/users/reports").hasRole("ADMIN")
                //recipes
                .antMatchers(HttpMethod.PUT,
                        "/api/recipes/{id:[\\d]+}/likes",
                        "/api/recipes/{id:[\\d]+}/dislikes",
                        "/api/recipes/{id:[\\d]+}/favorite",
                        "/api/recipes/{id:[\\d]+}/comments"
                        ).authenticated()
                .antMatchers(HttpMethod.PUT,
                        "/api/recipes/{id:[\\d]+}/highlighted"
                        ).hasRole("ADMIN")
                .antMatchers(HttpMethod.POST,
                        "/api/recipes").hasRole("VERIFIED")
                .antMatchers(HttpMethod.DELETE,
                        "/api/recipes/{id:[\\d]+}",
                        "/api/recipes/{id:[\\d]+/favorite}",
                        "/api/recipes/{id:[\\d]+}/comments/{idComment:[\\d]+}"
                        ).authenticated()
                .and().csrf().disable()
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

    }
    @Override
    public void configure(final WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/css/**", "/js/**", "/img/**", "/favicon.ico", "/403");
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public JwtUtil jwtUtil(@Value("classpath:auth/auth_key.pem") Resource authKey) throws IOException {
        return new JwtUtil(authKey);
    }



}