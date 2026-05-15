package com.arquiweb.grupo3.serana.repositories;

import com.arquiweb.grupo3.serana.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    // ── QUERY METHODS ────────────────────────────────────────────────────────

    /** QM-POST1: Posts del más reciente al más antiguo */
    List<Post> findAllByOrderByFechaCreacionDesc();

    /** QM-POST2: Posts de un usuario por su id directo en la entidad */
    List<Post> findByUsuarioId(Long usuarioId);

    /** QM-POST3: Posts en modo anónimo */
    List<Post> findByModoAnonimoTrue();

    /** QM-POST4: Posts por tema */
    List<Post> findByTemaContainingIgnoreCase(String tema);

    // ── JPQL ─────────────────────────────────────────────────────────────────

    /**
     * JPQL-POST1: Posts con más interacciones (likes + participaciones).
     * Útil para la sección de tendencias.
     */
    @Query("""
        SELECT p FROM Post p
        LEFT JOIN p.usuariosPosts up
        GROUP BY p
        ORDER BY COUNT(up) DESC
    """)
    List<Post> obtenerPostsTendencia();

    /**
     * JPQL-POST2: Posts de un usuario via tabla usuarios_posts.
     */
    @Query("""
        SELECT DISTINCT p FROM Post p
        JOIN p.usuariosPosts up
        WHERE up.usuario.id = :idUsuario
        ORDER BY p.fechaCreacion DESC
    """)
    List<Post> obtenerPostsPorUsuarioId(@Param("idUsuario") Long idUsuario);

    /**
     * JPQL-POST3: Posts con al menos un comentario.
     * Útil para filtrar posts activos con discusión.
     */
    @Query("""
        SELECT DISTINCT p FROM Post p
        WHERE SIZE(p.comentarios) > 0
        ORDER BY p.fechaCreacion DESC
    """)
    List<Post> obtenerPostsConComentarios();

    /**
     * JPQL-POST4: Posts ordenados por cantidad de comentarios.
     */
    @Query("""
        SELECT p FROM Post p
        LEFT JOIN p.comentarios c
        GROUP BY p
        ORDER BY COUNT(c) DESC
    """)
    List<Post> obtenerPostsPorCantidadComentarios();

    // ── SQL NATIVAS ───────────────────────────────────────────────────────────

    /**
     * SQL-POST1: Contar likes de un post.
     * CORRECCIÓN: backtick MySQL → comillas dobles PostgreSQL en columna "liked".
     */
    @Query(value = """
        SELECT COUNT(*) FROM usuarios_posts
        WHERE id_post = :postId AND liked = true
    """, nativeQuery = true)
    Long contarLikesPorPostId(@Param("postId") Long postId);

    /**
     * SQL-POST2: Posts con más likes (top 10).
     */
    @Query(value = """
        SELECT p.*, COUNT(up.id) AS total_likes
        FROM posts p
        LEFT JOIN usuarios_posts up ON up.id_post = p.id AND up.liked = true
        GROUP BY p.id
        ORDER BY total_likes DESC
        LIMIT 10
    """, nativeQuery = true)
    List<Post> obtenerTopPostsLikeados();

    /**
     * SQL-POST3: Posts publicados en los últimos N días.
     */
    @Query(value = """
        SELECT * FROM posts
        WHERE fecha_creacion >= CURRENT_DATE - INTERVAL '7 days'
        ORDER BY fecha_creacion DESC
    """, nativeQuery = true)
    List<Post> obtenerPostsRecientes();

    /**
     * SQL-POST4: Cantidad de posts por usuario (ranking de participación).
     */
    @Query(value = """
        SELECT id_usuario, COUNT(*) AS total_posts
        FROM posts
        GROUP BY id_usuario
        ORDER BY total_posts DESC
        LIMIT 10
    """, nativeQuery = true)
    List<Object[]> rankingUsuariosPorPosts();
}
