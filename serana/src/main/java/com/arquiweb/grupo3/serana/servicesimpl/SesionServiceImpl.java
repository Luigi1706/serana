package com.arquiweb.grupo3.serana.servicesimpl;

import com.arquiweb.grupo3.serana.dtos.SesionDTO;
import com.arquiweb.grupo3.serana.entities.Paciente;
import com.arquiweb.grupo3.serana.entities.ProfesionalMedico;
import com.arquiweb.grupo3.serana.entities.Sesion;
import com.arquiweb.grupo3.serana.exceptions.ResourceNotFoundException;
import com.arquiweb.grupo3.serana.repositories.SesionRepository;
import com.arquiweb.grupo3.serana.services.PacienteService;
import com.arquiweb.grupo3.serana.services.ProfesionalMedicoService;
import com.arquiweb.grupo3.serana.services.SesionService;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SesionServiceImpl implements SesionService {

    @Autowired
    private SesionRepository sesionRepository;

    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private ProfesionalMedicoService profesionalMedicoService;

    @Override
    @Transactional
    public Sesion add(Sesion sesion) {
        if (sesion.getFecha() == null) throw new ValidationException("La fecha de la sesión es obligatoria.");
        if (sesion.getHora() == null) throw new ValidationException("La hora de la sesión es obligatoria.");
        if (sesion.getTipoSesion() == null || sesion.getTipoSesion().isBlank())
            throw new ValidationException("El tipo de sesión es obligatorio.");
        if (sesion.getEstadoSesion() == null || sesion.getEstadoSesion().isBlank())
            throw new ValidationException("El estado de la sesión es obligatorio.");
        if (sesion.getPaciente() == null) throw new ValidationException("El paciente es obligatorio.");
        if (sesion.getProfesionalMedico() == null) throw new ValidationException("El profesional médico es obligatorio.");
        return sesionRepository.save(sesion);
    }

    @Override
    @Transactional
    public SesionDTO addDTO(SesionDTO sesionDTO) {
        if (sesionDTO.getPacienteId() == null) throw new ValidationException("El id del paciente es obligatorio.");
        if (sesionDTO.getProfesionalMedicoId() == null) throw new ValidationException("El id del profesional es obligatorio.");

        Paciente paciente = pacienteService.findById(sesionDTO.getPacienteId());
        ProfesionalMedico profesionalMedico = profesionalMedicoService.findById(sesionDTO.getProfesionalMedicoId());

        Sesion newSesion = new Sesion(
                null,
                sesionDTO.getFecha(),
                sesionDTO.getHora(),
                sesionDTO.getTipoSesion(),
                sesionDTO.getEstadoSesion(),
                null,
                paciente,
                profesionalMedico,
                null
        );

        newSesion = add(newSesion);
        sesionDTO.setId(newSesion.getId());
        sesionDTO.setPacienteNombres(paciente.getNombres());
        sesionDTO.setPacienteApellidos(paciente.getApellidos());
        sesionDTO.setProfesionalMedicoNombres(profesionalMedico.getNombres());
        sesionDTO.setProfesionalMedicoApellidos(profesionalMedico.getApellidos());
        return sesionDTO;
    }

    @Override
    public List<Sesion> listAll() {
        return sesionRepository.findAll();
    }

    @Override
    public Sesion findById(Long id) {
        return sesionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sesión no encontrada con id: " + id));
    }

    @Override
    @Transactional
    public Sesion update(Sesion sesion) {
        Sesion found = findById(sesion.getId());
        // Solo actualizar campos no nulos
        if (sesion.getFecha() != null) found.setFecha(sesion.getFecha());
        if (sesion.getHora() != null) found.setHora(sesion.getHora());
        if (sesion.getTipoSesion() != null && !sesion.getTipoSesion().isBlank()) found.setTipoSesion(sesion.getTipoSesion());
        if (sesion.getEstadoSesion() != null && !sesion.getEstadoSesion().isBlank()) found.setEstadoSesion(sesion.getEstadoSesion());
        if (sesion.getComentario() != null) found.setComentario(sesion.getComentario());
        return sesionRepository.save(found);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        findById(id); // lanza excepción si no existe
        sesionRepository.deleteById(id);
    }
}
