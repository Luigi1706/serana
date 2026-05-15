package com.arquiweb.grupo3.serana.services;

import com.arquiweb.grupo3.serana.dtos.PacienteDTO;
import com.arquiweb.grupo3.serana.entities.Paciente;

import java.util.List;

public interface PacienteService {
    List<Paciente> findAll();
    Paciente findById(Long id);
    Paciente findByUsuarioId(Long usuarioId);
    List<Paciente> findByEstadoAnimo(String estadoAnimo);
    List<Paciente> buscarPorNombre(String termino);
    List<Paciente> obtenerPacientesCriticos(int nivelMinimo);
    PacienteDTO add(PacienteDTO dto);
    PacienteDTO update(PacienteDTO dto);
    void delete(Long id);
}
