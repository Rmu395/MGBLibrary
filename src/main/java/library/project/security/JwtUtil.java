package library.project.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secretKey; // Secret key – do podpisywania tokenu
    @Value("${jwt.expiration}")
    private long expirationMs; // czas ważności tokenu

    // Jwts.SIG.HS256.key()
    //Metoda do generowania tokenu:
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", getUserRoles(userDetails)); //dodanie niestandardowego Claim (oświadczenia) dla roli usera.
        Date now = new Date();
        //data wygaśnięcia tokenu:
        Date expirationDate = new Date(now.getTime() + expirationMs);
        //budowanie tokenu:
        return Jwts.builder()
                .header().type("JWT").and()      //opcjonalny typ tokenu
                .claims().add(claims)               //niestandardowe Claimsy – dodatkowa informacja o userze
                .and()                              //dalsze budowanie:
                .subject(userDetails.getUsername()) //podmiot tokenu – u nas username/login
                .issuedAt(now)                      //data wystawienia
                .expiration(expirationDate)         //data wygaśnięcia
                .signWith(getSigningKey())          //Podpisanie tokenu przy użyciu klucza
                .compact();                         //token jako ciąg znaków
    }

    public String extractUsername(String token) {
        return getClaims(token).getSubject(); //pobranie nazwy usera z tokenu
    }

    //Walidacja tokenu – czy Username i podpis się zgadza, oraz czy token nie wygasł
    public boolean validateToken(String token, UserDetails userDetails) {
        try {
            final String username = extractUsername(token);
            if (!username.equals(userDetails.getUsername())) {
                return false;
            }
            return !getClaims(token).getExpiration().before(new Date());
        } catch (JwtException e) {
            return false;
        }
    }

    private Claims getClaims(String token) {
        Jws<Claims> jwsClaims = Jwts.parser()   //parser tokena
                .verifyWith(getSigningKey())    //SPRWADZANIE PODPISU TOKENA!!
                .build()                        //budowanie parsera
                .parseSignedClaims(token);      //Gdy podpis prawidłowy sparoswanie tokena
        return jwsClaims.getPayload(); // zwrócenie claims (body) – dane usera – ciało tokenu.
    }

    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64URL.decode(secretKey); // dekoduje secretKey z base64url z pliku
        return Keys.hmacShaKeyFor(keyBytes); // generuje SecretKey dla algorytmuHMAC-SHA
        // HMAC to metoda szyfrowania, która wykorzystuje funkcję skrótu (np. SHA-256) i klucz tajny do generowania podpisu
    }

    private List<String> getUserRoles (UserDetails userDetails){
        return userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
    }
}