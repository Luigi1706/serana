package com.arquiweb.grupo3.serana.repositories;

import com.arquiweb.grupo3.serana.entities.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ComentarioRepository extends JpaRepository<Comentario, Long> {

    //Obtener todos los comentarios de un post ordenados por fecha
    List<Comentario> findByPostIdOrderByFechaPublicacionAsc(Long postId);

    // Contador de comentario respecto de una publicación
    @Query(value = "SELECT COUNT(*) FROM comentarios WHERE id_post = ?1", nativeQuery = true)
    Long contarComentariosSqlNativo(Long postId);

    //Obtener todos los comentarios de un post, indicando el autor
    @Query("SELECT DISTINCT c FROM Comentario c " +
            "JOIN FETCH c.usuariosComentarios uc " +
            "JOIN FETCH uc.usuario u " +
            "WHERE c.post.id = ?1 " +
            "AND uc.comentarista = true " +
            "ORDER BY c.fechaPublicacion ASC")
    List<Comentario> findComentariosConAutoresPorPostId(Long postId);
}
