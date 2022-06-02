package ar.edu.itba.paw.interfaces;

import ar.edu.itba.paw.models.Role;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.UserRole;

import java.util.List;

public interface RoleService {
    boolean makeAdmin(Long newAdminId);
    boolean removeAdmin(Long newAdminId);
    UserRole addRole(User user, Role role);
    void removeRole(User user, Role role);
    List<UserRole> getRolesById(User user);
    boolean isVerified(User user);
}
