package br.com.alunoonline.api.infra.security;

import br.com.alunoonline.api.model.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class TokenServiceTest {

    private static final String SECRET = "test-secret-with-at-least-32-bytes-long";
    private TokenService tokenService;

    @BeforeEach
    void setUp() throws Exception {
        tokenService = new TokenService();
        Field secret = TokenService.class.getDeclaredField("secret");
        secret.setAccessible(true);
        secret.set(tokenService, SECRET);
        tokenService.afterPropertiesSet();
    }

    @Test
    void createsAndValidatesTokenForUser() {
        String token = tokenService.gerarToken(new Usuario("ana", "hashed"));
        assertEquals("ana", tokenService.getSubject(token));
    }

    @Test
    void rejectsWrongSignatureAndExpiredTokens() {
        String wrongSignature = JWT.create().withIssuer("API Aluno Online")
                .withSubject("ana").withExpiresAt(Instant.now().plusSeconds(60))
                .sign(Algorithm.HMAC256("another-secret-with-at-least-32-bytes"));
        String expired = JWT.create().withIssuer("API Aluno Online")
                .withSubject("ana").withExpiresAt(Date.from(Instant.now().minusSeconds(60)))
                .sign(Algorithm.HMAC256(SECRET));

        assertThrows(JWTVerificationException.class, () -> tokenService.getSubject(wrongSignature));
        assertThrows(JWTVerificationException.class, () -> tokenService.getSubject(expired));
    }

    @Test
    void rejectsWeakSecret() throws Exception {
        Field secret = TokenService.class.getDeclaredField("secret");
        secret.setAccessible(true);
        secret.set(tokenService, "short");
        assertThrows(IllegalStateException.class, () -> tokenService.afterPropertiesSet());
    }

    @Test
    void bcryptStoresOnlyHashAndVerifiesOriginalPassword() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = encoder.encode("senha-correta");
        assertNotEquals("senha-correta", hash);
        assertTrue(encoder.matches("senha-correta", hash));
        assertFalse(encoder.matches("senha-incorreta", hash));
    }
}
