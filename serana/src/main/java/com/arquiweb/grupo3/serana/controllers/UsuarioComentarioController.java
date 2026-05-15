package com.arquiweb.grupo3.serana.controllers;

// Controller encargado de gestionar las interacciones usuario-comentario en Serana:
// dar like, registrar al comentarista, consultar y eliminar interacciones.

import com.arquiweb.grupo3.serana.dtos.UsuarioComentarioDTO;
import com.arquiweb.grupo3.serana.services.UsuarioComentarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/arqui_serana")
public class UsuarioComentarioController {

    @Autowired
    private UsuarioComentarioService usuarioComentarioService;

    @GetMapping("/usuarios-comentarios/usuario/{usuarioId}")
    public ResponseEntity<List<UsuarioComentarioDTO>> findByUsuarioId(@PathVariable Long usuarioId) {
        return new ResponseEntity<>(usuarioComentarioService.findByUsuarioId(usuarioId), HttpStatus.OK);
    }

    @GetMapping("/usuarios-comentarios/comentario/{comentarioId}")
    public ResponseEntity<List<UsuarioComentarioDTO>> findByComentarioId(@PathVariable Long comentarioId) {
        return new ResponseEntity<>(usuarioComentarioService.findByComentarioId(comentarioId), HttpStatus.OK);
    }

    @GetMapping("/usuarios-comentarios/likes/{comentarioId}")
    public ResponseEntity<Long> contarLikes(@PathVariable Long comentarioId) {
        return new ResponseEntity<>(usuarioComentarioService.contarLikes(comentarioId), HttpStatus.OK);
    }

    /** Toggle like de un usuario sobre un comentario */
    @PatchMapping("/usuarios-comentarios/like")
    public ResponseEntity<UsuarioComentarioDTO> toggleLike(
            @RequestParam Long usuarioId,
            @RequestParam Long comentarioId) {
        return new ResponseEntity<>(usuarioComentarioService.toggleLike(usuarioId, comentarioId), HttpStatus.OK);
    }

    @PostMapping("/usuarios-comentarios")
    public ResponseEntity<UsuarioComentarioDTO> add(@RequestBody UsuarioComentarioDTO dto) {
        return new ResponseEntity<>(usuarioComentarioService.add(dto), HttpStatus.CREATED);
    }

    @DeleteMapping("/usuarios-comentarios/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        usuarioComentarioService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
