package com.FoodDelivery.FoodDeliveryBackend.serviceImpl;

import com.FoodDelivery.FoodDeliveryBackend.io.CartRequest;
import com.FoodDelivery.FoodDeliveryBackend.io.CartResponse;
import com.FoodDelivery.FoodDeliveryBackend.model.Cart;
import com.FoodDelivery.FoodDeliveryBackend.repository.CartRepository;
import com.FoodDelivery.FoodDeliveryBackend.service.CartService;
import com.FoodDelivery.FoodDeliveryBackend.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;

    private final UserService userService;

    @Override
    public CartResponse addToCart(CartRequest request) {

        String loggedInUserId = userService.findByUserId();
        Optional<Cart> cartOptional = cartRepository.findByUserId(loggedInUserId);
        Cart cart = cartOptional.orElseGet(()->new Cart(loggedInUserId,new HashMap<>()));
        Map<String,Integer> cartItems = cart.getItems();
        cartItems.put(request.getFoodId(),cartItems.getOrDefault(request.getFoodId(),0)+1);
        cart.setItems(cartItems);
        cart = cartRepository.save(cart);
        return mapToResponse(cart);
    }

    @Override
    public CartResponse getCart() {
        String loggedInUserId = userService.findByUserId();
        Cart cart = cartRepository.findByUserId(loggedInUserId)
                .orElse(new Cart(null,loggedInUserId,new HashMap<>()));
        return mapToResponse(cart);
    }

    @Override
    public void clearCart() {
        String loggedInUserId = userService.findByUserId();
        cartRepository.deleteByUserId(loggedInUserId);
    }

    @Override
    public CartResponse removeFromCart(CartRequest request) {
        String loggedInUserId = userService.findByUserId();
        Cart cart = cartRepository.findByUserId(loggedInUserId)
                .orElseThrow(()-> new RuntimeException("Cart Not Found"));
        Map<String,Integer> cartItems = cart.getItems();
        if(cartItems.containsKey(request.getFoodId())){
            int currentQty = cartItems.get(request.getFoodId());
            if(currentQty>0){
                cartItems.put(request.getFoodId(),currentQty-1);
            }else{
                cartItems.remove(request.getFoodId());
            }

            cart = cartRepository.save(cart);
        }
        return mapToResponse(cart);
    }

    private CartResponse mapToResponse(Cart cart){
        return CartResponse.builder()
                .id(cart.getId())
                .userId(cart.getUserId())
                .items(cart.getItems())
                .build();
    }
}
