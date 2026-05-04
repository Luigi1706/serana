package com.arquiweb.grupo3.serana.repositories;

import com.arquiweb.grupo3.serana.entities.Sesion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SesionRepository extends JpaRepository<Sesion, Long> {

    //Lista de atenciones programadas (más reciente a más antigua) por paciente
    @Query("""
        SELECT s FROM Sesion s
        WHERE s.paciente.id = :pacienteId
        ORDER BY s.fecha DESC, s.hora DESC
    """)
    List<Sesion> obtenerHistorialPaciente(Long pacienteId);

    // Obtención de lista de atenciones programadas por profesiona
    @Query("""
        SELECT s FROM Sesion s 
        WHERE s.profesionalMedico.id = :profesionalId 
        AND s.estadoSesion = 'PROGRAMADA'
    """)
    List<Sesion> obtenerSesionesProgramadasProfesional(Long profesionalId);

    // Obtener lista de profesionales con más de 10 atenciones programadas
    @Query(value = """
        SELECT id_profesional_medico, COUNT(*) as total 
        FROM sesiones 
        WHERE fecha >= CURRENT_DATE 
        GROUP BY id_profesional_medico 
        HAVING COUNT(*) > 10
    """, nativeQuery = true)
    List<Sesion> detectarProfesionalesSobrecargados();
}
