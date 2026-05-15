package com.arquiweb.grupo3.serana.repositories;

import com.arquiweb.grupo3.serana.entities.Sesion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface SesionRepository extends JpaRepository<Sesion, Long> {

    // ── QUERY METHODS ────────────────────────────────────────────────────────

    /** QM-S1: Sesiones de un paciente ordenadas por fecha descendente */
    List<Sesion> findByPacienteIdOrderByFechaDescHoraDesc(Long pacienteId);

    /** QM-S2: Sesiones de un profesional en un estado determinado */
    List<Sesion> findByProfesionalMedicoIdAndEstadoSesion(Long profesionalMedicoId, String estadoSesion);

    /** QM-S3: Sesiones a partir de una fecha (agenda futura) */
    List<Sesion> findByFechaGreaterThanEqualOrderByFechaAscHoraAsc(LocalDate fecha);

    /** QM-S4: Sesiones de un paciente en un estado determinado */
    List<Sesion> findByPacienteIdAndEstadoSesion(Long pacienteId, String estadoSesion);

    // ── JPQL ─────────────────────────────────────────────────────────────────

    /**
     * JPQL-S1: Historial completo del paciente ordenado por fecha.
     */
    @Query("""
        SELECT s FROM Sesion s
        WHERE s.paciente.id = :pacienteId
        ORDER BY s.fecha DESC, s.hora DESC
    """)
    List<Sesion> obtenerHistorialPaciente(@Param("pacienteId") Long pacienteId);

    /**
     * JPQL-S2: Sesiones programadas de un profesional.
     */
    @Query("""
        SELECT s FROM Sesion s
        WHERE s.profesionalMedico.id = :profesionalId
        AND s.estadoSesion = 'Programada'
        AND s.fecha >= CURRENT_DATE
        ORDER BY s.fecha ASC, s.hora ASC
    """)
    List<Sesion> obtenerSesionesProgramadasProfesional(@Param("profesionalId") Long profesionalId);

    /**
     * JPQL-S3: Sesiones entre dos fechas para un profesional.
     */
    @Query("""
        SELECT s FROM Sesion s
        WHERE s.profesionalMedico.id = :profesionalId
        AND s.fecha BETWEEN :fechaInicio AND :fechaFin
        ORDER BY s.fecha ASC, s.hora ASC
    """)
    List<Sesion> obtenerSesionesPorProfesionalEnRango(
            @Param("profesionalId") Long profesionalId,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin);

    /**
     * JPQL-S4: Contar sesiones completadas de un profesional.
     */
    @Query("SELECT COUNT(s) FROM Sesion s WHERE s.profesionalMedico.id = :profesionalId AND s.estadoSesion = 'Completada'")
    Long contarSesionesCompletadasProfesional(@Param("profesionalId") Long profesionalId);

    // ── SQL NATIVAS ───────────────────────────────────────────────────────────

    /**
     * SQL-S1: Profesionales con más de 10 sesiones próximas (sobrecargados).
     * CORRECCIÓN: tipo de retorno Object[] en lugar de List<Sesion> (era ClassCastException).
     */
    @Query(value = """
        SELECT id_profesional_medico, COUNT(*) AS total
        FROM sesiones
        WHERE fecha >= CURRENT_DATE
        GROUP BY id_profesional_medico
        HAVING COUNT(*) > 10
    """, nativeQuery = true)
    List<Object[]> detectarProfesionalesSobrecargados();

    /**
     * SQL-S2: Sesiones de hoy para todos los profesionales.
     */
    @Query(value = """
        SELECT s.*
        FROM sesiones s
        WHERE s.fecha = CURRENT_DATE
        ORDER BY s.hora ASC
    """, nativeQuery = true)
    List<Sesion> obtenerSesionesDeHoy();

    /**
     * SQL-S3: Tasa de sesiones completadas vs canceladas.
     */
    @Query(value = """
        SELECT estado_sesion, COUNT(*) AS total
        FROM sesiones
        GROUP BY estado_sesion
        ORDER BY total DESC
    """, nativeQuery = true)
    List<Object[]> obtenerEstadisticasPorEstado();

    /**
     * SQL-S4: Pacientes con más sesiones en el último mes.
     */
    @Query(value = """
        SELECT id_paciente, COUNT(*) AS total_sesiones
        FROM sesiones
        WHERE fecha >= CURRENT_DATE - INTERVAL '30 days'
        GROUP BY id_paciente
        ORDER BY total_sesiones DESC
        LIMIT 10
    """, nativeQuery = true)
    List<Object[]> pacientesMasActivosUltimoMes();
}
