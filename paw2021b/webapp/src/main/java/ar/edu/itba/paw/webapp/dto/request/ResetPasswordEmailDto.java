package ar.edu.itba.paw.webapp.dto.request;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class ResetPasswordEmailDto {
    @NotEmpty
    @Size(max = 254)
    @Pattern(regexp = "^([a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+)*$")
    String email;

    public ResetPasswordEmailDto() {
    }

    public ResetPasswordEmailDto(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
