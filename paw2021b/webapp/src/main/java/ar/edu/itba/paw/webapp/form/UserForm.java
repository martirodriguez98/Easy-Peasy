package ar.edu.itba.paw.webapp.form;

import org.hibernate.validator.constraints.Email;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserForm {
    @Size(min = 2, max = 30)
    @Pattern(regexp = "[a-zA-Z0-9_]+")
    private String username;
    @Size(min = 6, max = 30)
    private String password;
    @Size(min = 6, max = 30)
    private String repeatPassword;
    @Size(min = 6, max = 254)
    @Email
    private String email;
    @Size(min = 2, max = 50)
    private String name;
    private MultipartFile avatar;


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
    public String getRepeatPassword() {
        return repeatPassword;
    }
    public void setRepeatPassword(String repeatPassword)
    {
        this.repeatPassword = repeatPassword;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MultipartFile getAvatar() {
        return avatar;
    }

    public void setAvatar(MultipartFile avatar) {
        this.avatar = avatar;
    }
}
