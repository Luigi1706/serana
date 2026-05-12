package com.arquiweb.grupo3.serana.repositories;

import com.arquiweb.grupo3.serana.entities.ProfesionalMedico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProfesionalMedicoRepository extends JpaRepository<ProfesionalMedico, Long> {

    List<ProfesionalMedico> findByEspecialidadIgnoreCase(String especialidad);

    Optional<ProfesionalMedico> findByUsuarioId(Long usuarioId);

    List<ProfesionalMedico> findByNombresContainingIgnoreCaseOrApellidosContainingIgnoreCase(
            String nombres, String apellidos);

    List<ProfesionalMedico> findByLugarTrabajoContainingIgnoreCase(String lugarTrabajo);

    @Query("""
            SELECT DISTINCT pm FROM ProfesionalMedico pm
            JOIN pm.horarios h
            WHERE h.disponible = true
              AND h.fecha >= CURRENT_DATE
              AND LOWER(pm.especialidad) = LOWER(:especialidad)
            ORDER BY pm.apellidos ASC
            """)
    List<ProfesionalMedico> buscarDisponiblesPorEspecialidad(@Param("especialidad") String especialidad);

    @Query("""
            SELECT DISTINCT pm FROM ProfesionalMedico pm
            LEFT JOIN FETCH pm.horarios h
            WHERE pm.id = :id
              AND (h IS NULL
                   OR (h.disponible = true AND h.fecha >= CURRENT_DATE))
            """)
    Optional<ProfesionalMedico> findByIdConHorariosDisponibles(@Param("id") Long id);

    @Query("""
            SELECT pm FROM ProfesionalMedico pm
            LEFT JOIN pm.sesiones s
            WHERE s.estadoSesion = 'Completada'
            GROUP BY pm
            ORDER BY COUNT(s) DESC
            """)
    List<ProfesionalMedico> obtenerProfesionalesMasActivos();

    @Query("""
            SELECT DISTINCT pm.especialidad FROM ProfesionalMedico pm
            ORDER BY pm.especialidad ASC
            """)
    List<String> listarEspecialidadesDisponibles();

    @Query(value = """
            SELECT pm.*
            FROM profesionales_medicos pm
            LEFT JOIN sesiones s ON s.id_profesional_medico = pm.id
            GROUP BY pm.id
            ORDER BY COUNT(s.id) DESC
            LIMIT 10
            """, nativeQuery = true)
    List<ProfesionalMedico> obtenerTopProfesionales();

    @Query(value = """
            SELECT pm.*
            FROM profesionales_medicos pm
            WHERE pm.id NOT IN (
                SELECT DISTINCT h.id_profesional_medico
                FROM horarios h
            )
            """, nativeQuery = true)
    List<ProfesionalMedico> obtenerProfesionalesSinHorarios();

    @Query(value = """
            SELECT pm.especialidad,
                   COUNT(s.id)            AS total_sesiones,
                   COUNT(DISTINCT pm.id)  AS total_profesionales,
                   ROUND(COUNT(s.id)::NUMERIC
                         / NULLIF(COUNT(DISTINCT pm.id), 0), 2) AS promedio_sesiones
            FROM profesionales_medicos pm
            LEFT JOIN sesiones s ON s.id_profesional_medico = pm.id
            GROUP BY pm.especialidad
            ORDER BY total_sesiones DESC
            """, nativeQuery = true)
    List<Object[]> obtenerEstadisticasPorEspecialidad();

    @Query(value = """
            SELECT DISTINCT pm.*
            FROM profesionales_medicos pm
            INNER JOIN sesiones s ON s.id_profesional_medico = pm.id
            WHERE s.id_paciente = :pacienteId
            ORDER BY pm.apellidos ASC
            """, nativeQuery = true)
    List<ProfesionalMedico> obtenerProfesionalesQueTrataron(@Param("pacienteId") Long pacienteId);
}
