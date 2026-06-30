package com.FoodDelivery.FoodDeliveryBackend.repository;

import com.FoodDelivery.FoodDeliveryBackend.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User,String> {
    Optional<User> findByEmail(String email);
}
