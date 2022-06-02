package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.SessionRefreshToken;
import ar.edu.itba.paw.models.User;

import java.time.LocalDateTime;
import java.util.Optional;

public interface SessionRefreshTokenDao {
    SessionRefreshToken createToken(User user, String token, LocalDateTime expirationDate);

    Optional<SessionRefreshToken> getTokenByValue(String token);

    void removeToken(SessionRefreshToken token);

    Optional<SessionRefreshToken> getTokenByUser(User user);
}
