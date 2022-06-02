package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.Size;

public class ResetPasswordForm {
    @Size(min = 6, max = 30)
    private String password;

    @Size(min = 6, max = 30)
    private String repeatPassword;

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
