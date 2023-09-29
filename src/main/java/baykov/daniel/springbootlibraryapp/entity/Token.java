package baykov.daniel.springbootlibraryapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "token")
public class Token extends BaseEntity {

    @Column(nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    private LocalDateTime confirmedAt;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(nullable = false, name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(nullable = false, name = "token_type_id")
    private TokenType tokenType;

    public Token(User user, TokenType tokenType) {
        this.setToken(UUID.randomUUID().toString());
        this.setCreatedAt(LocalDateTime.now());
        this.setExpiresAt(LocalDateTime.now().plusMinutes(60));
        this.setUser(user);
        this.setTokenType(tokenType);
    }
}
