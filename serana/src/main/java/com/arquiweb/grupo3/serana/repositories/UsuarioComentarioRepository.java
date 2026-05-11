package com.arquiweb.grupo3.serana.repositories;

import com.arquiweb.grupo3.serana.entities.UsuarioComentario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UsuarioComentarioRepository extends JpaRepository<UsuarioComentario, Long> {

    @Query("SELECT uc FROM UsuarioComentario uc WHERE uc.comentario.id = :comentarioId")
    List<UsuarioComentario> findByComentarioId(Long comentarioId);

    // Obtener todos los comentarios en que participó un usuario
    @Query("SELECT uc FROM UsuarioComentario uc WHERE uc.usuario.id = :usuarioId")
    List<UsuarioComentario> findByUsuarioId(Long usuarioId);

    // Obtener el registro concreto usuario-comentario (útil para dar/quitar like)
    @Query("SELECT uc FROM UsuarioComentario uc " +
            "WHERE uc.usuario.id = :usuarioId AND uc.comentario.id = :comentarioId")
    Optional<UsuarioComentario> findByUsuarioIdAndComentarioId(Long usuarioId, Long comentarioId);

    // Contar likes de un comentario
    @Query("SELECT COUNT(uc) FROM UsuarioComentario uc " +
            "WHERE uc.comentario.id = :comentarioId AND uc.liked = true")
    Long contarLikesPorComentario(Long comentarioId);

    // Verificar si un usuario ya le dio like a un comentario
    @Query("SELECT COUNT(uc) > 0 FROM UsuarioComentario uc " +
            "WHERE uc.usuario.id = :usuarioId AND uc.comentario.id = :comentarioId AND uc.liked = true")
    boolean usuarioYaDioLike(Long usuarioId, Long comentarioId);

}
