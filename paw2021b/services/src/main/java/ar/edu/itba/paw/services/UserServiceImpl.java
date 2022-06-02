package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.*;
import ar.edu.itba.paw.interfaces.exceptions.DuplicateUserException;
import ar.edu.itba.paw.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private VerificationTokenDao verificationTokenDao;
    @Autowired
    private PasswordTokenDao passwordTokenDao;
    @Autowired
    private MailService mailService;
    @Autowired
    MessageSource messageSource;
    @Autowired
    private SessionRefreshTokenDao sessionRefreshTokenDao;

    private Locale locale;
    private final int ITEMS_PER_PAGE = 6;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    private final AdminPanelOptions DEFAULT_ORDER = AdminPanelOptions.BY_USERNAME;
    private final ReportsPanelOptions DEFAULT_ORDER_REPORTS = ReportsPanelOptions.BY_REPORTED;
    public Optional<User> findById(long id) {
        return this.userDao.findById(id);
    }

    @Transactional
    @Override
    public int validateData(String email, String username, String password, String repeatPass, BindingResult errors) {
        email = email.toLowerCase();
        username = username.toLowerCase();
        if (findByEmail(email).isPresent() && findByEmail(email).get().getUsername() == null) {
            changeUser(username, password, findByEmail(email).get().getIdUser());
            return 0;
        }
        if (findByEmail(email).isPresent()) {
            errors.rejectValue("email", "validation.user.email");
            return -1;
        }
        if (findByUsername(username).isPresent()) {
            errors.rejectValue("username", "validation.user.username");
            return -1;
        }
        if (!password.equals(repeatPass)) {
            errors.rejectValue("repeatPassword", "validation.user.passwordsDontMatch");
            return -1;
        }

        return 1;
    }


    @Transactional
    @Override
    public void changeUser(String username, String password, long idUser) {
        LOGGER.debug("Changing to USER UNVERIFIED");
        Optional<User> opUser = this.userDao.findById(idUser);
        if (opUser.isPresent()) {
            final User user = opUser.get();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(password));
            this.roleService.addRole(user, Role.USER);
            this.roleService.addRole(user, Role.UNVERIFIED);
            VerificationToken token = generateVerificationToken(user);
            sendVerificationToken(user, token);
        }
    }

    @Transactional
    @Override
    public User create(String username, String email, String password) throws DuplicateUserException {
        LOGGER.debug("Creating user {}", username);
        User user = this.userDao.create(username, email, passwordEncoder.encode(password));
        this.roleService.addRole(user, Role.USER);
        this.roleService.addRole(user, Role.UNVERIFIED);
        VerificationToken token = generateVerificationToken(user);
        sendVerificationToken(user, token);
        return user;
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return this.userDao.findByEmail(email);
    }

    @Transactional
    @Override
    public boolean resendVerificationToken(String username) {
        Optional<User> user;
        if ((user = findByUsername(username)).isPresent()) {
            LOGGER.info("Resending verification token");
            verificationTokenDao.removeTokenByUserId(user.get());
            VerificationToken token = generateVerificationToken(user.get());
            sendVerificationToken(user.get(), token);
        } else {
            return true;
        }
        return false;
    }


    private VerificationToken generateVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        return verificationTokenDao.generateVerificationToken(user, token, VerificationToken.generateExpirationDate());

    }

    private PasswordToken generatePasswordToken(User user) {
        String token = UUID.randomUUID().toString();
        return passwordTokenDao.generatePasswordToken(user, token, VerificationToken.generateExpirationDate());
    }

    private void sendVerificationToken(User user, VerificationToken token) {
        LOGGER.info("Sending verification token");
        mailService.sendVerificationEmail(user.getEmail(), token.getToken());
    }

    @Override
    public boolean banUser(Long idUser) {
        Optional<User> optionalUser = findById(idUser);
        if (!optionalUser.isPresent()) {
            return false;
        }
        LOGGER.debug("Banning user with id {}", optionalUser.get().getIdUser());
        ban(optionalUser.get());
        mailService.sendBannedEmail(optionalUser.get().getEmail());
        return true;
    }

    @Override
    public boolean unbanUser(Long idUser) {
        Optional<User> optionalUser = findById(idUser);
        if (!optionalUser.isPresent()) {
            return false;
        }
        LOGGER.debug("Unbanning user with id {}", optionalUser.get().getIdUser());
        unban(optionalUser.get());
        return true;
    }



    @Override
    public List<UserRole> getRoles(User user) {
        return roleService.getRolesById(user);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return this.userDao.findByUsername(username);
    }

    @Transactional
    @Override
    public void changeAvatar(long idUser, byte[] avatar, String mimeType) {
        LOGGER.info("Changing user avatar");
        Optional<User> opUser = this.userDao.findById(idUser);
        if (opUser.isPresent()) {
            final User user = opUser.get();
            user.setAvatar(avatar);
            user.setMimeType(mimeType);
        }
    }

    @Override
    public ImageUser findImageById(long id) {
        return this.userDao.findImageById(id);
    }

    @Transactional
    @Override
    public long verifyAccount(String token) {
        LOGGER.debug("Verifying");
        Optional<VerificationToken> verifToken = verificationTokenDao.getTokenByStringValue(token);
        if (!verifToken.isPresent())
            return -1;
        verificationTokenDao.removeTokenByUserId(verifToken.get().getUser());
        if (!verifToken.get().isValid()) {
            return -1;
        }
        roleService.addRole(verifToken.get().getUser(), Role.VERIFIED);
        roleService.removeRole(verifToken.get().getUser(), Role.UNVERIFIED);
        return verifToken.get().getUser().getIdUser();
    }

    @Transactional
    @Override
    public void sendPasswordToken(String email) {
        Optional<User> usr = userDao.findByEmail(email);
        if (usr.isPresent()) {
            passwordTokenDao.removeTokenByUserId(usr.get());
            PasswordToken token = generatePasswordToken(usr.get());
            mailService.sendPasswordResetEmail(email, token.getToken());
        }
    }

    @Transactional
    @Override
    public void resetPassword(String tokenStr, String password) {
        LOGGER.debug("Reset password");
        Optional<PasswordToken> token = passwordTokenDao.getTokenByStringValue(tokenStr);
        if (token.isPresent()) {
            User user = token.get().getUser();
            String newPass = passwordEncoder.encode(password);
            user.setPassword(newPass);
            passwordTokenDao.removeTokenByUserId(user);

        }
    }

    @Override
    public boolean validatePasswordToken(String tokenStr) {
        Optional<PasswordToken> token = passwordTokenDao.getTokenByStringValue(tokenStr);
        if (!token.isPresent())
            return false;
        if (token.get() != null && token.get().isValid())
            return true;
        return false;
    }

    @Override
    public boolean isAdmin(User user) {
        boolean test = false;
        List<UserRole> roles = getRoles(user);
        for (UserRole r : roles) {
            if (r.getRole() == Role.ADMIN)
                test = true;
        }
        return test;
    }

    @Override
    public boolean isManager(User user) {
        boolean test = false;
        List<UserRole> roles = getRoles(user);
        for (UserRole r : roles) {
            if (r.getRole() == Role.MANAGER)
                test = true;
        }
        return test;
    }

    @Override
    public boolean isBanned(User user) {
        boolean test = false;
        List<UserRole> roles = getRoles(user);
        for (UserRole r : roles) {
            if (r.getRole() == Role.BANNED)
                test = true;
        }
        return test;
    }

    @Transactional
    @Override
    public void ban(User user) {
        LOGGER.debug("Banning user with id: {}", user.getIdUser());
        boolean banned = false;
        List<UserRole> roles = getRoles(user);
        for (UserRole r : roles) {
            if (r.getRole() == Role.BANNED)
                banned = true;
        }
        if (!banned) {
            roleService.addRole(user, Role.BANNED);
            roleService.removeRole(user, Role.ADMIN);
        }
    }

    @Transactional
    @Override
    public void unban(User user) {
        LOGGER.debug("Unbanning user with id: {}", user.getIdUser());
        boolean banned = false;
        List<UserRole> roles = getRoles(user);
        for (UserRole r : roles) {
            if (r.getRole() == Role.BANNED)
                banned = true;
        }
        if (banned) {
            roleService.removeRole(user, Role.BANNED);
        }
    }

    @Override
    public PaginatedResult<User> findBannedUsers(String order, int page) {
        AdminPanelOptions queryOrder;
        if (!AdminPanelOptions.contains(order)) {
            queryOrder = DEFAULT_ORDER;
        } else {
            queryOrder = AdminPanelOptions.valueOf(order);
        }
        final long totalUsers = userDao.totalBannedUsers();
        final Collection<User> bannedUsers = userDao.findBannedUsers(queryOrder, page);
        PaginatedResult<User> paginatedResult = new PaginatedResult<>(page, ITEMS_PER_PAGE, totalUsers, 1, bannedUsers);
        if (page >= paginatedResult.getTotalPages()) {
            paginatedResult = new PaginatedResult<>(paginatedResult.getTotalPages() - 1, ITEMS_PER_PAGE, totalUsers, 1, bannedUsers);
        }
        return paginatedResult;
    }

    @Override
    public PaginatedResult<User> findAdmins(String order, int page) {
        AdminPanelOptions queryOrder;
        if (!AdminPanelOptions.contains(order)) {
            queryOrder = DEFAULT_ORDER;
        } else {
            queryOrder = AdminPanelOptions.valueOf(order);
        }
        final long totalUsers = userDao.totalAdminUsers();
        final Collection<User> adminUsers = userDao.findAdmins(queryOrder, page);
        PaginatedResult<User> paginatedResult = new PaginatedResult<>(page, ITEMS_PER_PAGE, totalUsers, 1, adminUsers);
        if (page >= paginatedResult.getTotalPages()) {
            paginatedResult = new PaginatedResult<>(paginatedResult.getTotalPages() - 1, ITEMS_PER_PAGE, totalUsers, 1, adminUsers);
        }
        return paginatedResult;
    }

    @Override
    public long totalHighlightedRecipes(User user) {
        return this.userDao.totalHighlightedRecipes(user);
    }

    @Override
    public String getUserOrderString(String order) {
        locale = LocaleContextHolder.getLocale();
        String option = OrderOptionsUsers.valueOf(order).toString();
        if (option.equals(OrderOptionsUsers.NAME_ASC.toString())) {
            return messageSource.getMessage("search.orderBy.title.asc", null, locale);
        } else if (option.equals(OrderOptionsUsers.NAME_DESC.toString())) {
            return messageSource.getMessage("search.orderBy.title.desc", null, locale);

        }
        return null;
    }

    @Transactional
    @Override
    public Optional<User> getUserByRefreshToken(String token) {
        LOGGER.debug("Retrieving user for token with value {}", token);
        return sessionRefreshTokenDao.getTokenByValue(token).filter(SessionRefreshToken::isValid).map(SessionRefreshToken::getUser);
    }

    @Transactional
    @Override
    public SessionRefreshToken getSessionRefreshToken(User user) {
        LOGGER.debug("Retrieving session refresh token for user");
        final Optional<SessionRefreshToken> tokenOpt = sessionRefreshTokenDao.getTokenByUser(user);

        if (tokenOpt.isPresent()) {
            final SessionRefreshToken sessionRefreshToken = tokenOpt.get();
            if (!sessionRefreshToken.isValid()) {
                sessionRefreshToken.refresh();
            }
            return sessionRefreshToken;
        }

        return generateSessionRefreshToken(user);
    }

    private SessionRefreshToken generateSessionRefreshToken(User user) {
        return sessionRefreshTokenDao.createToken(user,
            SessionRefreshToken.generateSessionToken(),
            SessionRefreshToken.generateTokenExpirationDate()
        );
    }

    @Transactional
    @Override
    public Reports reportUser(String desc, User reporter, User reported, Comment comment) {
        return this.userDao.reportUser(desc, reporter, reported, comment);
    }

    @Override
    public PaginatedResult<Reports> getReports(String order, int page) {
        ReportsPanelOptions queryOrder;
        if (!ReportsPanelOptions.contains(order)) {
            queryOrder = DEFAULT_ORDER_REPORTS;
        } else {
            queryOrder = ReportsPanelOptions.valueOf(order);
        }
        final long totalReports = userDao.totalReports();
        final Collection<Reports> reports = userDao.getReports(queryOrder, page);
        PaginatedResult<Reports> paginatedResult = new PaginatedResult<>(page, ITEMS_PER_PAGE, totalReports, 1, reports);
        if (page >= paginatedResult.getTotalPages()) {
            paginatedResult = new PaginatedResult<>(paginatedResult.getTotalPages() - 1, ITEMS_PER_PAGE, totalReports, 1, reports);
        }
        return paginatedResult;
    }

    @Transactional
    @Override
    public void deleteReport(Long idReport) {
        userDao.deleteReport(idReport);
    }

    @Transactional
    @Override
    public void deleteUserReports(User user) {
        userDao.deleteUserReports(user);
    }
}

