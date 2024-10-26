package com.medic.system.config;

import com.medic.system.services.UserService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {

    private final UserService userService;

    public SecurityConfiguration(UserService userService) {
        this.userService = userService;
    }

//    @Bean
//    public InMemoryUserDetailsManager userDetailsInMemory() {
//        UserDetails user1 = User
//                .withUsername("user1")
//                .password("$2a$12$VcsUDlmbLDczCbM2Cm5sAetkRVDSyHHzisAig1B.ypT/GW8XrcTq.")
//                .build();
//        UserDetails user2 = User.withUsername("user2")
//                .password("$2a$12$BUppzBU81H1M.doHy7GMBegE1SoVSbmM5BH1F/sd5MiVti1EXL6IG")
//                .build();
//        return new InMemoryUserDetailsManager(user1, user2);
//    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/public/**").permitAll()  // Allow access to /public/**
                        .anyRequest().authenticated()               // Require authentication for other requests
                )
                .formLogin(form -> form
                        .successHandler(customAuthenticationSuccessHandler()) // Use custom success handler
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login?logout")
                );

        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return (request, response, authentication) -> {
            response.sendRedirect("/users"); // Redirect to /users after successful login
        };
    }

}
