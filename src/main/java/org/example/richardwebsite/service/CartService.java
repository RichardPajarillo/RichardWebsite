package org.example.richardwebsite.service;

import org.example.richardwebsite.model.Cart;
import org.example.richardwebsite.model.User;
import org.example.richardwebsite.repository.CartRepository;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final SecurityService securityService;

    public CartService(CartRepository cartRepository,
                       SecurityService securityService) {
        this.cartRepository = cartRepository;
        this.securityService = securityService;
    }

    public Cart getCart() {

        User user = securityService.getCurrentUser();

        return cartRepository.findByUser_Id(user.getId())
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUser(user);
                    return cartRepository.save(cart);
                });
    }
    public Cart save(Cart cart) {
        return cartRepository.save(cart);
    }
}