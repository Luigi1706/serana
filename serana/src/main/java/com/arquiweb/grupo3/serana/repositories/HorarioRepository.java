package com.arquiweb.grupo3.serana.repositories;

import com.arquiweb.grupo3.serana.entities.Horario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;


public interface HorarioRepository extends JpaRepository<Horario, Long> {

    List<Horario> findByProfesionalMedicoId(Long profesionalMedicoId);
    List<Horario> findByProfesionalMedicoIdAndDisponibleTrue(Long profesionalMedicoId);
    List<Horario> findByProfesionalMedicoIdAndFechaGreaterThanEqualOrderByFechaAscHoraInicioAsc(
            Long profesionalMedicoId, LocalDate fecha);
    List<Horario> findByDisponibleTrueAndFechaGreaterThanEqualOrderByFechaAscHoraInicioAsc(
            LocalDate fechaDesde);
    @Query("""
            SELECT h FROM Horario h
            WHERE h.profesionalMedico.id = :profesionalId
              AND h.disponible = true
              AND h.fecha >= CURRENT_DATE
            ORDER BY h.fecha ASC, h.horaInicio ASC
            """)
    List<Horario> obtenerHorariosDisponiblesPorProfesional(@Param("profesionalId") Long profesionalId);
    @Query("""
            SELECT h FROM Horario h
            WHERE LOWER(h.profesionalMedico.especialidad) = LOWER(:especialidad)
              AND h.disponible = true
              AND h.fecha >= CURRENT_DATE
            ORDER BY h.fecha ASC, h.horaInicio ASC
            """)
    List<Horario> buscarDisponibilidadPorEspecialidad(@Param("especialidad") String especialidad);

    @Query("""
            SELECT h FROM Horario h
            WHERE h.profesionalMedico.id = :profesionalId
              AND h.fecha BETWEEN :fechaInicio AND :fechaFin
            ORDER BY h.fecha ASC, h.horaInicio ASC
            """)
    List<Horario> obtenerHorariosPorProfesionalEnRango(
            @Param("profesionalId") Long profesionalId,
            @Param("fechaInicio") LocalDate fechaInicio,
            @Param("fechaFin") LocalDate fechaFin);
    @Query("""
            SELECT COUNT(h) FROM Horario h
            WHERE h.profesionalMedico.id = :profesionalId
              AND h.disponible = true
              AND h.fecha >= CURRENT_DATE
            """)
    Long contarHorariosDisponiblesFuturos(@Param("profesionalId") Long profesionalId);
    @Query(value = """
            SELECT h.*
            FROM horarios h
            WHERE h.disponible = true
              AND h.fecha >= CURRENT_DATE
            ORDER BY h.fecha ASC, h.hora_inicio ASC
            LIMIT 20
            """, nativeQuery = true)
    List<Horario> obtenerProximosHorariosDisponibles();
    @Query(value = """
            SELECT h.*
            FROM horarios h
            WHERE h.id_profesional_medico = :profesionalId
              AND h.disponible = false
              AND h.fecha >= CURRENT_DATE
            ORDER BY h.fecha ASC, h.hora_inicio ASC
            """, nativeQuery = true)
    List<Horario> obtenerHorariosReservadosPorProfesional(@Param("profesionalId") Long profesionalId);
    @Query(value = """
            SELECT pm.especialidad,
                   COUNT(h.id) AS total_disponibles
            FROM horarios h
            INNER JOIN profesionales_medicos pm ON h.id_profesional_medico = pm.id
            WHERE h.disponible = true
              AND h.fecha >= CURRENT_DATE
            GROUP BY pm.especialidad
            ORDER BY total_disponibles DESC
            """, nativeQuery = true)
    List<Object[]> contarHorariosDisponiblesPorEspecialidad();
    @Query(value = """
            SELECT pm.*
            FROM profesionales_medicos pm
            WHERE pm.id NOT IN (
                SELECT DISTINCT h.id_profesional_medico
                FROM horarios h
                WHERE h.disponible = true
                  AND h.fecha BETWEEN CURRENT_DATE
                                  AND CURRENT_DATE + INTERVAL '7 days'
            )
            """, nativeQuery = true)
    List<Object[]> detectarProfesionalesSinDisponibilidadProxima();
}
