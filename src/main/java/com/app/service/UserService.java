package com.app.service;

import com.app.exceptions.ExceptionCode;
import com.app.exceptions.MyException;
import com.app.model.User;
import com.app.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserService {
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerNewUser(User user) {
        try {
            if (user == null) {
                throw new NullPointerException("USER IS NULL");
            }

            if (!Objects.equals(user.getPassword(), user.getPasswordConfirmation())) {
                throw new IllegalArgumentException("PASSWORDS DON'T MATCH");
            }

            user.setEnabled(true);
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            userRepository.save(user);

        } catch (Exception e) {
            throw new MyException(ExceptionCode.SERVICE, e.getMessage());
        }
    }
}
