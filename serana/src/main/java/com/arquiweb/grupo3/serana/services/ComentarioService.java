package com.arquiweb.grupo3.serana.services;

import com.arquiweb.grupo3.serana.dtos.ComentarioDTO;
import com.arquiweb.grupo3.serana.entities.Comentario;

import java.util.List;

public interface ComentarioService {
    Comentario add(Comentario comentario); //CREATE
    List<Comentario> listAll(); //READ
    void delete(Long id); //DELETE
    Comentario findById(Long id);
    List<Comentario> listByPostIdOrderByFecha(Long postId);
    Long contarComentariosDePost(Long id);
    ComentarioDTO addDTO(ComentarioDTO comentarioDTO);
}
