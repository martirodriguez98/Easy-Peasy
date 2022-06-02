package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "roles")
public class UserRole {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "roles_id_seq")
    @SequenceGenerator(sequenceName = "roles_id_seq", name="roles_id_seq", allocationSize = 1)
    @Column(name="id")
    private long id;

    @Column(name = "role_t")
    private String role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_user",nullable = false)
    private User user;


    /* default */
    protected UserRole() {
       // Just for Hibernate
    }

    public UserRole(String role, User user) {
        this.role = role;
        this.user = user;
    }

    public Role getRole() {
        return Role.valueOf(role);
    }

    public void setRole(String role) {
        this.role = role;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRole userRole = (UserRole) o;
        return user.getIdUser() == userRole.user.getIdUser() && Objects.equals(role, userRole.role);
    }

}
