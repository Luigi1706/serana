package com.arquiweb.grupo3.serana.repositories;

import com.arquiweb.grupo3.serana.entities.ProfesionalMedico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProfesionalMedicoRepository extends JpaRepository<ProfesionalMedico, Long> {

    //Búsqueda de médico por especialidad que busca paciente
    List<ProfesionalMedico> findByEspecialidadIgnoreCase(String especialidad); //Query Method

    // Solo muestra profesionales disponibles
    @Query("""
        SELECT DISTINCT pm FROM ProfesionalMedico pm
        JOIN pm.horarios h
        WHERE h.disponible = true
        AND LOWER(pm.especialidad) = LOWER(:especialidad)
    """)
    List<ProfesionalMedico> buscarDisponiblesPorEspecialidad(String especialidad); //JPQL

    // Profesionales más activos (más sesiones)
    @Query(value = """
        SELECT pm.*
        FROM profesionales_medicos pm
        LEFT JOIN sesiones s ON s.id_profesional_medico = pm.id
        GROUP BY pm.id
        ORDER BY COUNT(s.id) DESC
        LIMIT 10
    """, nativeQuery = true)
    List<ProfesionalMedico> obtenerTopProfesionales(); //SQL
}
