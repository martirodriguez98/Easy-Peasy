package ar.edu.itba.paw.webapp.dto.request;

import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class RegisterDto {
    @Size(min = 2, max = 30)
    @Pattern(regexp = "[a-zA-Z0-9_]+")
    private String username;
    @Size(min = 6, max = 30)
    private String password;
    @Size(min = 6, max = 254)
    @Email
    private String email;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
