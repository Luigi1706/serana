package com.arquiweb.grupo3.serana.repositories;

import com.arquiweb.grupo3.serana.entities.Horario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface HorarioRepository extends JpaRepository<Horario, Long> {

    //Obtener todos los horarios aún no reservados por profesional médico
    @Query("""
        SELECT h FROM Horario h 
        WHERE h.profesionalMedico.id = :profesionalId 
        AND h.disponible = true 
        AND h.fecha >= CURRENT_DATE 
        ORDER BY h.fecha ASC, h.horaInicio ASC
    """)
    List<Horario> obtenerHorariosDisponiblesPorProfesional(Long profesionalId);

    //Obtener todos los horarios disponibles por especialidad
    @Query("""
        SELECT h FROM Horario h 
        WHERE h.profesionalMedico.especialidad = :especialidad 
        AND h.disponible = true 
        AND h.fecha >= CURRENT_DATE 
        ORDER BY h.fecha ASC, h.horaInicio ASC
    """)
    List<Horario> buscarDisponibilidadPorEspecialidad(String especialidad);
}
