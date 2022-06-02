package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "verify_tokens")
public class VerificationToken {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "verify_tokens_id_seq")
    @SequenceGenerator(sequenceName = "verify_tokens_id_seq", name="verify_tokens_id_seq", allocationSize = 1)
    @Column(name = "id")
    private long id;

    @Column(name = "token", nullable = false)
    private String token;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user")
    private User user;

    @Column(name = "expiration_date", nullable = false)
    private LocalDate expirationDate;

    private static final int TOKEN_DAY_DURATION = 2;

    public VerificationToken(String token, User user, LocalDate expirationDate) {
        this.token = token;
        this.user = user;
        this.expirationDate = expirationDate;
    }

    protected VerificationToken(){

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VerificationToken that = (VerificationToken) o;
        return user.getIdUser() == that.user.getIdUser() && Objects.equals(token, that.token);
    }

}
