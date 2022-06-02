package ar.edu.itba.paw.webapp.form;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

public class ShareForm {
    @NotEmpty
    @Size(min = 2, max = 50)
    private String from;
    @NotEmpty
    @Size(min = 6, max = 254)
    @Email
    private String email;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
