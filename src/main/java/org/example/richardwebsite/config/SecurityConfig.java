package org.example.richardwebsite.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.example.richardwebsite.config.CustomAccessDeniedHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomAccessDeniedHandler accessDeniedHandler;

    public SecurityConfig(CustomAccessDeniedHandler accessDeniedHandler) {
        this.accessDeniedHandler = accessDeniedHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(auth -> auth

                        // 🌐 PUBLIC
                        .requestMatchers("/", "/login", "/register", "/css/**").permitAll()

                        // 👑 ADMIN ONLY
                        .requestMatchers(
                                "/admin/**",
                                "/showNewBookForm",
                                "/saveBook",
                                "/deleteBook/**",
                                "/showFormForUpdate/**"
                        ).hasRole("ADMIN")

                        // 🔐 AUTHENTICATED USERS
                        .requestMatchers(
                                "/cart/**",
                                "/orders/**",
                                "/checkout"
                        ).authenticated()

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
                        .permitAll()
                )

                // 🔥 THIS IS THE FIX
                .exceptionHandling(ex -> ex
                        .accessDeniedHandler(accessDeniedHandler)
                );

        return http.build();
    }

    /* Use BCrypt in real apps
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    } */

    // ⚠️ only for development/testing
    @Bean public PasswordEncoder passwordEncoder() {
     return NoOpPasswordEncoder.getInstance();
    }
}