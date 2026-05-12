package com.arquiweb.grupo3.serana.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfiguration {
    private static final String[] AUTH_WHITELIST = {

            // -- Swagger
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/swagger-resources/**",

            // -- Login
            "/arqui_serana/usuarios/login/**",

            // -- Registro de nuevos usuarios
            "/arqui_serana/usuarios/register/**",
    };

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Autowired
    JwtRequestFilter jwtRequestFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        http.cors(Customizer.withDefaults());
        http.csrf(AbstractHttpConfigurer::disable);

        http.authorizeHttpRequests(
                (auth) -> auth
                        .requestMatchers(AUTH_WHITELIST).permitAll()

                        // --- Pacientes ---
                        // Un paciente (ROLE_PACIENTE) puede ver y editar su propio perfil
                        // Un profesional (ROLE_PROFESIONAL) también puede ver pacientes
                        // El admin (ROLE_ADMIN) tiene acceso total
                        .requestMatchers(HttpMethod.GET, "/arqui_serana/pacientes/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_PROFESIONAL", "ROLE_PACIENTE")
                        .requestMatchers(HttpMethod.POST, "/arqui_serana/pacientes/**").hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/arqui_serana/pacientes/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_PACIENTE")
                        .requestMatchers(HttpMethod.DELETE, "/arqui_serana/pacientes/**").hasAnyAuthority("ROLE_ADMIN")

                        // --- Profesionales Médicos ---
                        .requestMatchers(HttpMethod.GET, "/arqui_serana/profesionales/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_PROFESIONAL", "ROLE_PACIENTE")
                        .requestMatchers(HttpMethod.POST, "/arqui_serana/profesionales/**").hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/arqui_serana/profesionales/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_PROFESIONAL")
                        .requestMatchers(HttpMethod.DELETE, "/arqui_serana/profesionales/**").hasAnyAuthority("ROLE_ADMIN")

                        // --- Sesiones ---
                        .requestMatchers(HttpMethod.GET, "/arqui_serana/sesiones/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_PROFESIONAL", "ROLE_PACIENTE")
                        .requestMatchers(HttpMethod.POST, "/arqui_serana/sesiones/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_PROFESIONAL", "ROLE_PACIENTE")
                        .requestMatchers(HttpMethod.PUT, "/arqui_serana/sesiones/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_PROFESIONAL")
                        .requestMatchers(HttpMethod.DELETE, "/arqui_serana/sesiones/**").hasAnyAuthority("ROLE_ADMIN")

                        // --- Horarios ---
                        .requestMatchers(HttpMethod.GET, "/arqui_serana/horarios/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_PROFESIONAL", "ROLE_PACIENTE")
                        .requestMatchers(HttpMethod.POST, "/arqui_serana/horarios/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_PROFESIONAL")
                        .requestMatchers(HttpMethod.PUT, "/arqui_serana/horarios/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_PROFESIONAL")
                        .requestMatchers(HttpMethod.DELETE, "/arqui_serana/horarios/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_PROFESIONAL")

                        // --- Posts ---
                        .requestMatchers(HttpMethod.GET, "/arqui_serana/posts/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_PROFESIONAL", "ROLE_PACIENTE")
                        .requestMatchers(HttpMethod.POST, "/arqui_serana/posts/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_PROFESIONAL", "ROLE_PACIENTE")
                        .requestMatchers(HttpMethod.PUT, "/arqui_serana/posts/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_PROFESIONAL", "ROLE_PACIENTE")
                        .requestMatchers(HttpMethod.DELETE, "/arqui_serana/posts/**").hasAnyAuthority("ROLE_ADMIN")

                        // --- Comentarios ---
                        .requestMatchers(HttpMethod.GET, "/serana/comentarios/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_PROFESIONAL", "ROLE_PACIENTE")
                        .requestMatchers(HttpMethod.POST, "/serana/comentarios/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_PROFESIONAL", "ROLE_PACIENTE")
                        .requestMatchers(HttpMethod.DELETE, "/serana/comentarios/**").hasAnyAuthority("ROLE_ADMIN")

                        // --- Recursos Educativos ---
                        .requestMatchers(HttpMethod.GET, "/arqui_serana/recursos/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_PROFESIONAL", "ROLE_PACIENTE")
                        .requestMatchers(HttpMethod.POST, "/arqui_serana/recursos/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_PROFESIONAL")
                        .requestMatchers(HttpMethod.PUT, "/arqui_serana/recursos/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_PROFESIONAL")
                        .requestMatchers(HttpMethod.DELETE, "/arqui_serana/recursos/**").hasAnyAuthority("ROLE_ADMIN")

                        // --- Configuracion ---
                        .requestMatchers(HttpMethod.GET, "/arqui_serana/configuraciones/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_PROFESIONAL", "ROLE_PACIENTE")
                        .requestMatchers(HttpMethod.PUT, "/arqui_serana/configuraciones/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_PROFESIONAL", "ROLE_PACIENTE")

                        .anyRequest().authenticated()
        );

        http.sessionManagement(
                (session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        );

        return http.build();
    }
}
