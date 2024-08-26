package com.example.UserMicroserviceAPI.service;

import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.UserMicroserviceAPI.model.UserWithRolesDto;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.OptimisticLockingFailureException;
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

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);


    public List<UserWithRolesDto> getAllUsersWithRoles() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::convertToUserWithRolesDto)
                .collect(Collectors.toList());
    }

    private UserWithRolesDto convertToUserWithRolesDto(User user) {
        List<String> roles = user.getAuthorities().stream()
                .map(Authority::getAuthority)
                .collect(Collectors.toList());

        return new UserWithRolesDto(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstname(),
                user.getLastname(),
                roles
        );
    }

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

    @Transactional
    public User updateUserRoles(Long userId, Set<Long> newAuthorityIds) {
        logger.debug("Updating roles for user with ID: {}", userId);

        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

            logger.debug("Found user: {}", user);

            // Fetch existing user authorities
            List<UserAuthority> existingAuthorities = userAuthorityRepository.findByUserId(userId);

            // Remove authorities that are not in the new set
            for (UserAuthority ua : existingAuthorities) {
                if (!newAuthorityIds.contains(ua.getAuthority().getId())) {
                    userAuthorityRepository.delete(ua);
                    logger.debug("Deleted user authority: {}", ua);
                }
            }

            // Add new authorities
            Set<Authority> newAuthorities = new HashSet<>();
            for (Long authorityId : newAuthorityIds) {
                Authority authority = authorityRepository.findById(authorityId)
                        .orElseThrow(() -> new EntityNotFoundException("Authority not found with ID: " + authorityId));

                if (existingAuthorities.stream().noneMatch(ua -> ua.getAuthority().getId().equals(authorityId))) {
                    UserAuthority userAuthority = new UserAuthority(user, authority);
                    userAuthorityRepository.save(userAuthority);
                    logger.debug("Saved new user authority: {}", userAuthority);
                }

                newAuthorities.add(authority);
            }

            user.setAuthorities(newAuthorities);
            User savedUser = userRepository.save(user);
            logger.debug("Saved updated user: {}", savedUser);

            return savedUser;
        } catch (OptimisticLockingFailureException e) {
            logger.error("Concurrent modification detected", e);
            throw new ConcurrentModificationException("The user roles were modified by another transaction. Please try again.");
        } catch (Exception e) {
            logger.error("Error in updateUserRoles", e);
            throw e;
        }
    }
}
