package com.app.security;

import com.app.exceptions.ExceptionCode;
import com.app.exceptions.MyException;
import com.app.model.Role;
import com.app.model.User;
import com.app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

@Service
@Qualifier("myUserDetailsService")
public class MyUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            if (username == null) {
                throw new NullPointerException("USERNAME IS NULL");
            }

            User user = userRepository
                    .findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException(username));

            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    user.getEnabled(),
                    true,
                    true,
                    true,
                    getAuthorites(user.getRole())
            );
        } catch (Exception e) {
            throw new MyException(ExceptionCode.SECURITY, e.getMessage());
        }
    }

    public Collection<GrantedAuthority> getAuthorites(Role... roles) {
        return Arrays
                .stream(roles)
                .map(r -> new SimpleGrantedAuthority(r.getFullName()))
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
