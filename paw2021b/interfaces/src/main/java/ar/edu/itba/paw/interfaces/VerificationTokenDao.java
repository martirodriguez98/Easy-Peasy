package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.VerificationToken;

import java.time.LocalDate;
import java.util.Optional;

public interface VerificationTokenDao {

    VerificationToken generateVerificationToken(User user, String token, LocalDate expirationDate);
    Optional<VerificationToken> getTokenByStringValue(String token);
    void removeTokenByUserId(User user);
    Optional<VerificationToken> getTokenByUserId(User user);
}
