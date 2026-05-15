package com.arquiweb.grupo3.serana.repositories;

import com.arquiweb.grupo3.serana.entities.UsuarioComentario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UsuarioComentarioRepository extends JpaRepository<UsuarioComentario, Long> {

    // ── QUERY METHODS ────────────────────────────────────────────────────────

    /** QM-UC1: Todos los registros de interacción de un usuario con comentarios */
    List<UsuarioComentario> findByUsuarioId(Long usuarioId);

    /** QM-UC2: Todas las interacciones sobre un comentario específico */
    List<UsuarioComentario> findByComentarioId(Long comentarioId);

    /** QM-UC3: Interacción de un usuario sobre un comentario concreto */
    Optional<UsuarioComentario> findByUsuarioIdAndComentarioId(Long usuarioId, Long comentarioId);

    /** QM-UC4: Solo los likes de un comentario */
    List<UsuarioComentario> findByComentarioIdAndLikedTrue(Long comentarioId);

    // ── JPQL ─────────────────────────────────────────────────────────────────

    /**
     * JPQL-UC1: Contar likes de un comentario.
     * Se usa para mostrar el contador de likes en la UI.
     */
    @Query("SELECT COUNT(uc) FROM UsuarioComentario uc WHERE uc.comentario.id = :comentarioId AND uc.liked = true")
    Long contarLikesPorComentario(@Param("comentarioId") Long comentarioId);

    /**
     * JPQL-UC2: Verificar si un usuario ya dio like a un comentario.
     * Evita likes duplicados del mismo usuario.
     */
    @Query("SELECT COUNT(uc) > 0 FROM UsuarioComentario uc WHERE uc.usuario.id = :usuarioId AND uc.comentario.id = :comentarioId AND uc.liked = true")
    boolean yaLikeoComentario(@Param("usuarioId") Long usuarioId, @Param("comentarioId") Long comentarioId);

    /**
     * JPQL-UC3: Comentarios que un usuario ha dado like, con datos del comentario.
     * Útil para el historial de interacciones del usuario.
     */
    @Query("""
        SELECT uc FROM UsuarioComentario uc
        JOIN FETCH uc.comentario c
        WHERE uc.usuario.id = :usuarioId AND uc.liked = true
        ORDER BY c.fechaPublicacion DESC
    """)
    List<UsuarioComentario> findComentariosLikeadosPorUsuario(@Param("usuarioId") Long usuarioId);

    /**
     * JPQL-UC4: Usuarios que comentaron en un post (via UsuarioComentario.comentarista=true).
     * Útil para mostrar participantes de un post.
     */
    @Query("""
        SELECT DISTINCT uc.usuario FROM UsuarioComentario uc
        WHERE uc.comentario.post.id = :postId AND uc.comentarista = true
    """)
    List<Object> findComentaristasDePost(@Param("postId") Long postId);

    // ── SQL NATIVAS ───────────────────────────────────────────────────────────

    /**
     * SQL-UC1: Top comentarios con más likes de todo el sistema.
     * Útil para destacar los comentarios más valorados.
     */
    @Query(value = """
        SELECT uc.id_comentario, COUNT(*) AS total_likes
        FROM usuarios_comentarios uc
        WHERE uc.liked = true
        GROUP BY uc.id_comentario
        ORDER BY total_likes DESC
        LIMIT 10
    """, nativeQuery = true)
    List<Object[]> topComentariosMasLikeados();

    /**
     * SQL-UC2: Usuarios más activos en comentarios (más comentarios publicados).
     * Útil para el ranking de participación en la comunidad.
     */
    @Query(value = """
        SELECT uc.id_usuario, COUNT(*) AS total_comentarios
        FROM usuarios_comentarios uc
        WHERE uc.comentarista = true
        GROUP BY uc.id_usuario
        ORDER BY total_comentarios DESC
        LIMIT 10
    """, nativeQuery = true)
    List<Object[]> usuariosMasActivosEnComentarios();

    /**
     * SQL-UC3: Contar total de likes y comentarios de un usuario.
     * Útil para el perfil de actividad del usuario.
     */
    @Query(value = """
        SELECT
            SUM(CASE WHEN liked = true THEN 1 ELSE 0 END) AS total_likes_dados,
            SUM(CASE WHEN comentarista = true THEN 1 ELSE 0 END) AS total_comentarios
        FROM usuarios_comentarios
        WHERE id_usuario = :usuarioId
    """, nativeQuery = true)
    List<Object[]> resumenActividadComentariosUsuario(@Param("usuarioId") Long usuarioId);

    /**
     * SQL-UC4: Verificar si ya existe el vínculo usuario-comentario.
     * Evita duplicados en la tabla de interacción.
     */
    @Query(value = """
        SELECT COUNT(*) > 0
        FROM usuarios_comentarios
        WHERE id_usuario = :usuarioId AND id_comentario = :comentarioId
    """, nativeQuery = true)
    boolean existeInteraccion(@Param("usuarioId") Long usuarioId, @Param("comentarioId") Long comentarioId);
}
