
package com.example.UserMicroserviceAPI.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinColumn;
import java.util.Set;



@Entity
public class Authority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String Authority;
    
    @ManyToMany(mappedBy = "Authority")
    private Set<User> User;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "authority_permissions",
        joinColumns = @JoinColumn(name = "authority_id"),
        inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<Permission> permissions;

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthority() {
        return Authority;
    }

    public void setAuthority(String Authority) {
        this.Authority = Authority;
    }

    public Set<User> getUsers() {
        return User;
    }

    public void setUsers(Set<User> User) {
        this.User = User;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }
}
