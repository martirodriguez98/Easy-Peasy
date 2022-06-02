package ar.edu.itba.paw.webapp.form;

import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.Size;

public class ResetPasswordEmailForm {
    @Size(min = 6, max = 254)
    @Email
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
