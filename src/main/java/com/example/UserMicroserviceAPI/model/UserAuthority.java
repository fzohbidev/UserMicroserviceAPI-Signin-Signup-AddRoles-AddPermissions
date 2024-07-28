package com.example.UserMicroserviceAPI.model;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.JoinColumn;
import java.util.Set;


@Entity
@Table(name = "user_authorities")
public class UserAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "authority_id")
    private Authority authority;

    // Default constructor
    public UserAuthority() {
    }

    // Parameterized constructor
    public UserAuthority(User user, Authority authority) {
        this.user = user;
        this.authority = authority;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Authority getAuthority() {
        return authority;
    }

    public void setAuthority(Authority authority) {
        this.authority = authority;
    }

    @Override
    public String toString() {
        return "UserAuthority{" +
                "id=" + id +
                ", user=" + user +
                ", authority=" + authority +
                '}';
    }
}
