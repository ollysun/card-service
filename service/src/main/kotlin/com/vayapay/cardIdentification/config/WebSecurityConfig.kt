package com.vayapay.cardIdentification.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.InMemoryUserDetailsManager
import org.springframework.security.web.SecurityFilterChain


@EnableWebSecurity
@Configuration
class WebSecurityConfig {

    //Todo update web security configuration settings
    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain {
        //TODO secure the /card-registration endpoint with appropriate mechanism
        http.authorizeRequests()
            .antMatchers("/card-registration", "/js/**", "/styles/**", "/img/**").permitAll()
            .anyRequest().authenticated()
        return http.build()
    }

    @Bean
    fun userDetailsService(): UserDetailsService {
        val users: User.UserBuilder = User.builder()
        val manager = InMemoryUserDetailsManager()
        manager.createUser(users.username("user").password("password").roles("USER").build())
        manager.createUser(
            users.username("admin").password(passwordEncoder()?.encode("userSecret")).roles("USER", "ADMIN").build()
        )
        return manager
    }
    
    @Bean
    fun passwordEncoder(): PasswordEncoder? {
        return BCryptPasswordEncoder()
    }
}