package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.VerificationTokenDao;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.VerificationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class VerificationTokenDaoImpl implements VerificationTokenDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(VerificationTokenDaoImpl.class);
    @PersistenceContext
    private EntityManager em;

    @Override
    public VerificationToken generateVerificationToken(User user, String token, LocalDate expirationDate) {
        final VerificationToken vtok = new VerificationToken(token,user,expirationDate);
        LOGGER.info("generating verification token");
        em.persist(vtok);
        return vtok;
    }

    @Override
    public Optional<VerificationToken> getTokenByStringValue(String token) {
        final TypedQuery<VerificationToken> query = em.createQuery("SELECT v FROM VerificationToken v WHERE v.token = :token",VerificationToken.class);
        query.setParameter("token",token);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public Optional<VerificationToken> getTokenByUserId(User user) {
        final TypedQuery<VerificationToken> query = em.createQuery("SELECT v FROM VerificationToken v WHERE v.user = :user",VerificationToken.class);
        query.setParameter("user",user);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public void removeTokenByUserId(User user) {
        final TypedQuery<VerificationToken> query = em.createQuery("select vt FROM VerificationToken vt where vt.user = :user",VerificationToken.class);
        query.setParameter("user",user);
        List<VerificationToken> tokens = query.getResultList();
        if(!tokens.isEmpty()){
            em.remove(em.find(VerificationToken.class,tokens.get(0).getId()));
        }
    }

}
