package com.project.Ecommerce.Security.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.project.Ecommerce.Entity.User;
import com.project.Ecommerce.Repository.UserRepository;

import jakarta.transaction.Transactional;
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserRepository userRepository;
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       
        User user = userRepository.findByUserName(username)
        .orElseThrow(() -> new UsernameNotFoundException("Username Not Found " + username));

       
       
        return UserDetailsImpl.build(user);
    }

    
    
}
