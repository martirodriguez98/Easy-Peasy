package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.interfaces.exceptions.DuplicateUserException;
import ar.edu.itba.paw.models.*;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Optional;

public interface UserService {
    int validateData(String email, String username, String password,String repeatPass, BindingResult errors);
    Optional<User> findById(long id);
    User create(String username, String email, String password) throws DuplicateUserException;
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    void changeAvatar(long idUser, byte[] avatar, String mimeType);
    ImageUser findImageById(long id);
    boolean resendVerificationToken(String username);
    long verifyAccount(String token);
    void sendPasswordToken(String email);
    void resetPassword(String token, String password);
    List<UserRole> getRoles(User user);
    void changeUser(String username, String password, long idUser) throws Exception;
    boolean validatePasswordToken(String tokenStr);
    boolean isAdmin(User user);
    boolean isBanned(User user);
    public boolean isManager(User user);
    void ban(User user);
    boolean banUser(Long idUser);
    void unban(User user);
    boolean unbanUser(Long idUser);
    PaginatedResult<User> findBannedUsers(String order, int page);
    PaginatedResult<User> findAdmins(String order, int page);
    long totalHighlightedRecipes(User user);
    String getUserOrderString(String order);
    Optional<User> getUserByRefreshToken(String token);
    SessionRefreshToken getSessionRefreshToken(User user);
    Reports reportUser(String desc, User reporter, User reported, Comment comment);
    void deleteReport(Long idReport);
    PaginatedResult<Reports> getReports(String order, int page);
    void deleteUserReports(User user);
}

