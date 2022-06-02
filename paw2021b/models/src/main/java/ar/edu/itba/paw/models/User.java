package ar.edu.itba.paw.models;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "user_t")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_t_id_user_seq")
    @SequenceGenerator(sequenceName = "user_t_id_user_seq", name = "user_t_id_user_seq", allocationSize = 1)
    @Column(name = "id_user")
    private Long idUser;

    @Column(name = "nickname", length = 500, nullable = true, unique = false)
    private String name;

    @Column(name = "username", length = 30, unique = true)
    private String username;

    @Column(name = "email", length = 254, nullable = false, unique = true)
    private String email;

    @Column(name = "password", length = 100)
    private String password;

    @Column(name = "avatar")
    @Basic(fetch = FetchType.LAZY)
    private byte[] avatar;

    @Column(name = "mime_type")
    private String mimeType;

    @Column(name = "privileged")
    private boolean privileged;

    @Column(name = "date_created")
    private LocalDate dateCreated;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = {CascadeType.MERGE, CascadeType.REFRESH}, orphanRemoval = true)
    private Set<LikedRecipes> likes;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = {CascadeType.MERGE, CascadeType.REFRESH}, orphanRemoval = true)
    private Set<DislikedRecipes> dislikes;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = {CascadeType.MERGE, CascadeType.REFRESH}, orphanRemoval = true)
    private Set<Favourites> favourites;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = {CascadeType.MERGE, CascadeType.REFRESH}, orphanRemoval = true)
    private Set<Comment> comments;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user", cascade = {CascadeType.MERGE, CascadeType.REFRESH}, orphanRemoval = true)
    private Set<Recipe> recipes;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = {CascadeType.MERGE, CascadeType.REFRESH}, orphanRemoval = true)
    private Set<UserRole> roles;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.REFRESH}, orphanRemoval = true)
    private VerificationToken verifToken;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.REFRESH}, orphanRemoval = true)
    private PasswordToken passToken;

    /* default */
    protected User() {
        // Just for Hibernate
    }

    public User(String name, String username, String email, String password, byte[] avatar, String mimeType, boolean privileged, LocalDate dateCreated) {
        this.name = name;
        this.username = username;
        this.email = email;
        this.password = password;
        this.avatar = avatar;
        this.mimeType = mimeType;
        this.privileged = privileged;
        this.dateCreated = dateCreated;
    }

    public long getIdUser() {
        return idUser;
    }

    public void setIdUser(long idUser) {
        this.idUser = idUser;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public byte[] getAvatar() {
        return avatar;
    }

    public void setAvatar(byte[] avatar) {
        this.avatar = avatar;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public boolean isPrivileged() {
        return privileged;
    }

    public void setPrivileged(boolean privileged) {
        this.privileged = privileged;
    }

    public LocalDate getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(LocalDate dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Set<LikedRecipes> getLikes() {
        return likes;
    }

    public void setLikes(Set<LikedRecipes> likes) {
        this.likes = likes;
    }

    public Set<DislikedRecipes> getDislikes() {
        return dislikes;
    }

    public void setDislikes(Set<DislikedRecipes> dislikes) {
        this.dislikes = dislikes;
    }

    public Set<Favourites> getFavourites() {
        return favourites;
    }

    public void setFavourites(Set<Favourites> favourites) {
        this.favourites = favourites;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Set<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(Set<Recipe> recipes) {
        this.recipes = recipes;
    }

    public Set<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(Set<UserRole> roles) {
        this.roles = roles;
    }

    public Set<Recipe> getHighlightedRecipes() {
        Set<Recipe> highlightedRecipes = new HashSet<>();
        for (Recipe r : recipes) {
            if (r.isHighlighted()) {
                highlightedRecipes.add(r);
            }
        }
        return highlightedRecipes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(idUser, user.idUser);
    }


    @Override
    public int hashCode() {
        return Objects.hash(idUser);
    }
}


