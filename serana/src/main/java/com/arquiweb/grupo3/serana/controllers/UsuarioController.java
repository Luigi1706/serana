package com.arquiweb.grupo3.serana.controllers;

import com.arquiweb.grupo3.serana.dtos.TokenDTO;
import com.arquiweb.grupo3.serana.dtos.UsuarioDTO;
import com.arquiweb.grupo3.serana.entities.Usuario;
import com.arquiweb.grupo3.serana.security.JwtUtilService;
import com.arquiweb.grupo3.serana.security.UsuarioSecurity;
import com.arquiweb.grupo3.serana.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

/**
 * Controlador REST para la gestión de Usuarios de Serana.
 *
 * Maneja el registro de nuevos usuarios, inicio de sesión con JWT
 * y la consulta del perfil de usuario.
 *
 * Los endpoints /register y /login son públicos (AUTH_WHITELIST en SecurityConfiguration).
 * GET /usuarios/{id} requiere autenticación JWT.
 *
 * Base URL: /arqui_serana/usuarios
 */
@RestController
@CrossOrigin("*")
@RequestMapping("/arqui_serana")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtilService jwtUtilService;


    @PostMapping("/usuarios/register")
    public ResponseEntity<UsuarioDTO> register(@RequestBody UsuarioDTO usuarioDTO) {
        UsuarioDTO registrado = usuarioService.add(usuarioDTO);
        return new ResponseEntity<>(registrado, HttpStatus.CREATED);
    }

    @PostMapping("/usuarios/login")
    public ResponseEntity<TokenDTO> login(@RequestBody Usuario usuario) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        usuario.getCorreo(),
                        usuario.getContrasenia()
                )
        );

        UsuarioSecurity usuarioSecurity =
                (UsuarioSecurity) userDetailsService.loadUserByUsername(usuario.getCorreo());

        String jwt = jwtUtilService.generateToken(usuarioSecurity);
        Long id = usuarioSecurity.getUsuario().getId();
        String authorities = usuarioSecurity.getUsuario().getAuthorities().stream()
                .map(authority -> authority.getRol())
                .collect(Collectors.joining(";", "", ""));

        return new ResponseEntity<>(new TokenDTO(jwt, id, authorities), HttpStatus.OK);
    }

    @GetMapping("/usuarios/{id}")
    public ResponseEntity<Usuario> findById(@PathVariable Long id) {
        return new ResponseEntity<>(usuarioService.findById(id), HttpStatus.OK);
    }
}
