package baykov.daniel.springbootbookstoreapp.security.util;

import baykov.daniel.springbootbookstoreapp.exception.LibraryHTTPException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

import static baykov.daniel.springbootbookstoreapp.constant.ErrorMessages.*;

@Component
public class JWTTokenProvider {

    @Value("${app.jwt-secret}")
    private String jwtSecret;

    @Value("${app.jwt-expiration-milliseconds}")
    private long jwtExpirationDate;

    @Value("${app.jwtRefreshExpiration}")
    private Long refreshTokenDurationMs;

    public String generateAccessToken(Authentication authentication) {
        String email = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + jwtExpirationDate);
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(key())
                .compact();
    }

    public String generateRefreshToken(Authentication authentication) {
        String email = authentication.getName();
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + refreshTokenDurationMs);
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(expireDate)
                .signWith(key())
                .compact();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    public String getEmail(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key())
                    .build()
                    .parse(token);
            return true;
        } catch (MalformedJwtException e) {
            throw new LibraryHTTPException(HttpStatus.BAD_REQUEST, INVALID_JWT_TOKEN);
        } catch (ExpiredJwtException e) {
            throw new LibraryHTTPException(HttpStatus.BAD_REQUEST, EXPIRED_JWT_TOKEN);
        } catch (UnsupportedJwtException e) {
            throw new LibraryHTTPException(HttpStatus.BAD_REQUEST, UNSUPPORTED_JWT_TOKEN);
        } catch (IllegalArgumentException e) {
            throw new LibraryHTTPException(HttpStatus.BAD_REQUEST, JWT_CLAIM_EMPTY);
        }
    }
}
