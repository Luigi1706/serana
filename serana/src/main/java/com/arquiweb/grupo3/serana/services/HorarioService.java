package com.arquiweb.grupo3.serana.services;

import com.arquiweb.grupo3.serana.dtos.HorarioDTO;
import com.arquiweb.grupo3.serana.entities.Horario;

import java.util.List;

public interface HorarioService {

    public List<Horario> findAll();

    public Horario findById(Long id);

    public List<Horario> findByProfesionalMedicoId(Long idProfesional);

    public HorarioDTO add(HorarioDTO horarioDTO);

    public HorarioDTO update(HorarioDTO horarioDTO);

    public void delete(Long id);
}