
package com.example.UserMicroserviceAPI.controller;

import com.example.UserMicroserviceAPI.model.Authority;
import com.example.UserMicroserviceAPI.model.AuthorityDto;
import com.example.UserMicroserviceAPI.service.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/authorities")
public class AuthorityController {
    @Autowired
    private AuthorityService authorityService;

    @PostMapping
    public ResponseEntity<String> createRoles(@RequestBody List<AuthorityDto> roles) {
        for (AuthorityDto roleDTO : roles) {
            authorityService.createAuthorityWithPermissions(roleDTO);
        }
        return ResponseEntity.ok(roles.size() + " authorities added successfully");
    }

    @GetMapping
    public ResponseEntity<List<AuthorityDto>> getAllAuthorities() {
        List<AuthorityDto> authorities = authorityService.findAll();
        return ResponseEntity.ok(authorities);
    }
}

