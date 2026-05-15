package com.arquiweb.grupo3.serana.services;

import com.arquiweb.grupo3.serana.dtos.SesionRecursoEducativoDTO;
import com.arquiweb.grupo3.serana.entities.SesionRecursoEducativo;

import java.util.List;

public interface SesionRecursoEducativoService {
    SesionRecursoEducativo findById(Long id);

    // Recursos vinculados a una sesión concreta
    List<SesionRecursoEducativoDTO> findBySesionId(Long sesionId);

    // Sesiones en que se usó un recurso concreto
    List<SesionRecursoEducativoDTO> findByRecursoEducativoId(Long recursoId);

    // Contar cuántos recursos tiene una sesión
    Long contarRecursosPorSesion(Long sesionId);

    // Vincular un recurso a una sesión
    SesionRecursoEducativoDTO add(SesionRecursoEducativoDTO dto);

    // Desvincular (eliminar la relación)
    void delete(Long id);
}
