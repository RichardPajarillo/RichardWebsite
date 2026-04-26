package org.example.richardwebsite.controller;

import org.example.richardwebsite.model.*;
import org.example.richardwebsite.repository.*;
import org.example.richardwebsite.service.OrderService;
import org.example.richardwebsite.service.SecurityService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartRepository cartRepository;
    private final BookRepository bookRepository;
    private final SecurityService securityService;
    private final OrderService orderService;
    private final CartItemRepository cartItemRepository;

    public CartController(CartRepository cartRepository,
                          BookRepository bookRepository,
                          SecurityService securityService,
                          OrderService orderService, CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.bookRepository = bookRepository;
        this.securityService = securityService;
        this.orderService = orderService;
        this.cartItemRepository = cartItemRepository;
    }

    private Cart getCart() {
        User user = securityService.getCurrentUser();

        return cartRepository.findByUser_Id(user.getId())
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUser(user);
                    return cartRepository.save(cart);
                });
    }



    @GetMapping
    public String viewCart(
            @RequestParam(name = "page", defaultValue = "0") int page,
            Model model) {

        User user = securityService.getCurrentUser();
        Cart cart = getCart(); // Your existing method to get/create cart

        int size = 5; // Items per page
        Page<CartItem> itemPage = cartItemRepository.findByCart_Id(cart.getId(), PageRequest.of(page, size));

        model.addAttribute("cart", cart);
        model.addAttribute("itemPage", itemPage);
        model.addAttribute("items", itemPage.getContent());
        model.addAttribute("baseUrl", "/cart");

        return "cart";
    }

    @PostMapping("/add/{id}")
    public String addToCart(@PathVariable Long id,
                            @RequestParam(defaultValue = "1") int qty) {

        Book book = bookRepository.findById(id).orElseThrow();
        Cart cart = getCart();

        cart.addItem(book, qty);
        cartRepository.save(cart);

        return "redirect:/cart";
    }

    @GetMapping("/remove/{id}")
    public String remove(@PathVariable Long id) {

        Cart cart = getCart();
        cart.removeItem(id);
        cartRepository.save(cart);

        return "redirect:/cart";
    }

    @PostMapping("/update")
    @ResponseBody
    public Double updateQty(@RequestParam Long bookId,
                            @RequestParam int qty) {

        Cart cart = getCart();

        cart.getItems().stream()
                .filter(i -> i.getBook().getId().equals(bookId))
                .findFirst()
                .ifPresent(i -> i.setQuantity(qty));

        cartRepository.saveAndFlush(cart);

        return cart.getTotal();
    }

    //checkout section here


}