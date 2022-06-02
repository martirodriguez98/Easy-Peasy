package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.CommentDao;
import ar.edu.itba.paw.models.Comment;
import ar.edu.itba.paw.models.Recipe;
import ar.edu.itba.paw.models.User;
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
public class CommentDaoImpl implements CommentDao {

    @PersistenceContext
    private EntityManager em;

    private final int ITEMS_PER_PAGE = 6;

    private static final Logger LOGGER = LoggerFactory.getLogger(CommentDaoImpl.class);

    @Override
    public Comment create(Recipe recipe, User user, String commentDesc) {
        Comment comment = new Comment(recipe, user, commentDesc, LocalDate.now());
        em.persist(comment);
        return comment;
    }

    @Override
    public Optional<Comment> findById(long id) {
        final String query = "SELECT c FROM Comment c WHERE c.idComment = :id";
        LOGGER.debug("Executing query {} in comment dao", query);
        TypedQuery<Comment> resultQuery = em.createQuery(query,Comment.class);
        resultQuery.setParameter("id",id);
        return resultQuery.getResultList().stream().findFirst();
    }


    @Override
    public Collection<Comment> findByRecipeId(Recipe recipe, int page) {
        final List<Object> variables = new LinkedList<>();
        variables.add(recipe.getIdRecipe());
        final String offsetAndLimitQuery = getOffsetAndLimitQuery(page, ITEMS_PER_PAGE, variables);

        final String filteredIdsQuery = " select id_comment FROM comment c where id_recipe = ? order by c.date_created desc,id_comment desc " + offsetAndLimitQuery;

        Query filteredIdsNativeQuery = em.createNativeQuery(filteredIdsQuery);

        setQueryVariables(filteredIdsNativeQuery, variables);

        @SuppressWarnings("unchecked") final List<Long> filteredIds = ((List<Number>) filteredIdsNativeQuery.getResultList())
                .stream()
                .map(Number::longValue)
                .collect(Collectors.toList());
        if(filteredIds.isEmpty())
            return Collections.emptyList();

        return em.createQuery("from Comment c where id in :filteredIds order by date_created desc, id desc", Comment.class)
                .setParameter("filteredIds", filteredIds)
                .getResultList();
    }

    public Long findByRecipeIdCount(Recipe recipe) {
        List<Object> variables = new LinkedList<>();
        variables.add(recipe.getIdRecipe());
        final String query = "select count(*) as total from Comment where id_recipe = ?";
        Query nativeQuery = em.createQuery(query);
        setQueryVariables(nativeQuery,variables);
        return (Long) nativeQuery.getSingleResult();
    }


    @Override
    public void delete(long id) {
        final TypedQuery<Comment> query = em.createQuery("SELECT c FROM Comment c WHERE c.idComment = :id",Comment.class);
        query.setParameter("id",id);
        Comment comment = em.find(Comment.class,query.getResultList().get(0).getIdComment());
        em.remove(comment);
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
}

