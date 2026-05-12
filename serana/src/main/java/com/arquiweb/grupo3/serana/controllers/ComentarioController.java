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
@RequestMapping("/arqui_serana") // http://localhost:8080/arqui_serana
public class ComentarioController {

    @Autowired
    ComentarioService comentarioService;

    @GetMapping("/comentarios") // http://localhost:8080/arqui_serana/comentarios
    public ResponseEntity<List<Comentario>> listAll(){
        return new ResponseEntity<>(comentarioService.listAll(), HttpStatus.OK);
    }

    @PostMapping("/comentarios") // http://localhost:8080/arqui_serana/comentarios
    public ResponseEntity<ComentarioDTO> create(@RequestBody ComentarioDTO comentarioDTO){
        ComentarioDTO newComentarioDTO = comentarioService.addDTO(comentarioDTO);
        return new ResponseEntity<>(newComentarioDTO, HttpStatus.CREATED);
    }

    @GetMapping("/comentarios/ordenarporfecha/{postId}") // http://localhost:8080/arqui_serana/comentarios/ordenarporfecha/1
    public ResponseEntity<List<Comentario>> listarPorPostOrdenadoPorFecha(@PathVariable("postId") Long postId){
        List<Comentario> comentariosOrdenados = comentarioService.listByPostIdOrderByFecha(postId);
        return new ResponseEntity<>(comentariosOrdenados,HttpStatus.OK);
    }

    @GetMapping("comentarios/cantidad/{postId}") // http://localhost:8080/arqui_serana/comentarios/cantidad/1
    public ResponseEntity<Long> contarComentariosdePost(@PathVariable("postId") Long postId){
        Long cantidad = comentarioService.contarComentariosDePost(postId);
        return new ResponseEntity<>(cantidad,HttpStatus.OK);
    }

    @DeleteMapping("/comentarios/{comentarioId}") // http://localhost:8080/arqui_serana/comentarios/3
    public ResponseEntity<HttpStatus> delete(@PathVariable("comentarioId") Long postId){
        comentarioService.delete(postId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
