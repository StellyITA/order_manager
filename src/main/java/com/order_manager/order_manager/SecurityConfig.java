package com.order_manager.order_manager;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        
        http.authorizeHttpRequests(req -> req
                                            .requestMatchers(HttpMethod.POST,"/menu")
                                            .hasRole("ADMIN")
                                            .requestMatchers(HttpMethod.GET,"/menu/**")
                                            .permitAll())
            .httpBasic(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable());

        return http.build();
    }

    @Bean
    UserDetailsService testUsersService(PasswordEncoder passwordEncoder) {
        User.UserBuilder users = User.builder();
        
        UserDetails admin = users.username("admin")
        .password(passwordEncoder.encode("password"))
        .roles("ADMIN")
        .build();

        UserDetails user = users.username("username")
        .password(passwordEncoder.encode("12345678"))
        .roles("USER")
        .build();

        return new InMemoryUserDetailsManager(admin, user);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
