package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.interfaces.exceptions.DuplicateUserException;
import ar.edu.itba.paw.models.*;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserDao {
    Optional<User> findById(long id);
    User create(String username, String email, String password) throws DuplicateUserException;
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    List<User> findBannedUsers(AdminPanelOptions order, int page);
    List<User> findAdmins(AdminPanelOptions order, int page);
    long totalBannedUsers();
    long totalAdminUsers();
    Long searchUsersCount(String query, Boolean highlighted, Boolean admins, int page);
    Collection<User> searchUsers(String query, Boolean highlighted, Boolean admins, OrderOptionsUsers queryOrder, int page);
    ImageUser findImageById(long id);
    long totalHighlightedRecipes(User user);
    Reports reportUser(String desc, User reporter, User reported, Comment comment);
    void deleteReport(Long idReport);
    long totalReports();
    void deleteUserReports(User user);

    Collection<Reports> getReports(ReportsPanelOptions queryOrder, int page);
}

