package com.arquiweb.grupo3.serana.services;

import com.arquiweb.grupo3.serana.dtos.ProfesionalMedicoDTO;
import com.arquiweb.grupo3.serana.entities.ProfesionalMedico;

import java.util.List;

public interface ProfesionalMedicoService {

    public List<ProfesionalMedico> findAll();

    public ProfesionalMedico findById(Long id);

    public ProfesionalMedico findByUsuarioId(Long idUsuario);

    public ProfesionalMedicoDTO add(ProfesionalMedicoDTO dto);

    public ProfesionalMedicoDTO update(ProfesionalMedicoDTO dto);

    public void delete(Long id);
}