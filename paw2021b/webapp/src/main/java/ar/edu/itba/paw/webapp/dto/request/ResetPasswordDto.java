package ar.edu.itba.paw.webapp.dto.request;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Size;

public class ResetPasswordDto {

    @NotEmpty
    private String token;

    @NotEmpty
    @Size(min = 6,max=30)
    private String password;

    public ResetPasswordDto() {
    }

    public ResetPasswordDto(String token, String password) {
        this.token = token;
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
