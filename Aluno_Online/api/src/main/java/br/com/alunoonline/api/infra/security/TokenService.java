package br.com.alunoonline.api.infra.security;

import br.com.alunoonline.api.model.Usuario;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;

@Service
public class TokenService implements InitializingBean {

    @Value("${api.security.token.secret}")
    private String secret;

    @Override
    public void afterPropertiesSet() {
        if (secret == null || secret.getBytes(StandardCharsets.UTF_8).length < 32) {
            throw new IllegalStateException("JWT_SECRET deve conter pelo menos 32 bytes");
        }
    }

    public String gerarToken(Usuario usuario) {
        try {
            return JWT.create()
                    .withIssuer("API Aluno Online")
                    .withSubject(usuario.getLogin())
                    .withExpiresAt(Instant.now().plusSeconds(7200))
                    .sign(Algorithm.HMAC256(secret));
        } catch (JWTCreationException exception) {
            throw new IllegalStateException("Não foi possível gerar o token JWT", exception);
        }
    }

    public String getSubject(String tokenJWT) {
        String subject = JWT.require(Algorithm.HMAC256(secret))
                .withIssuer("API Aluno Online")
                .build()
                .verify(tokenJWT)
                .getSubject();
        if (subject == null || subject.isBlank()) {
            throw new com.auth0.jwt.exceptions.JWTVerificationException("JWT sem subject");
        }
        return subject;
    }
}
