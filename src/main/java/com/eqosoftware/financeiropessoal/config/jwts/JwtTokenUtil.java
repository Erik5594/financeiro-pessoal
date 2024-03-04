package com.eqosoftware.financeiropessoal.config.jwts;

import com.eqosoftware.financeiropessoal.config.security.UsuarioSistema;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * Created by erik on 28/01/2022.
 */

@Component
public class JwtTokenUtil {

    public static final long JWT_TOKEN_VALIDITY = 72 * 60 * 60;//72 horas de validade

    @Value("${jwt.secret}")
    private String secret;

    //retorna o username do token jwt
    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    //retorna a data de expiração do token jwt
    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);

    }

    //para retornar qualquer informação do token nos iremos precisar da secret key
    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    //checa se o token está expirado
    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    //gera token para user
    public String generateToken(UsuarioSistema usuarioSistema) {
        Map<String, Object> claims = new HashMap<>();
        if (Objects.nonNull(usuarioSistema.getUsuario())) {
            claims.put("guid", usuarioSistema.getUsuario().getUuid().toString());
            if (Objects.nonNull(usuarioSistema.getUsuario().getTenant())
                    && StringUtils.isNotBlank(usuarioSistema.getUsuario().getTenant().getNomeSchema())) {
                claims.put("tenantId", usuarioSistema.getUsuario().getTenant().getNomeSchema());
            }
        }
        return doGenerateToken(claims, usuarioSistema.getUsername());
    }

    //Cria o token e define tempo de expiração pra ele
    private String doGenerateToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    //valida o token
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }


}
