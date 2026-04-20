package org.example.richardwebsite.controller;

import org.example.richardwebsite.model.*;
import org.example.richardwebsite.repository.BookRepository;
import org.example.richardwebsite.repository.CartRepository;
import org.example.richardwebsite.repository.OrderRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final BookRepository bookRepository;
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;

    public CartController(CartRepository cartRepository,
                          BookRepository bookRepository,
                          OrderRepository orderRepository) {

        this.cartRepository = cartRepository;
        this.bookRepository = bookRepository;
        this.orderRepository = orderRepository;
    }

    private Cart getCart() {

        return cartRepository.findAll()
                .stream()
                .findFirst()
                .orElseGet(() -> {
                    Cart cart = new Cart();
                    return cartRepository.save(cart);
                });
    }

    // Add to cart
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

    // View cart
    @GetMapping
    public String viewCart(Model model) {

        Cart cart = getCart();
        System.out.println("CART ITEMS: " + cart.getItems().size());
        model.addAttribute("cart", cart);

        return "cart";
    }

    // Remove item
    @GetMapping("/remove/{id}")
    public String removeFromCart(@PathVariable Long id) {

        Cart cart = getCart();

        cart.removeItem(id);

        cartRepository.save(cart);

        return "redirect:/cart";
    }


    //Add Quantity

    @PostMapping("/increase/{bookId}")
    public String increaseQty(@PathVariable Long bookId) {

        Cart cart = getCart();

        for (CartItem item : cart.getItems()) {
            if (item.getBook().getId().equals(bookId)) {
                item.setQuantity(item.getQuantity() + 1);
                break;
            }
        }

        cartRepository.save(cart);
        return "redirect:/cart";
    }

    // Checkout
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