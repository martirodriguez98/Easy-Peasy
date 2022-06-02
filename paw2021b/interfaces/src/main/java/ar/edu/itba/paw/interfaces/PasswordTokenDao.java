package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.PasswordToken;
import ar.edu.itba.paw.models.User;

import java.time.LocalDate;
import java.util.Optional;

public interface PasswordTokenDao {
    PasswordToken generatePasswordToken(User user, String token, LocalDate expirationDate);
    Optional<PasswordToken> getTokenByStringValue(String token);
    void removeTokenByUserId(User user);

}
