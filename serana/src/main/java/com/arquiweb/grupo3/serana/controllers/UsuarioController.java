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

@RestController
@CrossOrigin("*")
@RequestMapping("/arqui_serana")  // El grupo de peticiones que se van a escuchar con este Controller
// http://localhost:8080/arqui_serana
// Controller encargado de la autenticación y gestión de usuarios de Serana:
// registro, login (JWT) y consulta de perfil de usuario.
public class UsuarioController {
    @Autowired
    UsuarioService usuarioService;

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtilService jwtUtilService;

    @PostMapping("/usuarios/register") // http://localhost:8080/arqui_serana/usuarios/register
    public ResponseEntity<UsuarioDTO> register(@RequestBody UsuarioDTO usuario) {
        usuario = usuarioService.add(usuario);
        return new ResponseEntity<>(usuario, HttpStatus.CREATED);
    }

    @PostMapping("/usuarios/login") // http://localhost:8080/arqui_serana/usuarios/login
    public ResponseEntity<TokenDTO> login(@RequestBody Usuario usuario) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(usuario.getCorreo(), usuario.getContrasenia())
        );

        UsuarioSecurity usuarioSecurity = (UsuarioSecurity) userDetailsService.loadUserByUsername(usuario.getCorreo());

        String jwt = jwtUtilService.generateToken(usuarioSecurity);
        Long id = usuarioSecurity.getUsuario().getId();
        String authorities = usuarioSecurity.getUsuario().getAuthorities().stream().
                map(authority -> authority.getRol())
                .collect(Collectors.joining(";", "", ""));

        return new ResponseEntity<>(new TokenDTO(jwt, id, authorities), HttpStatus.OK);
    }

    @GetMapping("/usuarios/{id}") // http://localhost:8080/arqui_serana/usuarios/1
    public ResponseEntity<Usuario> findById(@PathVariable Long id) {
        Usuario found = usuarioService.findById(id);
        if (found == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(found, HttpStatus.OK);
    }

}
