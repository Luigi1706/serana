package com.arquiweb.grupo3.serana.services;

import com.arquiweb.grupo3.serana.dtos.ComentarioDTO;
import com.arquiweb.grupo3.serana.entities.Comentario;

import java.util.List;

public interface ComentarioService {
    public Comentario findById(Long id);
    public List<Comentario> findByPostIdOrderByFecha(Long postId);
    public List<Comentario> findComentariosConAutoresPorPostId(Long postId);
    public Long contarComentariosPorPost(Long postId);
    public ComentarioDTO add(ComentarioDTO comentarioDTO);
    public void delete(Long id);
}
