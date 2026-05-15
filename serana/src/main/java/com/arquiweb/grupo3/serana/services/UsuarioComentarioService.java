package com.arquiweb.grupo3.serana.services;

import com.arquiweb.grupo3.serana.dtos.UsuarioComentarioDTO;

import java.util.List;

public interface UsuarioComentarioService {
    /** Registrar interacción (like o marcar como comentarista) */
    UsuarioComentarioDTO add(UsuarioComentarioDTO dto);
    /** Obtener todas las interacciones de un usuario */
    List<UsuarioComentarioDTO> findByUsuarioId(Long usuarioId);
    /** Obtener todas las interacciones sobre un comentario */
    List<UsuarioComentarioDTO> findByComentarioId(Long comentarioId);
    /** Contar likes de un comentario */
    Long contarLikes(Long comentarioId);
    /** Toggle like: si ya existe cambia liked, si no existe lo crea */
    UsuarioComentarioDTO toggleLike(Long usuarioId, Long comentarioId);
    /** Eliminar interacción */
    void delete(Long id);
}
