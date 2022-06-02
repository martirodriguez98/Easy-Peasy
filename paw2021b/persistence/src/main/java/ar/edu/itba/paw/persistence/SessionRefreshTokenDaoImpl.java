package ar.edu.itba.paw.persistence;


import ar.edu.itba.paw.interfaces.SessionRefreshTokenDao;
import ar.edu.itba.paw.models.SessionRefreshToken;
import ar.edu.itba.paw.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public class SessionRefreshTokenDaoImpl implements SessionRefreshTokenDao {
    @PersistenceContext
    private EntityManager em;

    private static final Logger LOGGER = LoggerFactory.getLogger(SessionRefreshTokenDaoImpl.class);

    @Override
    public SessionRefreshToken createToken(User user, String token, LocalDateTime expirationDate) {
        LOGGER.debug("Creating token for user");
        final SessionRefreshToken sessionRefreshToken = new SessionRefreshToken(token, user, expirationDate);

        em.persist(sessionRefreshToken);

        return sessionRefreshToken;
    }

    @Override
    public Optional<SessionRefreshToken> getTokenByValue(String token) {
        LOGGER.debug("Retrieving token with value {}", token);
        return em.
            createQuery("from SessionRefreshToken where value = :token",
                SessionRefreshToken.class)
            .setParameter("token", token)
            .getResultList()
            .stream()
            .findFirst();
    }

    @Override
    public void removeToken(SessionRefreshToken token) {
        LOGGER.debug("Removing token");
        em.remove(token);
    }

    @Override
    public Optional<SessionRefreshToken> getTokenByUser(User user) {
        LOGGER.debug("Retrieving token for user");
        return em.createQuery(
            "FROM SessionRefreshToken srt where srt.user.id = :userId", SessionRefreshToken.class)
            .setParameter("userId", user.getIdUser())
            .getResultList()
            .stream()
            .findFirst();

    }
}
