package com.arquiweb.grupo3.serana.repositories;

import com.arquiweb.grupo3.serana.dtos.PostFeedDTO;
import com.arquiweb.grupo3.serana.entities.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    //Listar posts del más reciente al más antigüo
    List<Post> findAllByOrderByFechaCreacionDesc();

    //Obtener post de un usuario por su id
    @Query(value = """
           SELECT p.* 
           FROM posts p
           JOIN usuarios_posts up ON p.id = up.id_post
           WHERE up.id_usuario = :idUsuario
           """, nativeQuery = true)
    List<Post> obtenerPostsporUsuarioId(Long usuarioId);

    // Obtener los posts con más interacciones
    @Query("""
           SELECT p
           FROM Post p
           LEFT JOIN p.usuariosPosts up
           GROUP BY p
           ORDER BY COUNT(up) DESC
           """)
    List<Post> obtenerPostsTendencia();

    @Query(value = "SELECT COUNT(*) FROM usuarios_posts WHERE id_post = ?1 AND `like` = true", nativeQuery = true)
    Long contarLikesPorPostId(Long postId);
}
