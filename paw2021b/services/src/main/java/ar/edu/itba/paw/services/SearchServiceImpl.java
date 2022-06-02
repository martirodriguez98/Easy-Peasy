package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.RecipeDao;
import ar.edu.itba.paw.interfaces.SearchService;
import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    private RecipeDao recipeDao;

    @Autowired
    private UserDao userDao;

    private final OrderOptions DEFAULT_ORDER = OrderOptions.valueOf("DATE_DESC");
    private final OrderOptionsUsers DEFAULT_ORDER_USER = OrderOptionsUsers.valueOf("NAME_ASC");
    final int ITEMS_PER_PAGE = 12;

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchServiceImpl.class);

    @Override
    public PaginatedResult<Recipe> searchRecipes(String query, List<Integer> categoriesQuery,int difficulty, String order, int page,  boolean isHighlighted, int pageSize) {
        LOGGER.debug("Searching recipes");
        OrderOptions queryOrder;
        if (!OrderOptions.contains(order)) {
            queryOrder = DEFAULT_ORDER;
            order = DEFAULT_ORDER.name();
        } else {
            queryOrder = OrderOptions.valueOf(order);
        }
        if(categoriesQuery == null){
            categoriesQuery = new LinkedList<>();
        }
        final int totalRecipes = recipeDao.getSearchResultCount(query,categoriesQuery,difficulty, isHighlighted);
        final Collection<Recipe> recipes = recipeDao.searchRecipes(query, categoriesQuery,difficulty, queryOrder, page, isHighlighted, pageSize);
        LOGGER.info("Found {} recipes", totalRecipes);

        PaginatedResult<Recipe> paginatedResult = new PaginatedResult<>(order, query, categoriesQuery,difficulty, page, pageSize, totalRecipes, recipes);
        paginatedResult.setrHighlighted(isHighlighted);
        if(page >= paginatedResult.getTotalPages()){
            paginatedResult = new PaginatedResult<>(order, query, categoriesQuery,difficulty, paginatedResult.getTotalPages()-1, pageSize, totalRecipes, recipes);
            paginatedResult.setrHighlighted(isHighlighted);
        }
        return paginatedResult;
    }

    @Override
    public PaginatedResult<User> searchUsers(String query,Boolean highlighted, Boolean admins, String order, int page){
        LOGGER.debug("Searching users");
        OrderOptionsUsers queryOrder;
        if(!OrderOptionsUsers.contains(order)){
            queryOrder = DEFAULT_ORDER_USER;
            order = DEFAULT_ORDER_USER.name();
        }else{
            queryOrder = OrderOptionsUsers.valueOf(order);
        }
        final long totalUsers = userDao.searchUsersCount(query, highlighted,admins,page);
        final Collection<User> users = userDao.searchUsers(query, highlighted,admins,queryOrder, page);
        LOGGER.info("Found {} users",totalUsers);
        PaginatedResult<User> paginatedResult = new PaginatedResult<>(order,query,null,0,page,ITEMS_PER_PAGE,totalUsers,users);
        if(page >= paginatedResult.getTotalPages()){
            paginatedResult = new PaginatedResult<>(order, query, null,0, paginatedResult.getTotalPages()-1, ITEMS_PER_PAGE, totalUsers, users);
        }
        paginatedResult.setAdmins(admins);
        paginatedResult.setHighlighted(highlighted);
        paginatedResult.setOrderU(order);


        return paginatedResult;
    }
}
