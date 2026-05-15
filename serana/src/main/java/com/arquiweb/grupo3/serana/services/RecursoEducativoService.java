package com.arquiweb.grupo3.serana.services;

import com.arquiweb.grupo3.serana.entities.RecursoEducativo;

import java.util.List;

public interface RecursoEducativoService {
    RecursoEducativo findById(Long id);
    RecursoEducativo add(RecursoEducativo recursoEducativo);
    List<RecursoEducativo> listAll();
    RecursoEducativo update(RecursoEducativo recursoEducativo);
    void delete(Long id);
}
