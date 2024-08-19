package com.example.UserMicroserviceAPI.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.UserMicroserviceAPI.dto.SignupRequest;
import com.example.UserMicroserviceAPI.model.Authority;
import com.example.UserMicroserviceAPI.model.User;
import com.example.UserMicroserviceAPI.model.UserAuthority;
import com.example.UserMicroserviceAPI.repository.AuthorityRepository;
import com.example.UserMicroserviceAPI.repository.UserAuthorityRepository;
import com.example.UserMicroserviceAPI.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    AuthorityRepository authorityRepository;

    @Autowired
    private UserAuthorityRepository userAuthorityRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Transactional
    public User registerUser(SignupRequest signupRequest) {
        if (userRepository.findByUsername(signupRequest.getUsername()).isPresent() ||
            userRepository.findByEmail(signupRequest.getEmail()).isPresent()) {
            throw new RuntimeException("User already exists");
        }
        User user = new User();
        user.setUsername(signupRequest.getUsername());
        user.setFirstname(signupRequest.getFirstname());
        user.setLastname(signupRequest.getLastname());
        user.setPhone(signupRequest.getPhone());
        user.setEmail(signupRequest.getEmail());
        user.setEnabled(signupRequest.getEnabled());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));

        // Save the user first
        User savedUser = userRepository.save(user);

        // Now save authorities and user_authorities relationships
        Set<Authority> authorities = new HashSet<>();
        for (Long authorityId : signupRequest.getAuthorityIDs()) {
            Authority authority = authorityRepository.findById(authorityId)
                .orElseThrow(() -> new RuntimeException("Authority not found"));
            authorities.add(authority);

            // Save the relationship in user_authorities table
            UserAuthority userAuthority = new UserAuthority();
            userAuthority.setUser(savedUser);
            userAuthority.setAuthority(authority);
            userAuthorityRepository.save(userAuthority);
        }

        savedUser.setAuthorities(authorities);

        System.out.println("Authority in SignupRequest: " + signupRequest.getAuthorityIDs()); // Debug statement
        System.out.println("Authority in User entity: " + savedUser.getAuthorities()); // Debug statement

        return savedUser;
    }
}
