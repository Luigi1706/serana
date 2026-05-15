package com.arquiweb.grupo3.serana.repositories;

import com.arquiweb.grupo3.serana.entities.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    // ── QUERY METHODS ────────────────────────────────────────────────────────

    /** QM-P1: Obtener perfil del paciente vinculado a un usuario */
    Optional<Paciente> findByUsuarioId(Long usuarioId);

    /** QM-P2: Buscar pacientes por estado anímico */
    List<Paciente> findByEstadoAnimoIgnoreCase(String estadoAnimo);

    /** QM-P3: Buscar pacientes por nombre o apellido */
    List<Paciente> findByNombresContainingIgnoreCaseOrApellidosContainingIgnoreCase(
            String nombres, String apellidos);

    /** QM-P4: Pacientes que prefieren anonimato */
    List<Paciente> findByAnonimatoTrue();

    // ── JPQL ─────────────────────────────────────────────────────────────────

    /**
     * JPQL-P1: Pacientes con nivel de ansiedad igual o superior al umbral indicado.
     * Útil para priorizar atención a casos críticos.
     */
    @Query("SELECT p FROM Paciente p WHERE p.nivelAnsiedad >= :nivel ORDER BY p.nivelAnsiedad DESC")
    List<Paciente> obtenerPacientesCriticos(@Param("nivel") int nivel);

    /**
     * JPQL-P2: Pacientes que tienen al menos una sesión en estado indicado.
     * Útil para filtrar pacientes con sesiones pendientes o completadas.
     */
    @Query("""
        SELECT DISTINCT p FROM Paciente p
        JOIN p.sesiones s
        WHERE s.estadoSesion = :estado
        ORDER BY p.apellidos ASC
    """)
    List<Paciente> findByEstadoSesion(@Param("estado") String estado);

    /**
     * JPQL-P3: Pacientes atendidos por un profesional específico.
     * Permite al profesional ver su lista de pacientes histórica.
     */
    @Query("""
        SELECT DISTINCT p FROM Paciente p
        JOIN p.sesiones s
        WHERE s.profesionalMedico.id = :profesionalId
        ORDER BY p.apellidos ASC
    """)
    List<Paciente> findPacientesPorProfesional(@Param("profesionalId") Long profesionalId);

    /**
     * JPQL-P4: Cuenta el total de pacientes por estado anímico.
     * Útil para el dashboard de administración.
     */
    @Query("SELECT p.estadoAnimo, COUNT(p) FROM Paciente p GROUP BY p.estadoAnimo ORDER BY COUNT(p) DESC")
    List<Object[]> contarPacientesPorEstadoAnimo();

    // ── SQL NATIVAS ───────────────────────────────────────────────────────────

    /**
     * SQL-P1: Pacientes con el mayor número de sesiones completadas.
     * Permite reconocer a los más comprometidos con su proceso terapéutico.
     */
    @Query(value = """
        SELECT p.*, COUNT(s.id) AS total_sesiones
        FROM pacientes p
        LEFT JOIN sesiones s ON s.id_paciente = p.id
        WHERE s.estado_sesion = 'Completada'
        GROUP BY p.id
        ORDER BY total_sesiones DESC
        LIMIT 10
    """, nativeQuery = true)
    List<Paciente> obtenerTopPacientes();

    /**
     * SQL-P2: Pacientes sin ninguna sesión registrada (no han comenzado atención).
     * Permite al admin hacer seguimiento y contactar a quienes no avanzan.
     */
    @Query(value = """
        SELECT p.*
        FROM pacientes p
        WHERE p.id NOT IN (SELECT DISTINCT id_paciente FROM sesiones)
    """, nativeQuery = true)
    List<Paciente> obtenerPacientesSinSesiones();

    /**
     * SQL-P3: Pacientes con nivel de ansiedad alto y sin sesión programada próxima.
     * Detecta casos urgentes sin atención agendada.
     */
    @Query(value = """
        SELECT p.*
        FROM pacientes p
        WHERE p.nivel_ansiedad >= 7
        AND p.id NOT IN (
            SELECT DISTINCT id_paciente FROM sesiones
            WHERE estado_sesion = 'Programada'
            AND fecha >= CURRENT_DATE
        )
    """, nativeQuery = true)
    List<Paciente> obtenerPacientesCriticosSinAtencion();

    /**
     * SQL-P4: Distribución de pacientes por género.
     * Estadística para el panel de administración de Serana.
     */
    @Query(value = """
        SELECT genero, COUNT(*) AS total
        FROM pacientes
        GROUP BY genero
        ORDER BY total DESC
    """, nativeQuery = true)
    List<Object[]> distribucionPorGenero();
}
