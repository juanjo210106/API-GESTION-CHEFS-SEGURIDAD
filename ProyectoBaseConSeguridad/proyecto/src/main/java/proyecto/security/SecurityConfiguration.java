package proyecto.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private JWTAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authConf) throws Exception {
        return authConf.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        System.out.println("🔴 1. CARGANDO SEGURIDAD (Versión: A prueba de bombas)");

        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                // --- 1. RUTAS PÚBLICAS ---
                // Login
                .requestMatchers("/login", "/auth/login").permitAll()
                
                // ===> AQUÍ ESTÁ EL CAMBIO <===
                // Añadimos explícitamente "/chef" (sin barra), "/chef/" (con barra) y "/chef/**" (con cosas detrás)
                .requestMatchers(HttpMethod.POST, "/chef", "/chef/", "/chef/**").permitAll() 
                .requestMatchers(HttpMethod.POST, "/cliente", "/cliente/", "/cliente/**").permitAll()
                
                // Consultas públicas
                .requestMatchers(HttpMethod.GET, "/chef/listadoActivos").permitAll()
                .requestMatchers(HttpMethod.GET, "/chef/*").permitAll()
                .requestMatchers("/noticia/**").permitAll()
                
                // Swagger
                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()

                // --- 2. RUTAS PRIVADAS ---
                
                // ADMIN
                .requestMatchers("/admin/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.GET, "/chef").hasAuthority("ADMIN") 
                .requestMatchers(HttpMethod.GET, "/cliente/**").hasAuthority("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/banear/**", "/desbanear/**").hasAuthority("ADMIN")

                // CHEF
                .requestMatchers(HttpMethod.PUT, "/chef/**").hasAuthority("CHEF")
                .requestMatchers(HttpMethod.DELETE, "/chef/**").hasAuthority("CHEF")
                .requestMatchers("/chef/miPerfil", "/chef/activar", "/chef/desactivar").hasAuthority("CHEF")
                .requestMatchers("/servicio/misServiciosChef").hasAuthority("CHEF")

                // CLIENTE
                .requestMatchers(HttpMethod.PUT, "/cliente/**").hasAuthority("CLIENTE")
                .requestMatchers(HttpMethod.DELETE, "/cliente/**").hasAuthority("CLIENTE")
                .requestMatchers("/cliente/miPerfil").hasAuthority("CLIENTE")
                .requestMatchers("/servicio/contratar/**").hasAuthority("CLIENTE")
                .requestMatchers("/servicio/puntuar/**").hasAuthority("CLIENTE")
                .requestMatchers("/servicio/misServiciosCliente").hasAuthority("CLIENTE")

                // --- 3. RESTO BLOQUEADO ---
                .anyRequest().authenticated()
            );

        // Filtro activado
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}