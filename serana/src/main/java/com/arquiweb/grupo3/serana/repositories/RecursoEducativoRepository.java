package com.arquiweb.grupo3.serana.repositories;

import com.arquiweb.grupo3.serana.entities.RecursoEducativo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RecursoEducativoRepository extends JpaRepository<RecursoEducativo, Long> {

    // ── QUERY METHODS ────────────────────────────────────────────────────────

    /** QM-RE1: Buscar por tipo de contenido (video, artículo, audio, etc.) */
    List<RecursoEducativo> findByTipoContenidoIgnoreCase(String tipoContenido);

    /** QM-RE2: Buscar por título (búsqueda parcial) */
    List<RecursoEducativo> findByTituloContainingIgnoreCase(String titulo);

    /** QM-RE3: Recursos ordenados del más reciente al más antiguo */
    List<RecursoEducativo> findAllByOrderByFechaPublicacionDesc();

    /** QM-RE4: Tipos de contenido distintos disponibles */
    // Implementado en JPQL-RE4 (Spring Data no soporta DISTINCT en query methods para campos simples)

    // ── JPQL ─────────────────────────────────────────────────────────────────

    /**
     * JPQL-RE1: Recursos publicados en los últimos N días.
     */
    @Query("SELECT r FROM RecursoEducativo r WHERE r.fechaPublicacion >= :desde ORDER BY r.fechaPublicacion DESC")
    List<RecursoEducativo> obtenerRecursosRecientes(@Param("desde") java.time.LocalDateTime desde);

    /**
     * JPQL-RE2: Recursos con más sesiones asociadas (más usados).
     * Útil para recomendar contenido popular.
     */
    @Query("""
        SELECT r FROM RecursoEducativo r
        LEFT JOIN r.sesionesRecursosEducativos sre
        GROUP BY r
        ORDER BY COUNT(sre) DESC
    """)
    List<RecursoEducativo> obtenerRecursosMasUsados();

    /**
     * JPQL-RE3: Recursos por tipo de contenido, ordenados por uso.
     */
    @Query("""
        SELECT r FROM RecursoEducativo r
        WHERE LOWER(r.tipoContenido) = LOWER(:tipo)
        ORDER BY r.fechaPublicacion DESC
    """)
    List<RecursoEducativo> buscarPorTipoOrdenado(@Param("tipo") String tipo);

    /**
     * JPQL-RE4: Lista de tipos de contenido distintos.
     * Alimenta los filtros del frontend.
     */
    @Query("SELECT DISTINCT r.tipoContenido FROM RecursoEducativo r ORDER BY r.tipoContenido ASC")
    List<String> listarTiposDeContenido();

    // ── SQL NATIVAS ───────────────────────────────────────────────────────────

    /**
     * SQL-RE1: Top 10 recursos más usados en sesiones.
     */
    @Query(value = """
        SELECT re.*, COUNT(sre.id) AS veces_usado
        FROM recursos_educativos re
        LEFT JOIN sesiones_recursos_educativos sre ON sre.id_recurso_educativo = re.id
        GROUP BY re.id
        ORDER BY veces_usado DESC
        LIMIT 10
    """, nativeQuery = true)
    List<RecursoEducativo> obtenerTopRecursos();

    /**
     * SQL-RE2: Recursos que nunca han sido vinculados a una sesión.
     * Útil para identificar contenido sin usar.
     */
    @Query(value = """
        SELECT * FROM recursos_educativos
        WHERE id NOT IN (SELECT DISTINCT id_recurso_educativo FROM sesiones_recursos_educativos)
    """, nativeQuery = true)
    List<RecursoEducativo> obtenerRecursosSinUsar();

    /**
     * SQL-RE3: Conteo de recursos por tipo de contenido.
     */
    @Query(value = """
        SELECT tipo_contenido, COUNT(*) AS total
        FROM recursos_educativos
        GROUP BY tipo_contenido
        ORDER BY total DESC
    """, nativeQuery = true)
    List<Object[]> contarRecursosPorTipo();

    /**
     * SQL-RE4: Recursos publicados en el último mes.
     */
    @Query(value = """
        SELECT * FROM recursos_educativos
        WHERE fecha_publicacion >= CURRENT_TIMESTAMP - INTERVAL '30 days'
        ORDER BY fecha_publicacion DESC
    """, nativeQuery = true)
    List<RecursoEducativo> obtenerRecursosDelUltimoMes();
}
