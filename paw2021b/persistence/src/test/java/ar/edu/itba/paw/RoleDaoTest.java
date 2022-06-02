package ar.edu.itba.paw;

import ar.edu.itba.paw.interfaces.RoleDao;
import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.models.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.when;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@Rollback //para volver atras despues del test
@Sql(scripts = "classpath:roleTest.sql") //para partir de una base conocida
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration(classes = TestConfig.class)
public class RoleDaoTest {

    @Autowired
    private DataSource ds;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private UserDao userDao;

    private JdbcTemplate jdbcTemplate;


    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @Test
    public void testAddRole() {
        Optional<User> optionalUser = userDao.findById(2L);
        if(!optionalUser.isPresent())
            return;

        roleDao.addRole(optionalUser.get(), Role.BANNED);
        List<UserRole> roles = roleDao.getRolesById(optionalUser.get());
        assertEquals(roles.get(0).getRole(), Role.BANNED);

    }

    @Test
    public void testGetRolesById(){

        Optional<User> optionalUser = userDao.findById(1L);
        if(!optionalUser.isPresent())
            return;
        UserRole verifRole = new UserRole(Role.VERIFIED.getRoleName(), optionalUser.get());
        UserRole adminRole = new UserRole(Role.ADMIN.getRoleName(), optionalUser.get());
        UserRole userRole = new UserRole(Role.USER.getRoleName(), optionalUser.get());

        List<UserRole> roles = roleDao.getRolesById(optionalUser.get());
        assertTrue(roles.contains(verifRole));
        assertTrue(roles.contains(userRole));
        assertTrue(roles.contains(adminRole));

    }

    @Test
    public void testRemoveRole(){
        Optional<User> optionalUser = userDao.findById(1L);
        if(!optionalUser.isPresent())
            return;
        UserRole adminRole = new UserRole(Role.ADMIN.getRoleName(), optionalUser.get());
        roleDao.removeRole(optionalUser.get(), Role.ADMIN);
        List<UserRole> roles = roleDao.getRolesById(optionalUser.get());
        assertFalse(roles.contains(adminRole));
        assertEquals(2, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "roles", "id_user = 1"));

    }

    @Test
    public void testIsVerified(){
        Optional<User> optionalUser = userDao.findById(1L);
        if(!optionalUser.isPresent())
            return;
        assertTrue(roleDao.isVerified(optionalUser.get()));
    }


}
