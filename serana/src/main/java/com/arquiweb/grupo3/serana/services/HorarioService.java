package com.arquiweb.grupo3.serana.services;

import com.arquiweb.grupo3.serana.dtos.HorarioDTO;
import com.arquiweb.grupo3.serana.entities.Horario;

import java.time.LocalDate;
import java.util.List;

public interface HorarioService {

    List<Horario> findAll();
    Horario findById(Long id);
    List<Horario> findByProfesionalMedicoId(Long idProfesional);
    List<Horario> findDisponiblesByProfesional(Long idProfesional);
    List<Horario> findDisponiblesByEspecialidad(String especialidad);
    List<Horario> findByProfesionalEnRango(Long idProfesional, LocalDate fechaInicio, LocalDate fechaFin);
    HorarioDTO add(HorarioDTO horarioDTO);
    HorarioDTO update(HorarioDTO horarioDTO);
    HorarioDTO reservar(Long id);
    void delete(Long id);
}
