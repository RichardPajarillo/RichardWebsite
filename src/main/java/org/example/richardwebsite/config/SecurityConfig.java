package org.example.richardwebsite.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(auth -> auth

                        // 🌐 PUBLIC
                        .requestMatchers("/login", "/register", "/css/**").permitAll()

                        // 👑 ADMIN ONLY
                        .requestMatchers("/showNewBookForm").hasRole("ADMIN")
                        .requestMatchers("/saveBook").hasRole("ADMIN")
                        .requestMatchers("/deleteBook/**").hasRole("ADMIN")
                        .requestMatchers("/showFormForUpdate/**").hasRole("ADMIN")

                        // 🛒 USER + ADMIN (must be logged in)
                        .requestMatchers("/cart/**").authenticated()

                        // 📦 ORDERS (ONLY LOGGED IN USERS)
                        .requestMatchers("/orders").authenticated()

                        // CHECK OUT MY GOD
                        .requestMatchers("/checkout").authenticated()
                        .requestMatchers("/checkout", "/cart/**").authenticated()

                        .requestMatchers("/orders/**").authenticated()
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // 🔒 EVERYTHING ELSE
                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/login")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutSuccessUrl("/login")
                );

        return http.build();
    }

    // ⚠️ only for development/testing
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}