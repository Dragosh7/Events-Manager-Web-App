package com.example.events.config;

import com.example.events.Entity.User;
import com.example.events.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
@Slf4j
public class MyUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> userByUsername = userRepository.findUserByUsername(username);

        if (userByUsername.isEmpty()) {
            log.error("Could not find user with that username: {}", username);
            throw new UsernameNotFoundException("Invalid credentials!");
        }

        User user = userByUsername.get();

        if (!user.getUsername().equals(username)) {
            log.error("Could not find user with that username: {}", username);
            throw new UsernameNotFoundException("Invalid credentials!");
        }

        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        String role = user.getRole();
        grantedAuthorities.add(new SimpleGrantedAuthority(role));

        return new MySecurityUser(user.getUsername(), user.getPassword(), true, true, true, true, grantedAuthorities,
                user.getFirstName(), user.getLastName(), user.getEmailAddress());
    }
}
