package com.arquiweb.grupo3.serana.controllers;


import com.arquiweb.grupo3.serana.dtos.TokenDTO;
import com.arquiweb.grupo3.serana.dtos.UsuarioDTO;
import com.arquiweb.grupo3.serana.entities.Usuario;
import com.arquiweb.grupo3.serana.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@CrossOrigin("*")
@RequestMapping("/arqui_serana")  // El grupo de peticiones que se van a escuchar con este Controller
// http://localhost:8080/arqui_serana
public class UsuarioController {
    @Autowired
    UsuarioService usuarioService;

    @Autowired



    @PostMapping("/usuarios/register")
    public ResponseEntity<UsuarioDTO> register(@RequestBody UsuarioDTO usuario) {
        usuario = usuarioService.add(usuario);
        return new ResponseEntity<>(usuario, HttpStatus.CREATED);
    }




    @GetMapping("/usuarios/{id}")
    public ResponseEntity<Usuario> findById(@PathVariable Long id) {
        Usuario found = usuarioService.findById(id);
        if (found == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(found, HttpStatus.OK);
    }

}
