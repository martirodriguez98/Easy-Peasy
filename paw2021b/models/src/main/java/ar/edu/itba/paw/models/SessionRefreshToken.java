package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Base64;

@Entity
@Table(name = "session_refresh_tokens")
public class SessionRefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "session_refresh_tokens_srt_id_seq")
    @SequenceGenerator(sequenceName = "session_refresh_tokens_srt_id_seq", name = "session_refresh_tokens_srt_id_seq", allocationSize = 1)
    @Column(name = "srt_id")
    private Long id;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "srt_user_id")
    private User user;

    @Column(name = "srt_token", nullable = false)
    private String value;

    @Column(name = "srt_expiration_date", nullable = false)
    private LocalDateTime expirationDate;

    private static final int TOKEN_DURATION_DAYS = 5;

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getEncoder();

    public static LocalDateTime generateTokenExpirationDate() {
        return LocalDateTime.now().plusDays(TOKEN_DURATION_DAYS);
    }

    public static String generateSessionToken() {
        final byte[] bytes = new byte[64];

        secureRandom.nextBytes(bytes);

        return base64Encoder.encodeToString(bytes);
    }

    /* default */
    protected SessionRefreshToken() {
        // Just for Hibernate
    }

    public SessionRefreshToken(String value, User user, LocalDateTime expirationDate) {
        this.value = value;
        this.user = user;
        this.expirationDate = expirationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isValid() {
        return expirationDate.compareTo(LocalDateTime.now()) > 0;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public void refresh() {
        setValue(generateSessionToken());
        resetExpirationDate();
    }

    public void resetExpirationDate() {
        expirationDate = generateTokenExpirationDate();
    }
}
