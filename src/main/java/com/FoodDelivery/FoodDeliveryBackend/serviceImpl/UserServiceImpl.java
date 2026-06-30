package com.FoodDelivery.FoodDeliveryBackend.serviceImpl;

import com.FoodDelivery.FoodDeliveryBackend.io.UserRequest;
import com.FoodDelivery.FoodDeliveryBackend.io.UserResponse;
import com.FoodDelivery.FoodDeliveryBackend.model.User;
import com.FoodDelivery.FoodDeliveryBackend.repository.UserRepository;
import com.FoodDelivery.FoodDeliveryBackend.service.AuthenticationFacade;
import com.FoodDelivery.FoodDeliveryBackend.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationFacade authenticationFacade;

    @Override
    public UserResponse registerUser(UserRequest request) {
        User newUser = mapToModel(request);
        newUser = userRepository.save(newUser);
        return convertToResponse(newUser);
    }

    @Override
    public String findByUserId() {
        String loggedInUserEmail = authenticationFacade
                .getAuthentication()
                .getName();

        User loggedInUser = userRepository.findByEmail(loggedInUserEmail)
                .orElseThrow(()->new UsernameNotFoundException("User not Found"));

        return loggedInUser.getId();
    }


    private User mapToModel(UserRequest request){
        return User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .name(request.getName())
                .build();

    }

    private UserResponse convertToResponse(User registeredUser){
        return UserResponse.builder()
                .id(registeredUser.getId())
                .name(registeredUser.getName())
                .email(registeredUser.getEmail())
                .build();

    }


}
