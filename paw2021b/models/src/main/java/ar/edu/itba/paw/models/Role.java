package ar.edu.itba.paw.models;

public enum Role {
    USER("USER") ,
    ADMIN("ADMIN"),
    MANAGER("MANAGER"),
    VERIFIED("VERIFIED"),
    UNVERIFIED("UNVERIFIED"),
    BANNED("BANNED");

    private final String role;

    Role(String role) {
        this.role = role;
    }

    public String getRoleName(){
        return role;
    }
}
