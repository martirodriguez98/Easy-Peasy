package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.PasswordTokenDao;
import ar.edu.itba.paw.models.PasswordToken;
import ar.edu.itba.paw.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.*;

@Repository
public class PasswordTokenDaoImpl implements PasswordTokenDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(PasswordTokenDaoImpl.class);
    @PersistenceContext
    private EntityManager em;

    @Transactional
    @Override
    public PasswordToken generatePasswordToken(User user, String token, LocalDate expirationDate) {
        final PasswordToken ptok = new PasswordToken(token,user,expirationDate);
        LOGGER.info("generating password reset token");
        em.persist(ptok);
        return ptok;
    }

    @Override
    public Optional<PasswordToken> getTokenByStringValue(String token) {
        TypedQuery<PasswordToken> query = em.createQuery("SELECT p FROM PasswordToken p WHERE p.token = :token", PasswordToken.class);
        query.setParameter("token", token);
        return query.getResultList().stream().findFirst();
    }

    @Transactional
    @Override
    public void removeTokenByUserId(User user) {
        TypedQuery<PasswordToken> query = em.createQuery("select t from PasswordToken t where t.user = :user",PasswordToken.class);
        query.setParameter("user",user);
        List<PasswordToken> tokens = query.getResultList();
        if(!tokens.isEmpty()){
            em.remove(em.find(PasswordToken.class,tokens.get(0).getId()));
        }

    }

}
