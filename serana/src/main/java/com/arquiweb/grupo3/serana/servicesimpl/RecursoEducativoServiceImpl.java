package com.arquiweb.grupo3.serana.servicesimpl;

import com.arquiweb.grupo3.serana.entities.RecursoEducativo;
import com.arquiweb.grupo3.serana.exceptions.ResourceNotFoundException;
import com.arquiweb.grupo3.serana.repositories.RecursoEducativoRepository;
import com.arquiweb.grupo3.serana.services.RecursoEducativoService;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecursoEducativoServiceImpl implements RecursoEducativoService {

    @Autowired
    private RecursoEducativoRepository recursoEducativoRepository;

    @Override
    public RecursoEducativo findById(Long id) {
        return recursoEducativoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recurso educativo no encontrado con id: " + id));
    }

    @Override
    @Transactional
    public RecursoEducativo add(RecursoEducativo recursoEducativo) {
        if (recursoEducativo.getFechaPublicacion() == null)
            throw new ValidationException("La fecha de publicación es obligatoria.");
        if (recursoEducativo.getTitulo() == null || recursoEducativo.getTitulo().isBlank())
            throw new ValidationException("El título del recurso es obligatorio.");
        if (recursoEducativo.getTipoContenido() == null || recursoEducativo.getTipoContenido().isBlank())
            throw new ValidationException("El tipo de contenido es obligatorio.");
        if (recursoEducativo.getLink() == null || recursoEducativo.getLink().isBlank())
            throw new ValidationException("El link del recurso es obligatorio.");
        return recursoEducativoRepository.save(recursoEducativo);
    }

    @Override
    public List<RecursoEducativo> listAll() {
        return recursoEducativoRepository.findAll();
    }

    @Override
    @Transactional
    public RecursoEducativo update(RecursoEducativo recursoEducativo) {
        RecursoEducativo found = findById(recursoEducativo.getId()); // lanza 404 si no existe
        // Actualizar solo campos no nulos
        if (recursoEducativo.getTitulo() != null && !recursoEducativo.getTitulo().isBlank())
            found.setTitulo(recursoEducativo.getTitulo());
        if (recursoEducativo.getTipoContenido() != null && !recursoEducativo.getTipoContenido().isBlank())
            found.setTipoContenido(recursoEducativo.getTipoContenido());
        if (recursoEducativo.getLink() != null && !recursoEducativo.getLink().isBlank())
            found.setLink(recursoEducativo.getLink());
        if (recursoEducativo.getFechaPublicacion() != null)
            found.setFechaPublicacion(recursoEducativo.getFechaPublicacion());
        return recursoEducativoRepository.save(found);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        findById(id); // lanza excepción si no existe
        recursoEducativoRepository.deleteById(id);
    }
}
