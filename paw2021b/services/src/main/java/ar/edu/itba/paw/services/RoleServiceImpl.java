package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfaces.RoleDao;
import ar.edu.itba.paw.interfaces.RoleService;
import ar.edu.itba.paw.interfaces.UserService;
import ar.edu.itba.paw.models.Role;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.UserRole;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class RoleServiceImpl implements RoleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoleServiceImpl.class);

    @Autowired
    UserService userService;

    @Transactional
    @Override
    public boolean makeAdmin(Long newAdminId){
        Optional<User> newAdmin = userService.findById(newAdminId);
        LOGGER.debug("Making user with id {} admin", newAdminId);
        if(!newAdmin.isPresent() ){
            return false;
        }
        if(!userService.isAdmin(newAdmin.get()))
            addRole(newAdmin.get(), Role.ADMIN);
        return true;
    }

    @Transactional
    @Override
    public boolean removeAdmin(Long newAdminId){
        Optional<User> newAdmin = userService.findById(newAdminId);
        LOGGER.debug("Removing user with id {} admin", newAdminId);
        if(!newAdmin.isPresent() ){
            return false;
        }
        if(userService.isAdmin(newAdmin.get()))
            removeRole(newAdmin.get(), Role.ADMIN);
        return true;
    }


    @Autowired
    private RoleDao roleDao;

    @Transactional
    @Override
    public UserRole addRole(User user, Role role) {
        return this.roleDao.addRole(user, role);
    }

    @Override
    public boolean isVerified(User user){
        return this.roleDao.isVerified(user);
    }

    @Transactional
    @Override
    public void removeRole(User user, Role role) {
        this.roleDao.removeRole(user, role);
    }

    @Override
    public List<UserRole> getRolesById(User user) {
        return this.roleDao.getRolesById(user);
    }
}
