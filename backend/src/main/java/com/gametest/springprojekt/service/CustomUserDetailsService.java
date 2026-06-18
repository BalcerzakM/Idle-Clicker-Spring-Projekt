package com.gametest.springprojekt.service;

import com.gametest.springprojekt.exception.UserIsBannedException;
import com.gametest.springprojekt.model.UserEntity;
import com.gametest.springprojekt.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService { //klasa dla Spring Security


    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity player = userRepository.getByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Nie ma takiego użytkownika!"));

        if (player.isBanned()){
            throw new UserIsBannedException("Użytkownik ma zablokowane konto");
        }

        return User.withUsername(player.getUsername())
                .password(player.getPassword())
                .roles(player.getRole())
                .build();
    }
}
