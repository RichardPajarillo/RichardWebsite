package org.example.richardwebsite.controller;

import org.example.richardwebsite.model.*;
import org.example.richardwebsite.repository.*;
import org.example.richardwebsite.service.SecurityService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final BookRepository bookRepository;
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;
    private final SecurityService securityService;

    public CartController(CartRepository cartRepository,
                          BookRepository bookRepository,
                          OrderRepository orderRepository,
                          SecurityService securityService) {

        this.cartRepository = cartRepository;
        this.bookRepository = bookRepository;
        this.orderRepository = orderRepository;
        this.securityService = securityService;
    }

    // core method
    private Cart getCart() {

        User user = securityService.getCurrentUser();

        return cartRepository.findByUser_Id(user.getId())
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    cart.setUser(user);
                    return cartRepository.save(cart);
                });
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

        return Math.round(cart.getTotal() * 100.0) / 100.0;
    }

    @PostMapping("/add/{id}")
    public String addToCart(@PathVariable Long id,
                            @RequestParam(defaultValue = "1") int qty) {

        Book book = bookRepository.findById(id)
                .orElseThrow();

        Cart cart = getCart();

        cart.addItem(book, qty);

        cartRepository.save(cart);

        return "redirect:/cart";
    }

    @GetMapping
    public String viewCart(Model model) {

        Cart cart = getCart();

        model.addAttribute("cart", cart);

        return "cart";
    }

    @GetMapping("/remove/{id}")
    public String removeFromCart(@PathVariable Long id) {

        Cart cart = getCart();

        cart.removeItem(id);

        cartRepository.save(cart);

        return "redirect:/cart";
    }

    @PostMapping("/increase/{bookId}")
    public String increaseQty(@PathVariable Long bookId) {

        Cart cart = getCart();

        cart.getItems().stream()
                .filter(i -> i.getBook().getId().equals(bookId))
                .findFirst()
                .ifPresent(i -> i.setQuantity(i.getQuantity() + 1));

        cartRepository.saveAndFlush(cart);

        return "redirect:/cart";
    }

    @PostMapping("/decrease/{bookId}")
    public String decreaseQty(@PathVariable Long bookId) {

        Cart cart = getCart();

        CartItem target = cart.getItems().stream()
                .filter(i -> i.getBook().getId().equals(bookId))
                .findFirst()
                .orElse(null);

        if (target != null) {

            int newQty = target.getQuantity() - 1;

            if (newQty <= 0) {
                cart.getItems().remove(target);
            } else {
                target.setQuantity(newQty);
            }
        }

        cartRepository.saveAndFlush(cart);

        return "redirect:/cart";
    }

    @PostMapping("/checkout")
    public String checkout() {

        Cart cart = getCart();

        if (cart.getItems().isEmpty()) {
            return "redirect:/cart";
        }

        Order order = new Order();
        double total = 0;

        for (CartItem ci : cart.getItems()) {

            OrderItem orderItem = new OrderItem(
                    ci.getBook().getTitle(),
                    ci.getBook().getPrice(),
                    ci.getQuantity()
            );

            order.addItem(orderItem);

            total += ci.getBook().getPrice() * ci.getQuantity();
        }

        order.setTotal(total);

        orderRepository.save(order);

        cart.getItems().clear();
        cartRepository.save(cart);

        return "redirect:/orders";
    }
}