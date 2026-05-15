package com.arquiweb.grupo3.serana.services;

import com.arquiweb.grupo3.serana.dtos.SesionDTO;
import com.arquiweb.grupo3.serana.entities.Sesion;

import java.util.List;

public interface SesionService {
    public Sesion add(Sesion movie);
    public SesionDTO addDTO(SesionDTO sesionDTO);
    public List<Sesion> listAll();
    public Sesion findById(Long id);
    public Sesion update(Sesion sesion);
    public void delete(Long id);
}
