package ar.edu.itba.paw;

//import ar.edu.itba.paw.interfaces.*;
//import ar.edu.itba.paw.models.Role;
//import ar.edu.itba.paw.models.User;
//import ar.edu.itba.paw.models.UserRole;
//import ar.edu.itba.paw.models.VerificationToken;
//import ar.edu.itba.paw.persistence.RoleDaoImpl;
//import ar.edu.itba.paw.persistence.VerificationTokenDaoImpl;
//import ar.edu.itba.paw.services.RoleServiceImpl;
//import ar.edu.itba.paw.services.UserServiceImpl;
//import org.junit.Assert;
import ar.edu.itba.paw.interfaces.*;
import ar.edu.itba.paw.interfaces.exceptions.DuplicateUserException;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.services.UserServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
//import org.mockito.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.context.MessageSource;
//import org.springframework.context.i18n.LocaleContextHolder;
//import org.springframework.security.crypto.password.PasswordEncoder;

import javax.mail.MessagingException;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;


@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    private static final String USERNAME = "menganito";
    private static final String EMAIL = "menganito@mail.com";
    private static final String PASS = "menganitopass";

    private static final User testUser = new User(null, USERNAME, EMAIL, PASS, null, null, false, LocalDate.now());

    @Mock
    private UserDao mockUserDao;
    @Mock
    private User mockUser;

    @Mock
    private PasswordEncoder mockEncoder;

    @Mock
    private RoleService mockRoleService;

    @Mock
    private MailService mockMailService;

    @Mock
    private VerificationTokenDao mockVerifDao;

    @Mock
    private PasswordTokenDao mockPassDao;

    @InjectMocks
    private final UserServiceImpl userService = new UserServiceImpl();

    @Before
    public void setTest() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
    }

    @Test
    public void testCreate() throws Exception {
        VerificationToken token = new VerificationToken("token", testUser, LocalDate.now().plusDays(2));
        when(mockUserDao.create(Mockito.eq(USERNAME), Mockito.eq(EMAIL), Mockito.eq(PASS))).thenReturn(testUser);
        when(mockEncoder.encode(PASS)).thenReturn(PASS);
        when(mockVerifDao.generateVerificationToken(Mockito.any(User.class), Mockito.anyString(), Mockito.any(LocalDate.class))).thenReturn(token);

        User user = userService.create(USERNAME, EMAIL, PASS);
        assertNotNull(user);
        assertEquals(testUser.getUsername(), user.getUsername());
        assertEquals(testUser.getEmail(), user.getEmail());
        assertEquals(testUser.getPassword(), user.getPassword());
        verify(mockUserDao).create(USERNAME, EMAIL, PASS);
    }

    @Test
    public void testVerifyAccount(){
        VerificationToken token = new VerificationToken("token", mockUser, LocalDate.now().plusDays(2));
        when(mockVerifDao.getTokenByStringValue(Mockito.anyString())).thenReturn((Optional.of(token)));
        Mockito.doNothing().when(mockVerifDao).removeTokenByUserId(Mockito.any(User.class));

        userService.verifyAccount("token");
        verify(mockRoleService).addRole(mockUser, Role.VERIFIED);
        verify(mockRoleService).removeRole(mockUser, Role.UNVERIFIED);
    }

    @Test
    public void testResetPassword(){
        PasswordToken token = new PasswordToken("token", mockUser, LocalDate.now().plusDays(2));
        when(mockPassDao.getTokenByStringValue(Mockito.anyString())).thenReturn((Optional.of(token)));
        Mockito.doNothing().when(mockPassDao).removeTokenByUserId(Mockito.any(User.class));

        when(mockEncoder.encode(PASS)).thenReturn("changedPass");

        userService.resetPassword("token", PASS);
        verify(mockUser).setPassword(Mockito.eq("changedPass"));

    }

    @Test
    public void testValidatePasswordToken(){
        PasswordToken token = new PasswordToken("token", mockUser, LocalDate.now().minusDays(1));
        boolean test = userService.validatePasswordToken("token");
        assertFalse(test);
    }

    @Test
    public void testBan(){
        UserRole testRole = new UserRole(Role.USER.getRoleName(), mockUser);
        ArrayList<UserRole> roleList = new ArrayList<>();
        roleList.add(testRole);
        when(userService.getRoles(mockUser)).thenReturn(roleList);
        userService.ban(mockUser);
        verify(mockRoleService).addRole(mockUser, Role.BANNED);
    }

    @Test
    public void testIsAdmin(){
        UserRole testRole = new UserRole(Role.ADMIN.getRoleName(), mockUser);
        ArrayList<UserRole> roleList = new ArrayList<>();
        roleList.add(testRole);
        when(userService.getRoles(mockUser)).thenReturn(roleList);
        assertTrue(userService.isAdmin(mockUser));
    }

    @Test
    public void testIsManager(){
        UserRole testRole = new UserRole(Role.MANAGER.getRoleName(), mockUser);
        ArrayList<UserRole> roleList = new ArrayList<>();
        roleList.add(testRole);
        when(userService.getRoles(mockUser)).thenReturn(roleList);
        assertTrue(userService.isManager(mockUser));
    }

    @Test
    public void testIsBanned(){
        UserRole testRole = new UserRole(Role.BANNED.getRoleName(), mockUser);
        ArrayList<UserRole> roleList = new ArrayList<>();
        roleList.add(testRole);
        when(userService.getRoles(mockUser)).thenReturn(roleList);
        assertTrue(userService.isBanned(mockUser));
    }

    @Test
    public void testChangeUser(){
        VerificationToken token = new VerificationToken("token", mockUser, LocalDate.now().plusDays(2));
        when(mockEncoder.encode(PASS)).thenReturn(PASS);
        when(mockUserDao.findById(mockUser.getIdUser())).thenReturn(Optional.of(mockUser));
        when(mockVerifDao.generateVerificationToken(Mockito.any(User.class), Mockito.anyString(), Mockito.any(LocalDate.class))).thenReturn(token);
        userService.changeUser(USERNAME, PASS, mockUser.getIdUser());
        verify(mockRoleService).addRole(mockUser, Role.UNVERIFIED);
        verify(mockRoleService).addRole(mockUser, Role.USER);
    }

}
