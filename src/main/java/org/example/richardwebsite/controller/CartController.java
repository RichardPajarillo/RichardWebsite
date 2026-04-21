package org.example.richardwebsite.controller;

import org.example.richardwebsite.model.*;
import org.example.richardwebsite.repository.*;
import org.example.richardwebsite.service.OrderService;
import org.example.richardwebsite.service.SecurityService;
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

    public CartController(CartRepository cartRepository,
                          BookRepository bookRepository,
                          SecurityService securityService,
                          OrderService orderService) {
        this.cartRepository = cartRepository;
        this.bookRepository = bookRepository;
        this.securityService = securityService;
        this.orderService = orderService;
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
    public String viewCart(Model model) {
        model.addAttribute("cart", getCart());
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

    @GetMapping("/checkout")
    public String checkout(Model model) {
        Cart cart = getCart();
        model.addAttribute("cart", cart);
        return "checkout"; // must match checkout.html
    }

    @PostMapping("/checkout")
    public String checkout() {

        User user = securityService.getCurrentUser();
        Cart cart = getCart();

        orderService.createOrder(user, cart);

        cart.getItems().clear();
        cartRepository.save(cart);

        return "redirect:/orders";
    }
}