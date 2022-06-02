package ar.edu.itba.paw;

import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.interfaces.exceptions.DuplicateUserException;
import ar.edu.itba.paw.models.AdminPanelOptions;
import ar.edu.itba.paw.models.ImageUser;
import ar.edu.itba.paw.models.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.mockito.Mockito;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static ar.edu.itba.paw.models.OrderOptionsUsers.NAME_ASC;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Rollback //para volver atras despues del test
@Sql(scripts = "classpath:userTest.sql") //para partir de una base conocida
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration(classes = TestConfig.class)
public class UserDaoTest {

    private static final String USERNAME = "fulanito";
    private static final String EMAIL = "fulanito@mailcito.com";
    private static final String PASSWORD = "lacontradefulano";
    private static final byte[] AVATAR = {2, 3, 0};

    @Autowired
    private DataSource ds;
    @Autowired
    private UserDao userDao;

    @PersistenceContext
    private EntityManager em;


    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @Test
    public void testCreateUser() throws DuplicateUserException {
        final User user = userDao.create(USERNAME, EMAIL, PASSWORD);
        assertNotNull(user);
        assertEquals(USERNAME, user.getUsername());
        assertEquals(EMAIL, user.getEmail());
        assertEquals(PASSWORD, user.getPassword());
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "user_t", "id_user = " + user.getIdUser()));
    }

    @Test
    public void testFindById(){
        User user = Mockito.mock(User.class);
        when(user.getIdUser()).thenReturn(1L);
        final Optional<User> optionalUser = userDao.findById(user.getIdUser());
        assertTrue(optionalUser.isPresent());
        assertEquals(optionalUser.get().getIdUser(), user.getIdUser());
    }

    @Test
    public void testFailFindById(){
        final Optional<User> usr = userDao.findById(800);
        assertFalse(usr.isPresent());
    }

    @Test
    public void testFindByEmail(){
        User user = Mockito.mock(User.class);
        when(user.getIdUser()).thenReturn(1L);
        when(user.getEmail()).thenReturn("menganito@mail.com");

        final Optional<User> usr = userDao.findByEmail(user.getEmail());
        assertTrue(usr.isPresent());
        assertEquals(usr.get().getIdUser(), user.getIdUser());
    }

    @Test
    public void testFailFindByEmail(){
        final Optional<User> usr = userDao.findByEmail("noexiste@mail.com");
        assertFalse(usr.isPresent());
    }

    @Test
    public void testFindByUsername(){
        User user = Mockito.mock(User.class);
        when(user.getIdUser()).thenReturn(1L);
        when(user.getUsername()).thenReturn("menganito");

        final Optional<User> usr = userDao.findByUsername(user.getUsername());
        assertTrue(usr.isPresent());
        assertEquals(usr.get().getIdUser(), user.getIdUser());
    }

    @Test
    public void testFailFindByUsername(){
        final Optional<User> usr = userDao.findByUsername("noexiste");
        assertFalse(usr.isPresent());
    }

    @Test
    public void testGetAdmins(){
        final List<User> adminUsers = userDao.findAdmins(AdminPanelOptions.BY_USERNAME, 0);
        User user = Mockito.mock(User.class);
        when(user.getIdUser()).thenReturn(1L);
        assertEquals(adminUsers.get(0).getIdUser(), user.getIdUser());
    }

    @Test
    public void testGetBanned(){
        final List<User> adminUsers = userDao.findBannedUsers(AdminPanelOptions.BY_USERNAME, 0);
        User user = Mockito.mock(User.class);
        when(user.getIdUser()).thenReturn(1L);
        assertEquals(adminUsers.get(0).getIdUser(), user.getIdUser());
    }

    @Test
    public void testTotalBanned(){
        final long banned = userDao.totalBannedUsers();
        assertEquals(1, banned);
    }

    @Test
    public void testTotalAdmin(){
        final long admin = userDao.totalAdminUsers();
        assertEquals(1, admin);
    }

    @Test
    public void testSearchUsers(){
        User user = Mockito.mock(User.class);
        when(user.getIdUser()).thenReturn(1L);
        Collection<User> found = userDao.searchUsers("menganito", false, false, NAME_ASC, 0);
        ArrayList<User> foundlist = new ArrayList<>(found);
        assertEquals(foundlist.get(0).getIdUser(), user.getIdUser());
    }

    @Test
    public void testTotalUsers(){
        final long users = userDao.searchUsersCount("menganito", false, false,0);
        assertEquals(1, users);
    }

}
