
package com.example.UserMicroserviceAPI.service;

import com.example.UserMicroserviceAPI.model.Authority;
import com.example.UserMicroserviceAPI.repository.AuthorityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthorityService {
    @Autowired
    private AuthorityRepository authorityRepository;

    public List<Authority> findAll() {
        return authorityRepository.findAll();
    }

    public Authority save(Authority authority) {
        return authorityRepository.save(authority);
    }
}
