package com.FoodDelivery.FoodDeliveryBackend.serviceImpl;


import org.springframework.security.core.userdetails.User;
import com.FoodDelivery.FoodDeliveryBackend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@AllArgsConstructor
public class AppUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        com.FoodDelivery.FoodDeliveryBackend.model.User user =
                userRepository.findByEmail(email)
                        .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));

        return new User(
                user.getEmail(),
                user.getPassword(),
                Collections.emptyList()
        );
    }
}

