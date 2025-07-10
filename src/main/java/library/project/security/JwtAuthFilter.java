package library.project.security;

import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter { //filtr zostanie wykonany tylko raz dla każdego przychodzącego żądania
    @Autowired
    private JwtUtil jwtUtil; //Do zaimplementowania - operacje na tokenach JWT.
    @Autowired
    private UserDetailsService userDetailsService; // Serwis do ładowania danych użytkownika.

    //Główna metoda filtra, która jest wywoływana dla każdego przychodzącego żądania HTTP:
    @Override
    protected void doFilterInternal(HttpServletRequest request, //przychodzące żądanie HTTP
                                    HttpServletResponse response, //Odpowiedź serwera
                                    FilterChain filterChain) //łańcuch filtrów – można przekazać do następnego filtra
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization"); //token JWT jest przesyłany w tym nagłówku
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // Brak nagłówka Authorization lub niepoprawny format -> przekazujemy żądanie dalej (nie autentykujemy)
            filterChain.doFilter(request, response);
            return;
        }
        //////
        //Gdy Istnieje nagłówek w formacie "Bearer <token>":
        String token = authHeader.substring(7); //sam token
        try {
            String username = jwtUtil.extractUsername(token);
            // Sprawdzamy, czy użytkownik nie jest już uwierzytelniony, np.. przez inne mechanizmy
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                //szczegóły użytkownika z bazy danych
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                //weryfikacja tokena na podstawie danych użytkownika
                if (jwtUtil.validateToken(token, userDetails)) {
                    //Jeśli token ważny, tworzymy obiekt autentykacji (UsernamePasswordAuthenticationToken)
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    //Dodatkowe informacje z requesta -ip klienta(id sesji nie ma)
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    // Ustawiamy kontekst bezpieczeństwa – użytkownik zalogowany
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (JwtException e) {
            //brak Authentication w SecurityContext
        }
        //żądanie idzie dalej w łańcuchu filtrów
        filterChain.doFilter(request, response);
    }
}