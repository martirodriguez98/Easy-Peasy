package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.RoleDao;
import ar.edu.itba.paw.models.Role;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.*;

@Repository
public class RoleDaoImpl implements RoleDao {

    @PersistenceContext
    private EntityManager em;

    private static final Logger LOGGER = LoggerFactory.getLogger(RoleDaoImpl.class);

    @Override
    public UserRole addRole(User user, Role role) {
        final UserRole userRole = new UserRole(role.getRoleName(),user);
        em.persist(userRole);
        LOGGER.info("Adding role {} to user with id {}",role,user.getIdUser());
        return userRole;
    }

    @Override
    public void removeRole(User user, Role role) {
        final TypedQuery<UserRole> query = em.createQuery("select u from UserRole u where u.user = :user and u.role = :role",UserRole.class);
        query.setParameter("user",user);
        query.setParameter("role",role.getRoleName());
        if(query.getResultList().isEmpty())
            return;
        UserRole userRole = em.find(UserRole.class, query.getResultList().get(0).getId());
        LOGGER.info("Removing role {} to user with id {}",role,user.getIdUser());
        em.remove(userRole);
    }

    @Override
    public List<UserRole> getRolesById(User user) {
        final String query = "SELECT u FROM UserRole u WHERE u.user = :user";
        final TypedQuery<UserRole> queryResult = em.createQuery(query,UserRole.class);
        queryResult.setParameter("user",user);
        return queryResult.getResultList();
    }

    @Override
    public boolean isVerified(User user) {
        final String query = "SELECT u FROM UserRole u WHERE u.user = :user AND u.role = 'VERIFIED'";
        final TypedQuery<UserRole> queryResult = em.createQuery(query,UserRole.class);
        queryResult.setParameter("user",user);
        return !queryResult.getResultList().isEmpty();
    }

}
