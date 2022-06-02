package ar.edu.itba.paw;

import ar.edu.itba.paw.interfaces.PasswordTokenDao;
import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.models.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@Rollback //para volver atras despues del test
@Sql(scripts = "classpath:passtokenTest.sql") //para partir de una base conocida
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration(classes = TestConfig.class)
public class PasswordTokenDaoTest {

    @Autowired
    private DataSource ds;
    @Autowired
    private PasswordTokenDao passwordTokenDao;
    @Autowired
    private UserDao userDao;

    private JdbcTemplate jdbcTemplate;


    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @Test
    public void testGeneratePasswordToken() {
        Optional<User> optionalUser = userDao.findById(1L);
        if(!optionalUser.isPresent())
            return;
        PasswordToken token = passwordTokenDao.generatePasswordToken(optionalUser.get(), "token", LocalDate.of(2021,4,10));
        assertEquals(optionalUser.get(), token.getUser());
        assertEquals("token", token.getToken());
        assertEquals(LocalDate.of(2021,4,10), token.getExpirationDate());
    }

    @Test
    public void testGetTokenByStringValue(){
        Optional<User> optionalUser = userDao.findById(2L);
        if(!optionalUser.isPresent())
            return;
        Optional<PasswordToken> token= passwordTokenDao.getTokenByStringValue("token");
        assertTrue(token.isPresent());
        assertEquals(token.get().getUser(), optionalUser.get());
    }


    @Test
    public void testRemoveTokenByUserId(){
        Optional<User> optionalUser = userDao.findById(2L);
        if(!optionalUser.isPresent())
            return;
        passwordTokenDao.removeTokenByUserId(optionalUser.get());
        assertFalse(passwordTokenDao.getTokenByStringValue("token").isPresent());
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "password_tokens", "id_user = 2"));

    }


}