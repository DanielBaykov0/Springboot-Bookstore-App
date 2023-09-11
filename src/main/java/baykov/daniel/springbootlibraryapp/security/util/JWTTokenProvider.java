package baykov.daniel.springbootlibraryapp.security.util;

import baykov.daniel.springbootlibraryapp.exception.LibraryHTTPException;
import baykov.daniel.springbootlibraryapp.utils.PropertyVariables;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JWTTokenProvider {

    private final PropertyVariables propertyVariables;

    public String generateToken(Authentication authentication) {
        String email = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + propertyVariables.getJwtExpirationDate());
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(key())
                .compact();
    }

    public String generateTokenFromEmail(String email) {
        Date expireDate = new Date(new Date().getTime() + propertyVariables.getJwtExpirationDate());
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(key())
                .compact();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(propertyVariables.getJwtSecret()));
    }

    public String getEmail(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean validateToke(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parse(token);
            return true;
        } catch (MalformedJwtException e) {
            throw new LibraryHTTPException(HttpStatus.BAD_REQUEST, "Invalid JWT Token");
        } catch (ExpiredJwtException e) {
            throw new LibraryHTTPException(HttpStatus.BAD_REQUEST, "Expired JWT Token");
        } catch (UnsupportedJwtException e) {
            throw new LibraryHTTPException(HttpStatus.BAD_REQUEST, "Unsupported JWT Token");
        } catch (IllegalArgumentException e) {
            throw new LibraryHTTPException(HttpStatus.BAD_REQUEST, "JWT claim string is empty");
        }
    }
}
