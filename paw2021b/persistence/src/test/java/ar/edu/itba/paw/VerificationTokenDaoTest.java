package ar.edu.itba.paw;

import ar.edu.itba.paw.interfaces.UserDao;
import ar.edu.itba.paw.interfaces.VerificationTokenDao;
import ar.edu.itba.paw.models.*;
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
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@Rollback //para volver atras despues del test
@Sql(scripts = "classpath:veriftokenTest.sql") //para partir de una base conocida
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
@ContextConfiguration(classes = TestConfig.class)
public class VerificationTokenDaoTest {

    @Autowired
    private DataSource ds;
    @Autowired
    private VerificationTokenDao verificationTokenDao;
    @Autowired
    private UserDao userDao;

    private JdbcTemplate jdbcTemplate;


    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @Test
    public void testGenerateVerificationToken() {
        Optional<User> optionalUser = userDao.findById(1L);
        if(!optionalUser.isPresent())
            return;
        VerificationToken token = verificationTokenDao.generateVerificationToken(optionalUser.get(), "token", LocalDate.of(2021,4,10));
        assertEquals(optionalUser.get(), token.getUser());
        assertEquals("token", token.getToken());
        assertEquals(LocalDate.of(2021,4,10), token.getExpirationDate());
    }

    @Test
    public void testGetTokenByStringValue(){
        Optional<User> optionalUser = userDao.findById(2L);
        if(!optionalUser.isPresent())
            return;
        Optional<VerificationToken> token= verificationTokenDao.getTokenByStringValue("token");
        assertTrue(token.isPresent());
        assertEquals(token.get().getUser(), optionalUser.get());
    }

    @Test
    public void testRemoveTokenByUserId(){
        Optional<User> optionalUser = userDao.findById(2L);
        if(!optionalUser.isPresent())
            return;
        verificationTokenDao.removeTokenByUserId(optionalUser.get());
        assertFalse(verificationTokenDao.getTokenByStringValue("token").isPresent());
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, "verify_tokens", "id_user = 2"));

    }


}