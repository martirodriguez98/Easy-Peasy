package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "password_tokens")
public class PasswordToken {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "password_tokens_id_seq")
    @SequenceGenerator(sequenceName = "password_tokens_id_seq", name="password_tokens_id_seq", allocationSize = 1)
    @Column(name="id")
    private long id;

    @Column(name = "token", nullable = false)
    private String token;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user")
    private User user;

    @Column(name = "expiration_date", nullable = false)
    private LocalDate expirationDate;

    private static final int TOKEN_DAY_DURATION = 2;
    private static final long MILLIS_IN_DAY = 24*60*60*1000;

    public PasswordToken(String token, User user, LocalDate expirationDate) {
        this.token = token;
        this.user = user;
        this.expirationDate = expirationDate;
    }

    protected PasswordToken(){

    }

    public static LocalDate generateExpirationDate(){
        return LocalDate.now().plusDays(TOKEN_DAY_DURATION);
    }

    public boolean isValid(){
        return expirationDate.compareTo(LocalDate.now()) > 0;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
