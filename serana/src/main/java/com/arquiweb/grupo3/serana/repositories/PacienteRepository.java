package com.arquiweb.grupo3.serana.repositories;

import com.arquiweb.grupo3.serana.entities.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    // Obtener perfil del paciente
    Paciente findByUsuarioId(Long usuarioId); //Query Method

    // Buscar pacientes por estado anímico en concreto
    List<Paciente> findByEstadoAnimo(String estado); //Query Method

    // Detectar paciente críticos para priorizar atención
    @Query("""
        SELECT p FROM Paciente p
        WHERE p.nivelAnsiedad >= :nivel
    """)
    List<Paciente> obtenerPacientesCriticos(int nivel); //JPQL
}
