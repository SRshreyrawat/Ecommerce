package com.project.Ecommerce.Controller;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.project.Ecommerce.Entity.AppRole;
import com.project.Ecommerce.Entity.Role;
import com.project.Ecommerce.Entity.User;
import com.project.Ecommerce.Repository.RoleRepository;
import com.project.Ecommerce.Repository.UserRepository;
import com.project.Ecommerce.Security.Jwt.JwtUtils;
import com.project.Ecommerce.Security.request.LoginRequest;
import com.project.Ecommerce.Security.request.SignupRequest;
import com.project.Ecommerce.Security.Services.UserDetailsImpl;
import com.project.Ecommerce.Security.response.MessageResponse;
import com.project.Ecommerce.Security.response.UserInfoResponse;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    PasswordEncoder passwordEncoder;

    Role role;

  
        // ================= SIGN IN =================

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {

        Authentication authentication;

        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()));
        } catch (AuthenticationException e) {
            Map<String, Object> error = new HashMap<>();
            error.put("message", "Invalid username or password");
            error.put("status", false);
            return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        String jwtToken = jwtUtils.generateTokenFromUsername(userDetails.getUsername());

        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(role -> role.getAuthority())
                .toList();

        UserInfoResponse response = new UserInfoResponse(
                userDetails.getId(),
                userDetails.getUsername(),
                roles,
                jwtToken);

        return ResponseEntity.ok(response);
    }

        // ================= SIGN UP =================
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(
            @Valid @RequestBody SignupRequest signupRequest) {

        if (userRepository.existsByUserName(signupRequest.getUsername())) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: Username already taken"));
        }

        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponse("Error: Email already in use"));
        }

        // Create User
        User user = new User(
                signupRequest.getUsername(),
                signupRequest.getEmail(),
                passwordEncoder.encode(signupRequest.getPassword())
        );

        Set<String> strRoles = signupRequest.getRole();
        Set<Role> roles = new HashSet<>();

        // Default ROLE_USER
        if (strRoles == null || strRoles.isEmpty()) {
            Role userRole = roleRepository
                    .findByRoleName(AppRole.ROLE_USER)
                    .orElseThrow(() ->
                            new RuntimeException("Error: Role USER not found"));
            roles.add(userRole);
        } else {
            strRoles.forEach(roleStr -> {
                switch (roleStr.toLowerCase()) {
                    case "admin":
                        Role adminRole = roleRepository
                                .findByRoleName(AppRole.ROLE_ADMIN)
                                .orElseThrow(() ->
                                        new RuntimeException("Error: Role ADMIN not found"));
                        roles.add(adminRole);
                        break;

                    case "seller":
                        Role sellerRole = roleRepository
                                .findByRoleName(AppRole.ROLE_SELLER)
                                .orElseThrow(() ->
                                        new RuntimeException("Error: Role SELLER not found"));
                        roles.add(sellerRole);
                        break;

                    default:
                        Role userRole = roleRepository
                                .findByRoleName(AppRole.ROLE_USER)
                                .orElseThrow(() ->
                                        new RuntimeException("Error: Role USER not found"));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(
                new MessageResponse("User registered successfully"));
       
      
    }
}
