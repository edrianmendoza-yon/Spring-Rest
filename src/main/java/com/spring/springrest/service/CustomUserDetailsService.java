package com.spring.springrest.service;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.spring.springrest.entity.AuthRole;
import com.spring.springrest.entity.AuthUser;
import com.spring.springrest.repository.AuthUserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService{
    private final AuthUserRepository authUserRepository;
    
    public CustomUserDetailsService(AuthUserRepository authUserRepository) {
        this.authUserRepository = authUserRepository;
    }
    
    public UserDetails loadUserByUsername(String username) {
        AuthUser  authUser = authUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
        
        return new User(authUser.getUsername(), authUser.getPassword(), mapRolesToAuthorities(authUser.getRoles()));
    }
    
    private Collection<GrantedAuthority> mapRolesToAuthorities(
            List<AuthRole> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }
}
