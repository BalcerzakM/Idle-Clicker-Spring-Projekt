package com.gametest.springprojekt.service;

import com.gametest.springprojekt.dto.RegistrationDto;
import com.gametest.springprojekt.exception.EmailAlreadyExistsException;
import com.gametest.springprojekt.exception.UsernameAlreadyExistsException;
import com.gametest.springprojekt.model.UserEntity;
import com.gametest.springprojekt.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional // adnotacja zapewniająca atomowość - np aby w trakcie dodawania nikt nie użył tej samej nazwy itd.
    public void registerUser(RegistrationDto user) throws UsernameAlreadyExistsException {
        if(userRepository.existsByUsername((user.getUsername()))){
            throw new UsernameAlreadyExistsException("Użytkownik o tej nazwie już istnieje");
        }
        if (userRepository.existsByEmail(user.getEmail())){
            throw new EmailAlreadyExistsException("Adres email jest już zajęty");
        }

        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(user.getUsername());
        userEntity.setPassword(passwordEncoder.encode(user.getPassword()));
        userEntity.setEmail(user.getEmail());
        userRepository.save(userEntity);
    }

}
