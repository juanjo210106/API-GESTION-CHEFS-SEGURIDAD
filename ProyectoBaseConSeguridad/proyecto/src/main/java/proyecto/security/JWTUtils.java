package proyecto.security;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import proyecto.entity.Actor;
import proyecto.service.ActorService;
import proyecto.service.AdminService;
import proyecto.service.ChefService;
import proyecto.service.ClienteService;

@Component
public class JWTUtils {

    @Autowired
    private ActorService actorService;

    @Autowired
    @Lazy
    private AdminService adminService;

    @Autowired
    @Lazy
    private ClienteService clienteService;

    @Autowired
    @Lazy
    private ChefService chefService;

    @Value("${jwt.secret}")
    private String JWT_FIRMA;

    @Value("${jwt.expiration}")
    private long EXTENCION_TOKEN;

    public String getToken(HttpServletRequest request) {
        String tokenBearer = request.getHeader("Authorization");
        if (StringUtils.hasText(tokenBearer) && tokenBearer.startsWith("Bearer ")) {
            return tokenBearer.substring(7);
        }
        return null;
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(JWT_FIRMA).parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            throw new AuthenticationCredentialsNotFoundException("JWT ha experido o no es valido");
        }
    }

    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Date fechaActual = new Date();
        Date fechaExpiracion = new Date(fechaActual.getTime() + EXTENCION_TOKEN);
        String rol = authentication.getAuthorities().iterator().next().getAuthority();
        String token = Jwts.builder().setSubject(username).setIssuedAt(fechaActual).setExpiration(fechaExpiracion)
                .claim("rol", rol).signWith(SignatureAlgorithm.HS512, JWT_FIRMA).compact();
        return token;
    }

    public String getUsernameOfToken(String token) {
        return Jwts.parser().setSigningKey(JWT_FIRMA).parseClaimsJws(token).getBody().getSubject();
    }

    public <T> T userLogin() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (StringUtils.isEmpty(username)) {
            return null;
        }
        Optional<Actor> actorO = actorService.findByUsername(username);
        if (!actorO.isPresent()) {
            return null;
        }
        Actor actor = actorO.get();
        switch (actor.getRol()) {
            case ADMIN:
                return (T) adminService.findByUsername(username).orElse(null);
            case CHEF:
                return (T) chefService.findByUsername(username).orElse(null);
            case CLIENTE:
                return (T) clienteService.findByUsername(username).orElse(null);
            default:
                return null;
        }
    }

}