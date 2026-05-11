package com.arquiweb.grupo3.serana.controllers;

import com.arquiweb.grupo3.serana.dtos.ComentarioDTO;
import com.arquiweb.grupo3.serana.entities.Comentario;
import com.arquiweb.grupo3.serana.services.ComentarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/arqui_serana")
public class ComentarioController {

    @Autowired
    ComentarioService comentarioService;

    @GetMapping("/comentarios/post/{postId}")
    public ResponseEntity<List<Comentario>> findByPost(@PathVariable Long postId) {
        return new ResponseEntity<>(comentarioService.findByPostIdOrderByFecha(postId), HttpStatus.OK);
    }

    @GetMapping("/comentarios/post/{postId}/autores")
    public ResponseEntity<List<Comentario>> findByPostConAutores(@PathVariable Long postId) {
        return new ResponseEntity<>(comentarioService.findComentariosConAutoresPorPostId(postId), HttpStatus.OK);
    }

    @GetMapping("/comentarios/post/{postId}/total")
    public ResponseEntity<Long> contarComentarios(@PathVariable Long postId) {
        return new ResponseEntity<>(comentarioService.contarComentariosPorPost(postId), HttpStatus.OK);
    }

    @PostMapping("/comentarios")
    public ResponseEntity<ComentarioDTO> add(@RequestBody ComentarioDTO comentarioDTO) {
        return new ResponseEntity<>(comentarioService.add(comentarioDTO), HttpStatus.CREATED);
    }

    @DeleteMapping("/comentarios/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        comentarioService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}

