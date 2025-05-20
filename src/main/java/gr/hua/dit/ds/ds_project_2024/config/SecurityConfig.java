package gr.hua.dit.ds.ds_project_2024.config;

import gr.hua.dit.ds.ds_project_2024.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    UserService userService;
    UserDetailsService userDetailsService;
    BCryptPasswordEncoder passwordEncoder;

    public SecurityConfig(UserService userService, UserDetailsService userDetailsService, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/", "/home", "/error/**", "/register", "/shelter/new", "/veterinarian/new", "/citizen/new", "/saveUser", "/actuator/health/**", "/actuator/**", "/images/**", "/js/**", "/css/**").permitAll()
                        .requestMatchers("/users/**", "/user/**", "/citizen", "/shelter", "/veterinarian", "/healthCheck/").hasRole("ADMIN")
                        .requestMatchers("healthCheck/").hasRole("VETERINARIAN")
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .loginPage("/login")
                        .failureUrl("/login?error=true")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                )
                .logout((logout) -> logout
                        .deleteCookies("JSESSIONID")
                        .permitAll());
        return http.build();
    }
}