package library.project.security;

import jakarta.servlet.Filter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration          //adnotacja konfiguracji – definicja beanów
@EnableWebSecurity      //włączamy mechanizm WebSecurity!
@EnableMethodSecurity   //Włączamy zabezpieczania metod – np. @PreAuthorize
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,                  // obiekt do łańcucha ustawień obsługi bezpieczeństwa
            JwtAuthFilter jwtAuthFilter,        // Niestandardowy filtr do implementacji - weryfikacja tokenu!
            AuthenticationProvider authProvider // Dostawca uwierzytelnienia
    ) throws Exception {
        http
                .csrf(csrf -> csrf.disable())               //w rest api – brak sesji i ciasteczek, wyłączamy ochronę //Cross-Site Request Forgery
                .authorizeHttpRequests(auth -> auth //TODO: to change
//                                .requestMatchers("/api/auth/admin/**").hasRole("ADMIN")
//                                .requestMatchers("/api/payments/checkout").hasRole("USER")
//                                .requestMatchers("/api/auth/**").permitAll()
//                                .requestMatchers("/api/payments/**").permitAll()
//                                .requestMatchers("/api/**").hasRole("USER")
//                                .requestMatchers(HttpMethod.POST,"/api/vehicles").hasRole("ADMIN")
//                                .requestMatchers(HttpMethod.GET,"/api/vehicles/available").hasRole("USER")
//                                .requestMatchers(HttpMethod.GET,"/api/vehicles").hasRole("ADMIN")
//                        .requestMatchers("api/admin/**").hasRole("ADMIN")
                                .anyRequest().authenticated() //każdy inny request wymaga zalogowania
                )
                //Brak sesji, rest api z tokenami jwt!
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //Dostawca uwierzytelnienia
                .authenticationProvider(authProvider)
                //Weryfikacja tokenu przed standardowym sposobem autoryzacji!
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider( //Bean z dostawcą autentykacji
                                                          UserDetailsService userDetailsService, //Serwis do ładowania danych usera
                                                          PasswordEncoder passwordEncoder //Encoder
    )
    {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(); //Obiekt do Autentykacja z bazy
        provider.setUserDetailsService(userDetailsService); //serwis używany do autentykacji
        provider.setPasswordEncoder(passwordEncoder); //encoder
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() { //Używamy bcrypt do hashowania hasla
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config //konfiguracja uwierzytelniania
    ) throws Exception {
        return config.getAuthenticationManager(); //dostęp do AuthenticationManagera.
    }
}