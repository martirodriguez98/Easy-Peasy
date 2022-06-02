package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfaces.RecipeDao;
import ar.edu.itba.paw.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class RecipeDaoImpl implements RecipeDao {

    @PersistenceContext
    private EntityManager em;

    private static final Logger LOGGER = LoggerFactory.getLogger(RecipeDaoImpl.class);

    private final int ITEMS_PER_PAGE_SEARCH = 12;

    private final int ITEMS_PER_PAGE_PROFILE = 6;

    private final int ITEMS_PER_PAGE_LANDING = 4;

    private final int ITEMS_PER_PAGE_SUGGESTION = 4;

    private static final Map<OrderOptions, String> ORDER_OPTIONS = new EnumMap<>(OrderOptions.class);

    static {
        ORDER_OPTIONS.put(OrderOptions.TITLE_ASC, "recipeTitle asc");
        ORDER_OPTIONS.put(OrderOptions.TITLE_DESC, "recipeTitle desc");
        ORDER_OPTIONS.put(OrderOptions.DATE_ASC, "recipeDateCreated asc");
        ORDER_OPTIONS.put(OrderOptions.DATE_DESC, "recipeDateCreated desc");
        ORDER_OPTIONS.put(OrderOptions.LIKES_ASC,"asc");
        ORDER_OPTIONS.put(OrderOptions.LIKES_DESC,"desc");
    }

    @Override
    public Recipe create(String recipeTitle, String recipeDesc, String recipeSteps, User user, int recipeDifficulty, String recipeTime) {
        final boolean isHighlighted = false;
        final Recipe recipe = new Recipe(recipeTitle, recipeDesc, recipeSteps, user, recipeDifficulty, isHighlighted, recipeTime, LocalDate.now());
        em.persist(recipe);
        return recipe;
    }

    @Override
    public void delete(Recipe recipe, User user) {
        Image image;
        for(Image i : recipe.getImages()){
            image = em.find(Image.class,i.getImageId());
            em.remove(image);
        }
        RecipeCategory category;
        for(RecipeCategory c : recipe.getCategories()){
            category = em.find(RecipeCategory.class, c.getId());
            em.remove(category);
        }
        Ingredient ing;
        for(Ingredient i : recipe.getIngredients()){
            ing = em.find(Ingredient.class,i.getIdIngredient());
            em.remove(ing);
        }
        LikedRecipes lr;
        for(LikedRecipes l : recipe.getLikes()){
            lr = em.find(LikedRecipes.class,l.getId());
            em.remove(lr);
        }
        DislikedRecipes dr;
        for(DislikedRecipes d : recipe.getDislikes()){
            dr = em.find(DislikedRecipes.class,d.getId());
            em.remove(dr);
        }

        Recipe r = em.find(Recipe.class,recipe.getIdRecipe());
        em.remove(r);
    }

    @Override
    public List<Recipe> findAllRecipes() {
        final TypedQuery<Recipe> query = em.createQuery("SELECT r FROM Recipe r",Recipe.class);
        return query.getResultList();
    }

    @Override
    public Collection<Recipe> findByAuthorId(User user, int page) {
        final List<Object> variables = new LinkedList<>();
        variables.add(user.getIdUser());
        final String offsetAndLimitQuery = getOffsetAndLimitQuery(page, ITEMS_PER_PAGE_PROFILE, variables);

        final String filteredIdsQuery = "select id_recipe from recipe r where id_user = ? order by r.recipe_date_created desc" + offsetAndLimitQuery;

        Query filteredIdsNativeQuery = em.createNativeQuery(filteredIdsQuery);
        setQueryVariables(filteredIdsNativeQuery, variables);

        @SuppressWarnings("unchecked") final List<Long> filteredIds = ((List<Number>) filteredIdsNativeQuery.getResultList())
                .stream()
                .map(Number::longValue)
                .collect(Collectors.toList());
        if(filteredIds.isEmpty())
            return Collections.emptyList();

        return em.createQuery("from Recipe r where id in :filteredIds order by recipe_date_created", Recipe.class)
                .setParameter("filteredIds", filteredIds)
                .getResultList();
    }


    @Override
    public Optional<Recipe> findById(long id) {
        Recipe recipe = em.find(Recipe.class,id);
        return Optional.ofNullable(recipe);
    }

    @Override
    public Collection<Recipe> findFavouritesByUserId(User user, int page) {

        final List<Object> variables = new LinkedList<>();
        variables.add(user.getIdUser());
        final String offsetAndLimitQuery = getOffsetAndLimitQuery(page, ITEMS_PER_PAGE_PROFILE, variables);

        final String filteredIdsQuery = "select id_recipe from recipe r where id_recipe in (select f.id_recipe from favourites f where f.id_user = ?) order by r.recipe_date_created" + offsetAndLimitQuery;

        Query filteredIdsNativeQuery = em.createNativeQuery(filteredIdsQuery);
        setQueryVariables(filteredIdsNativeQuery, variables);

        @SuppressWarnings("unchecked") final List<Long> filteredIds = ((List<Number>) filteredIdsNativeQuery.getResultList())
                .stream()
                .map(Number::longValue)
                .collect(Collectors.toList());
        if(filteredIds.isEmpty())
            return Collections.emptyList();

        return em.createQuery("from Recipe r where id in :filteredIds order by recipe_date_created", Recipe.class)
                .setParameter("filteredIds", filteredIds)
                .getResultList();
    }

    @Override
    public void favoriteRecipe(Recipe recipe, User user) {
        final TypedQuery<Favourites> isFaved = em.createQuery("SELECT f FROM Favourites f where f.recipe = :recipe and f.user = :user", Favourites.class);
        isFaved.setParameter("recipe",recipe);
        isFaved.setParameter("user",user);
        if(isFaved.getResultList().isEmpty()){
            //add to favourites
            final Favourites f = new Favourites(recipe,user);
            em.persist(f);
        }else{
            //remove from favourites
            TypedQuery<Favourites> favRecipe = em.createQuery("select f from Favourites f where f.user = :user and f.recipe = :recipe", Favourites.class);
            favRecipe.setParameter("user",user);
            favRecipe.setParameter("recipe",recipe);
            Favourites f = em.find(Favourites.class, favRecipe.getResultList().get(0).getId());
            em.remove(f);
        }
    }

    @Override
    public long getDislikes(Recipe recipe) {
        LOGGER.info("Retrieving dislikes");
        TypedQuery<DislikedRecipes> query = em.createQuery("select dr from DislikedRecipes dr where dr.recipe = :recipe",DislikedRecipes.class);
        query.setParameter("recipe",recipe);
        return query.getResultList().size();
    }

    @Override
    public long getLikes(Recipe recipe) {
        LOGGER.info("Retrieving likes");
        TypedQuery<LikedRecipes> query = em.createQuery("select distinct lr from LikedRecipes lr where lr.recipe = :recipe",LikedRecipes.class);
        query.setParameter("recipe",recipe);
        return query.getResultList().size();
    }

    @Override
    public boolean isFavorited(Recipe recipe, User user) {
        TypedQuery<Favourites> query = em.createQuery("select f from Favourites f where f.recipe = :recipe and f.user = :user",Favourites.class);
        query.setParameter("recipe",recipe);
        query.setParameter("user",user);
        return !query.getResultList().isEmpty();
    }

    @Override
    public void dislikeRecipe(Recipe recipe, User user) {
        final TypedQuery<DislikedRecipes> isDisliked = em.createQuery("SELECT dr from DislikedRecipes dr where dr.recipe = :recipe and dr.user = :user", DislikedRecipes.class);
        isDisliked.setParameter("recipe",recipe);
        isDisliked.setParameter("user",user);
        if(isDisliked.getResultList().isEmpty()){
            final DislikedRecipes dislikedRecipe = new DislikedRecipes(recipe,user);
            em.persist(dislikedRecipe);
            final TypedQuery<LikedRecipes> isLiked = em.createQuery("SELECT lr from LikedRecipes lr where lr.recipe = :recipe and lr.user = :user", LikedRecipes.class);
            isLiked.setParameter("recipe",recipe);
            isLiked.setParameter("user",user);
            if(!isLiked.getResultList().isEmpty()){
                LOGGER.info("Disliking recipe liked");
                removeLikeAux(recipe, user);
            }
        }else {
            removeDislikeAux(recipe, user);
        }
    }

    @Override
    public void likeRecipe(Recipe recipe, User user) {
        final TypedQuery<LikedRecipes> isLiked = em.createQuery("SELECT lr from LikedRecipes lr where lr.recipe = :recipe and lr.user = :user", LikedRecipes.class);
        isLiked.setParameter("recipe",recipe);
        isLiked.setParameter("user",user);
        if(isLiked.getResultList().isEmpty()){
            final LikedRecipes likedRecipe = new LikedRecipes(recipe,user);
            em.persist(likedRecipe);
            recipe.setLikesCount(recipe.getLikesCount()+1);
            final TypedQuery<DislikedRecipes> isDisliked = em.createQuery("SELECT dr from DislikedRecipes dr where dr.recipe = :recipe and dr.user = :user", DislikedRecipes.class);
            isDisliked.setParameter("recipe",recipe);
            isDisliked.setParameter("user",user);
            if(!isDisliked.getResultList().isEmpty()){
                LOGGER.info("Liking recipe disliked");
                removeDislikeAux(recipe, user);
            }
        }else {
            removeLikeAux(recipe, user);
        }
    }

    private void removeLikeAux(Recipe recipe, User user) {
        final TypedQuery<LikedRecipes> query = em.createQuery("select lr from LikedRecipes lr where lr.user = :user and lr.recipe = :recipe", LikedRecipes.class);
        query.setParameter("recipe",recipe);
        query.setParameter("user",user);
        LikedRecipes likedRecipe = em.find(LikedRecipes.class, query.getResultList().get(0).getId());
        em.remove(likedRecipe);
        recipe.setLikesCount(recipe.getLikesCount()-1);
    }

    private void removeDislikeAux(Recipe recipe, User user) {
        final TypedQuery<DislikedRecipes> query = em.createQuery("select dr from DislikedRecipes dr where dr.user = :user and dr.recipe = :recipe", DislikedRecipes.class);
        query.setParameter("recipe",recipe);
        query.setParameter("user",user);
        DislikedRecipes dislikedRecipe = em.find(DislikedRecipes.class, query.getResultList().get(0).getId());
        em.remove(dislikedRecipe);
    }

    @Override
    public boolean isLiked(Recipe recipe, User user) {
        TypedQuery<LikedRecipes> query = em.createQuery("select lr from LikedRecipes lr where lr.recipe = :recipe and lr.user = :user", LikedRecipes.class);
        query.setParameter("recipe",recipe);
        query.setParameter("user",user);
        return !query.getResultList().isEmpty();
    }

    @Override
    public boolean isDisliked(Recipe recipe, User user) {
        TypedQuery<DislikedRecipes> query = em.createQuery("select dr from DislikedRecipes dr where dr.recipe = :recipe and dr.user = :user", DislikedRecipes.class);
        query.setParameter("recipe",recipe);
        query.setParameter("user",user);
        return !query.getResultList().isEmpty();
    }

    @Override
    public List<Recipe> getSimilarRecipes(Recipe recipe) {

        List<String> titleTokens = new ArrayList<String>(Arrays.asList(recipe.getRecipeTitle().split(" ")));
        int j=0;
        for(String token : titleTokens){
            if(token.length() > 3) {
                titleTokens.set(j, "%" + token + "%");
                j++;
            }
        }
        StringBuilder titleSearch = new StringBuilder().append("(select r1.id_recipe from recipe r1 where r1.id_recipe in (select id_recipe from recipe where r1.id_recipe != ? and (lower(r1.recipe_title) like ?");
        if(titleTokens.size()>1){
            for(int i=0 ; i<titleTokens.size() - 1 ; i++){
                titleSearch.append(" OR lower(r1.recipe_title) like ?");
            }
        }
        String similarCatQuery = "select id_recipe from recipe_categories rc2 where rc2.id_recipe != ? and rc2.category in (select rc1.category from recipe_categories rc1 where rc1.id_recipe = ?)";
        String auxQuery = titleSearch.append(")) union ").append(similarCatQuery).append(" LIMIT ?)").toString();
        Query nativeQuery = em.createNativeQuery(auxQuery);
        List<Object> variables = new ArrayList<>();
        variables.add(recipe.getIdRecipe()); //first comparison in title
        variables.addAll(titleTokens);
        variables.add(recipe.getIdRecipe()); //first comparison in categories
        variables.add(recipe.getIdRecipe()); //second comparison in categories
        variables.add(ITEMS_PER_PAGE_SUGGESTION);
        setQueryVariables(nativeQuery, variables);

        @SuppressWarnings("unchecked") final List<Long> filteredIds = ((List<Number>) nativeQuery.getResultList())
                .stream()
                .map(Number::longValue)
                .collect(Collectors.toList());
        if(filteredIds.isEmpty()) {
            return Collections.emptyList();
        }
        List<Recipe> aux = em.createQuery("from Recipe r where r.idRecipe in :filteredIds order by r.recipeDateCreated", Recipe.class)
                .setParameter("filteredIds", filteredIds)
                .getResultList();
        return aux;
    }

    @Override
    public Long getFindFavouritesCount(User user) {
        List<Object> variables = new LinkedList<>();
        variables.add(user);
        final String query = "select count(*) as total from Recipe r where r in (select f.recipe from Favourites f where f.user = ?)";
        Query nativeQuery = em.createQuery(query);
        setQueryVariables(nativeQuery,variables);
        return (Long) nativeQuery.getSingleResult();
    }

    @Override
    public Long getFindByAuthorCount(User user) {
        List<Object> variables = new LinkedList<>();
        variables.add(user.getIdUser());
        final String query = "select count(*) as total from Recipe where id_user = ?";
        Query nativeQuery = em.createQuery(query);
        setQueryVariables(nativeQuery,variables);
        return (Long) nativeQuery.getSingleResult();
    }

    @Override
    public Collection<Recipe> searchRecipes(String query, List<Integer> categoriesQuery, int difficulty, OrderOptions order, int page, boolean isHighlighted, int pageSize) {
        List<Recipe> getSearchRecipes = getSearchRecipesAux(query,categoriesQuery,difficulty, isHighlighted);
        StringBuilder paginatedResultString = new StringBuilder();
        paginatedResultString.append("select distinct r from Recipe r where r in :list");
        if(order != OrderOptions.LIKES_DESC && order != OrderOptions.LIKES_ASC){
            paginatedResultString.append(" order by r.");
            paginatedResultString.append(ORDER_OPTIONS.get(order));
        }else{
            paginatedResultString.append(" order by r.likesCount");
            if(order == OrderOptions.LIKES_ASC){
                paginatedResultString.append(" asc)");
            }else{
                paginatedResultString.append(" desc)");
            }
        }
        paginatedResultString.append(",r.idRecipe desc");
        if(!getSearchRecipes.isEmpty()) {
            TypedQuery<Recipe> paginatedResult = em.createQuery(paginatedResultString.toString(), Recipe.class);
            paginatedResult.setParameter("list", getSearchRecipes);

            if (page > 0) {
                paginatedResult.setFirstResult(page * pageSize);
            }
            paginatedResult.setMaxResults(pageSize);
            return paginatedResult.getResultList();
        }
        return new LinkedList<>();
    }

    private List<Recipe> getSearchRecipesAux(String query, List<Integer> categoriesQuery, int difficulty, boolean isHighlighted){
        List<Recipe> result = new LinkedList<>();
        List<Object> searchvariables = new LinkedList<>();
        TypedQuery<Recipe> searchBarQuery = em.createQuery("select r from Recipe r where (LOWER(r.recipeDesc) LIKE ? OR LOWER(r.recipeTitle) LIKE ? OR LOWER(r.user.username) LIKE ? OR r in (select i.recipe from Ingredient i where LOWER(i.ingredientName) LIKE ? )) ",Recipe.class);
        if(query==null){
            query = "";
        }
        query = "%%" + query.replace("%", "\\%").replace("_", "\\_").toLowerCase() + "%%";
        searchvariables.add(query);
        searchvariables.add(query);
        searchvariables.add(query);
        searchvariables.add(query);
        setQueryVariables(searchBarQuery,searchvariables);
        List<Recipe> searchBarResult = searchBarQuery.getResultList();

        result = searchBarResult;

        List<TypedQuery<Recipe>> categoriesQueries = new LinkedList<>();
        for(Integer cId : categoriesQuery){
            TypedQuery<Recipe> q = em.createQuery("select distinct r from Recipe r join r.categories c where :id in c.category", Recipe.class);
            q.setParameter("id",cId);
            categoriesQueries.add(q);
        }
        List<Recipe> categoriesResult;
        if(!categoriesQueries.isEmpty()) {
            categoriesResult = categoriesQueries.get(0).getResultList();
            int i = 1;
            for (TypedQuery<Recipe> tq : categoriesQueries) {
                if (i != 1)
                    categoriesResult.retainAll(tq.getResultList());
                i++;
            }
            result.retainAll(categoriesResult);
        }
        if(difficulty != 0){
            TypedQuery<Recipe> difficultyQuery = em.createQuery("select r from Recipe r where r.recipeDifficulty = :difficulty",Recipe.class);
            difficultyQuery.setParameter("difficulty",difficulty);
            result.retainAll(difficultyQuery.getResultList());
        }
        if(isHighlighted) {
            TypedQuery<Recipe> highlightedQuery = em.createQuery("SELECT r FROM Recipe r where r.isHighlighted = true", Recipe.class);
            result.retainAll(highlightedQuery.getResultList());
        }
        return result;
    }



    @Override
    public Integer getSearchResultCount(String query, List<Integer> categoriesQuery, int difficulty, boolean isHighlighted) {
        return getSearchRecipesAux(query,categoriesQuery,difficulty, isHighlighted).size();
    }

    private void setQueryVariables(Query query, List<Object> variables){
        int i=1;
        for(Object variable : variables){
            query.setParameter(i,variable);
            i++;
        }
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
    public void highlightRecipe(Recipe recipe) {
        Recipe foundRecipe = em.find(Recipe.class, recipe.getIdRecipe());
        foundRecipe.setIsHighlighted(!foundRecipe.isHighlighted());
    }


}