package com.arquiweb.grupo3.serana.services;

import com.arquiweb.grupo3.serana.dtos.ProfesionalMedicoDTO;
import com.arquiweb.grupo3.serana.entities.ProfesionalMedico;

import java.util.List;

public interface ProfesionalMedicoService {
    List<ProfesionalMedico> findAll();
    ProfesionalMedico findById(Long id);
    ProfesionalMedico findByUsuarioId(Long idUsuario);
    List<ProfesionalMedico> findByEspecialidad(String especialidad);
    List<ProfesionalMedico> buscarPorNombre(String termino);
    List<ProfesionalMedico> findDisponiblesPorEspecialidad(String especialidad);
    List<String> listarEspecialidades();
    ProfesionalMedicoDTO add(ProfesionalMedicoDTO dto);
    ProfesionalMedicoDTO update(ProfesionalMedicoDTO dto);
    void delete(Long id);
}
