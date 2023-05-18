package br.com.ferraz.springstudy.infra.security;

import br.com.ferraz.springstudy.domain.user.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;

@Service
public class TokenService {

    @Value("${api.security.token.secret}") // Busca a propriedade no application.properties
    private String secret;

    public String generateToken(User user) {
        try {
            var algorithm = Algorithm.HMAC256(secret);
            String token = JWT.create()
                            .withIssuer("Spring Study")
                            .withSubject(user.getLogin()) // Identifica o usuário
                            .withClaim("id", user.getId()) // Podemos adicionar qualquer informação relevante usando o withClaim
                            .withExpiresAt(generateExpirationDate()) // É importante definir um tempo de expiração do token
                            .sign(algorithm);
            return token;
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar um JWT", exception);
        }
    }

    private Instant generateExpirationDate() {
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }

}
