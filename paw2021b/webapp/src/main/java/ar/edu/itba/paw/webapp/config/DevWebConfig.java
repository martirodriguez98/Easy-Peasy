package ar.edu.itba.paw.webapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("dev")
@Configuration
public class DevWebConfig {
    @Bean(name = "appHost")
    public String appHost() {
        return "localhost";
    }

    @Bean(name = "appProtocol")
    public String appProtocol() {
        return "http";
    }

    @Bean(name = "appWebContext")
    public String appWebContext() {
        return "/";
    }

    @Bean(name = "appPort")
    public int appPort() {
        return 4200;
    }

    @Bean(name = "DBUrl")
    public String DBUrl() {
        return "jdbc:postgresql://localhost/paw";
    }

    @Bean(name = "DBUser")
    public String DBUser() {
        return "postgres";
    }

    @Bean(name = "DBPass")
    public String DBPass() {
        return "postgres";
    }

}
