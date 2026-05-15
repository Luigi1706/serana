package com.arquiweb.grupo3.serana.servicesimpl;

import com.arquiweb.grupo3.serana.dtos.SesionRecursoEducativoDTO;
import com.arquiweb.grupo3.serana.entities.RecursoEducativo;
import com.arquiweb.grupo3.serana.entities.Sesion;
import com.arquiweb.grupo3.serana.entities.SesionRecursoEducativo;
import com.arquiweb.grupo3.serana.exceptions.ResourceNotFoundException;
import com.arquiweb.grupo3.serana.repositories.SesionRecursoEducativoRepository;
import com.arquiweb.grupo3.serana.services.RecursoEducativoService;
import com.arquiweb.grupo3.serana.services.SesionRecursoEducativoService;
import com.arquiweb.grupo3.serana.services.SesionService;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SesionRecursoEducativoServiceImpl implements SesionRecursoEducativoService {
    @Autowired
    SesionRecursoEducativoRepository sesionRecursoRepository;

    @Autowired
    SesionService sesionService;

    @Autowired
    RecursoEducativoService recursoEducativoService;

    @Override
    public SesionRecursoEducativo findById(Long id) {
        return sesionRecursoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No se encontró el vínculo sesión-recurso con id: " + id));
    }

    @Override
    public List<SesionRecursoEducativoDTO> findBySesionId(Long sesionId) {
        sesionService.findById(sesionId); // valida que la sesión exista
        return sesionRecursoRepository.findBySesionId(sesionId)
                .stream().map(this::toDTO).toList();
    }

    @Override
    public List<SesionRecursoEducativoDTO> findByRecursoEducativoId(Long recursoId) {
        recursoEducativoService.findById(recursoId); // valida que el recurso exista
        return sesionRecursoRepository.findByRecursoEducativoId(recursoId)
                .stream().map(this::toDTO).toList();
    }

    @Override
    public Long contarRecursosPorSesion(Long sesionId) {
        sesionService.findById(sesionId); // valida que la sesión exista
        return sesionRecursoRepository.contarRecursosPorSesion(sesionId);
    }

    @Override
    public SesionRecursoEducativoDTO add(SesionRecursoEducativoDTO dto) {
        if (dto.getIdSesion() == null) {
            throw new ValidationException("Debe indicar el id de la sesión.");
        }
        if (dto.getIdRecursoEducativo() == null) {
            throw new ValidationException("Debe indicar el id del recurso educativo.");
        }

        // Validar que la sesión y el recurso existen
        Sesion sesion = sesionService.findById(dto.getIdSesion());
        RecursoEducativo recurso = recursoEducativoService.findById(dto.getIdRecursoEducativo());

        // Evitar duplicados: un recurso no puede vincularse dos veces a la misma sesión
        if (sesionRecursoRepository.existsBySesionIdAndRecursoEducativoId(
                dto.getIdSesion(), dto.getIdRecursoEducativo())) {
            throw new ValidationException(
                    "El recurso ya está vinculado a esta sesión.");
        }

        SesionRecursoEducativo nuevo = new SesionRecursoEducativo(null, sesion, recurso);
        nuevo = sesionRecursoRepository.save(nuevo);
        return toDTO(nuevo);
    }

    @Override
    public void delete(Long id) {
        findById(id); // lanza excepción si no existe
        sesionRecursoRepository.deleteById(id);
    }

    // ── Mapeo entidad → DTO
    private SesionRecursoEducativoDTO toDTO(SesionRecursoEducativo sre) {
        SesionRecursoEducativoDTO dto = new SesionRecursoEducativoDTO();
        dto.setId(sre.getId());
        dto.setIdSesion(sre.getSesion().getId());
        dto.setIdRecursoEducativo(sre.getRecursoEducativo().getId());
        dto.setTipoSesion(sre.getSesion().getTipoSesion());
        dto.setTituloRecurso(sre.getRecursoEducativo().getTitulo());
        dto.setTipoContenido(sre.getRecursoEducativo().getTipoContenido());
        dto.setLinkRecurso(sre.getRecursoEducativo().getLink());
        return dto;
    }
}
