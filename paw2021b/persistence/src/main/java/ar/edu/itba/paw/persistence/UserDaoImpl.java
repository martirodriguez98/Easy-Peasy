package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.interfaces.exceptions.DuplicateUserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class UserDaoImpl implements UserDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(PasswordTokenDaoImpl.class);

    private final int ITEMS_PER_PAGE = 6;

    private final int ITEMS_PER_PAGE_SEARCH = 12;

    @PersistenceContext
    private EntityManager em;

    @Override
    public User create(String username, String email, String password) throws DuplicateUserException {
        Collection<User> emailCollection = em.createQuery("select u FROM User u where u.email = :email", User.class)
                .setParameter("email", email)
                .getResultList();
        Collection<User> userCollection = em.createQuery("select u from User u where u.username = :username", User.class)
                .setParameter("username", username)
                .getResultList();

        if (!userCollection.isEmpty() && !emailCollection.isEmpty()) {
            throw new DuplicateUserException();
        }

        final User user = new User("name", username, email, password, null, null, false, LocalDate.now());
        em.persist(user);
        LOGGER.info("Creating user {} with id {}",user.getUsername(),user.getIdUser());
        return user;
    }

    @Override
    public Optional<User> findById(long id) {
        return Optional.ofNullable(em.find(User.class, id));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        final TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.email = :email", User.class);
        query.setParameter("email", email);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public Optional<User> findByUsername(String username) {
        final String query = "SELECT u FROM User u WHERE u.username = :username";
        final TypedQuery<User> queryResult = em.createQuery(query, User.class);
        queryResult.setParameter("username", username);
        return queryResult.getResultList().stream().findFirst();
    }

    @Override
    public ImageUser findImageById(long idUser) {
        final String query = "SELECT u FROM User u WHERE u.idUser = :idUser";
        final TypedQuery<User> queryResult = em.createQuery(query, User.class);
        queryResult.setParameter("idUser", idUser);
        User user = queryResult.getSingleResult();
        return new ImageUser(idUser, user.getAvatar(), user.getMimeType());
    }

    @Override
    public List<User> findAdmins(AdminPanelOptions order, int page) {

        final List<Object> variables = new LinkedList<>();
        final String orderQuery = getOrder(order);
        final String offsetAndLimitQuery = getOffsetAndLimitQuery(page, ITEMS_PER_PAGE, variables);
        final String filteredIdsQuery = "select id_user from user_t u where u.id_user in (select ur.id_user from roles ur where ur.role_t = 'ADMIN') " + getNativeOrder(order) + offsetAndLimitQuery;

        Query filteredIdsNativeQuery = em.createNativeQuery(filteredIdsQuery);

        setQueryVariables(filteredIdsNativeQuery, variables);
        @SuppressWarnings("unchecked") final List<Long> filteredIds = ((List<Number>) filteredIdsNativeQuery.getResultList())
                .stream()
                .map(Number::longValue)
                .collect(Collectors.toList());
        if (filteredIds.isEmpty())
            return Collections.emptyList();

        StringBuilder finalQuery = new StringBuilder("from User u where u.idUser in :filteredIds");
        finalQuery.append(getOrder(order));

        return em.createQuery(finalQuery.toString(), User.class)
                .setParameter("filteredIds", filteredIds)
                .getResultList();
    }

    @Override
    public List<User> findBannedUsers(AdminPanelOptions order, int page) {

        final List<Object> variables = new LinkedList<>();
        final String orderQuery = getOrder(order);
        final String offsetAndLimitQuery = getOffsetAndLimitQuery(page, ITEMS_PER_PAGE, variables);
        final String filteredIdsQuery = "select u.id_user from user_t u where u.id_user in (select ur.id_user from roles ur where ur.role_t = 'BANNED') " + getNativeOrder(order) + offsetAndLimitQuery;

        Query filteredIdsNativeQuery = em.createNativeQuery(filteredIdsQuery);

        setQueryVariables(filteredIdsNativeQuery, variables);
        @SuppressWarnings("unchecked") final List<Long> filteredIds = ((List<Number>) filteredIdsNativeQuery.getResultList())
                .stream()
                .map(Number::longValue)
                .collect(Collectors.toList());
        if (filteredIds.isEmpty())
            return Collections.emptyList();

        StringBuilder finalQuery = new StringBuilder("from User u where u.idUser in :filteredIds");
        finalQuery.append(getOrder(order));

        return em.createQuery(finalQuery.toString(), User.class)
                .setParameter("filteredIds", filteredIds)
                .getResultList();
    }

    private String getOrder(AdminPanelOptions order) {
        StringBuilder stringBuilder = new StringBuilder();
        if (order == AdminPanelOptions.BY_ID) {
            stringBuilder.append(" order by u.idUser asc");
        } else {
            stringBuilder.append(" order by u.username asc");
        }
        return stringBuilder.toString();
    }

    private String getNativeOrder(AdminPanelOptions order) {
        StringBuilder stringBuilder = new StringBuilder();
        if (order == AdminPanelOptions.BY_ID) {
            stringBuilder.append(" order by u.id_user asc");
        } else {
            stringBuilder.append(" order by u.username asc");
        }
        return stringBuilder.toString();
    }

    @Override
    public long totalBannedUsers() {

        final String query = "select count(distinct u) as total from User u WHERE u in (SELECT ur.user FROM UserRole ur WHERE ur.role = 'BANNED')";
        Query nativeQuery = em.createQuery(query);
        return (Long) nativeQuery.getSingleResult();
    }

    @Override
    public long totalAdminUsers() {
        final String query = "select count(distinct u) as total from User u WHERE u in (SELECT ur.user FROM UserRole ur WHERE ur.role = 'ADMIN')";
        Query nativeQuery = em.createQuery(query);
        return (Long) nativeQuery.getSingleResult();
    }

    private String getOffsetAndLimitQuery(int page, int itemsPerPage, List<Object> variables) {
        final StringBuilder offsetAndLimitQuery = new StringBuilder();
        if (page > 0) {
            offsetAndLimitQuery.append(" OFFSET ? ");
            variables.add(page * itemsPerPage);
        }
        if (itemsPerPage > 0) {
            offsetAndLimitQuery.append(" LIMIT ? ");
            variables.add(itemsPerPage);
        }
        return offsetAndLimitQuery.toString();
    }

    private void setQueryVariables(Query query, Collection<Object> variables) {
        int i = 1;
        for (Object variable : variables) {
            query.setParameter(i, variable);
            i++;
        }
    }

    @Override
    public Collection<User> searchUsers(String query, Boolean highlighted, Boolean admins, OrderOptionsUsers queryOrder, int page) {
        final List<Object> variables = new LinkedList<>();
        final StringBuilder filteredIdsQuery = new StringBuilder();
        final StringBuilder queryBuilder = new StringBuilder();
        if(query == null){
            query = "";
        }
        queryBuilder.append("%").append(query.toLowerCase()).append("%");
        variables.add(queryBuilder.toString());

        filteredIdsQuery.append("select id_user from user_t u where username LIKE ?");

        if (highlighted != null && highlighted) {
            filteredIdsQuery.append(" and id_user in (select id_user from recipe where is_highlighted = true)");
        }
        if (admins != null && admins) {
            filteredIdsQuery.append(" and id_user in (select id_user from roles where role_t = 'ADMIN')");
        }

        filteredIdsQuery.append(getOrderQuery(queryOrder)).append(getOffsetAndLimitQuery(page, ITEMS_PER_PAGE_SEARCH, variables));

        Query filteredIdsNativeQuery = em.createNativeQuery(filteredIdsQuery.toString());
        setQueryVariables(filteredIdsNativeQuery, variables);
        @SuppressWarnings("unchecked") final List<Long> filteredIds = ((List<Number>) filteredIdsNativeQuery.getResultList())
                .stream()
                .map(Number::longValue)
                .collect(Collectors.toList());
        if (filteredIds.isEmpty())
            return Collections.emptyList();

        StringBuilder finalQuery = new StringBuilder();
        finalQuery.append("from User u where id in :filteredIds ").append(getOrderQuery(queryOrder));

        return em.createQuery(finalQuery.toString(), User.class)
                .setParameter("filteredIds", filteredIds)
                .getResultList();


    }

    private String getOrderQuery(OrderOptionsUsers queryOrder) {
        StringBuilder query = new StringBuilder();
        if (queryOrder != null) {
            if (queryOrder == OrderOptionsUsers.NAME_ASC) {
                query.append("order by u.username asc");
            } else if (queryOrder == OrderOptionsUsers.NAME_DESC) {
                query.append("order by u.username desc");
            }
        }
        return query.toString();
    }


    @Override
    public Long searchUsersCount(String query, Boolean highlighted, Boolean admins, int page) {
        List<Object> variables = new LinkedList<>();
        final StringBuilder filteredIdsQuery = new StringBuilder();
        final StringBuilder queryBuilder = new StringBuilder();
        if(query == null){
            query = "";
        }
        queryBuilder.append("%").append(query.toLowerCase()).append("%");
        variables.add(queryBuilder.toString());
        filteredIdsQuery.append("select count(*) as total from User u where u.username like ?");
        if (highlighted != null && highlighted) {
            filteredIdsQuery.append(" and u in (select r.user from Recipe r where r.isHighlighted = true)");
        }
        if (admins != null && admins) {
            filteredIdsQuery.append(" and u in (select ur from UserRole ur where ur.role = 'ADMIN')");
        }
        Query nativeQuery = em.createQuery(filteredIdsQuery.toString());
        setQueryVariables(nativeQuery, variables);
        return (Long) nativeQuery.getSingleResult();

    }

    @Override
    public long totalHighlightedRecipes(User user) {
        return em.createQuery("select count(*) as total from Recipe r where r.isHighlighted=true and r.user = :user",Long.class).setParameter("user",user).getSingleResult();
    }

    @Override
    public void deleteReport(Long idReport) {
        final TypedQuery<Reports> query = em.createQuery("SELECT r FROM Reports r WHERE r.idReport = :id",Reports.class);
        query.setParameter("id",idReport);
        Reports report = em.find(Reports.class, query.getResultList().get(0).getIdReport());
        em.remove(report);
    }

    @Override
    public void deleteUserReports(User user) {
        final TypedQuery<Reports> query = em.createQuery("SELECT r FROM Reports r WHERE r.reportedUser = :user",Reports.class);
        query.setParameter("user",user);
        Reports aux;
        for(Reports r : query.getResultList()){
            aux = em.find(Reports.class, r.getIdReport());
            em.remove(aux);
        }
    }

    @Override
    public Reports reportUser(String desc, User reporter, User reported, Comment comment) {
        Reports report = new Reports(reported, reporter, desc, comment);
        em.persist(report);
        return report;
    }

    @Override
    public long totalReports() {
        final String query = "select count(distinct r) as total from Reports r";
        Query nativeQuery = em.createQuery(query);
        return (Long) nativeQuery.getSingleResult();
    }

    private String getOrderReports(ReportsPanelOptions order) {
        StringBuilder stringBuilder = new StringBuilder();
        if (order == ReportsPanelOptions.BY_REPORTER) {
            stringBuilder.append(" order by r.reporterUser asc");
        }else if (order == ReportsPanelOptions.BY_REPORTED){
           stringBuilder.append(" order by r.reportedUser asc");
        } else if (order == ReportsPanelOptions.NEWEST) {
            stringBuilder.append(" order by r.date desc");
        }else{
            stringBuilder.append(" order by r.date asc");
        }
        return stringBuilder.toString();
    }

    private String getNativeOrderReports(ReportsPanelOptions order) {
        StringBuilder stringBuilder = new StringBuilder();
        if (order == ReportsPanelOptions.BY_REPORTER) {
            stringBuilder.append(" order by r.reporter_user_id asc");
        } else if (order == ReportsPanelOptions.BY_REPORTED){
            stringBuilder.append(" order by r.reported_user_id asc");
        }else if (order == ReportsPanelOptions.NEWEST){
            stringBuilder.append(" order by r.date desc");
        }else{
            stringBuilder.append(" order by r.date asc");
        }
        return stringBuilder.toString();
    }

    @Override
    public Collection<Reports> getReports(ReportsPanelOptions queryOrder, int page) {
        final List<Object> variables = new LinkedList<>();
        final String orderQuery = getOrderReports(queryOrder);
        final String offsetAndLimitQuery = getOffsetAndLimitQuery(page, ITEMS_PER_PAGE, variables);
        final String filteredIdsQuery = "select r.id_report from reports r " + getNativeOrderReports(queryOrder) + offsetAndLimitQuery;
        Query filteredIdsNativeQuery = em.createNativeQuery(filteredIdsQuery);

        setQueryVariables(filteredIdsNativeQuery, variables);
        @SuppressWarnings("unchecked") final List<Long> filteredIds = ((List<Number>) filteredIdsNativeQuery.getResultList())
                .stream()
                .map(Number::longValue)
                .collect(Collectors.toList());
        if (filteredIds.isEmpty())
            return Collections.emptyList();

        StringBuilder finalQuery = new StringBuilder("from Reports r where r.idReport in :filteredIds");
        finalQuery.append(getOrderReports(queryOrder));

        return em.createQuery(finalQuery.toString(), Reports.class)
                .setParameter("filteredIds", filteredIds)
                .getResultList();
    }
}

