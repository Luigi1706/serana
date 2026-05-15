package com.arquiweb.grupo3.serana.servicesimpl;

import com.arquiweb.grupo3.serana.dtos.ProfesionalMedicoDTO;
import com.arquiweb.grupo3.serana.entities.ProfesionalMedico;
import com.arquiweb.grupo3.serana.entities.Sesion;
import com.arquiweb.grupo3.serana.entities.Usuario;
import com.arquiweb.grupo3.serana.exceptions.ResourceNotFoundException;
import com.arquiweb.grupo3.serana.repositories.ProfesionalMedicoRepository;
import com.arquiweb.grupo3.serana.services.ProfesionalMedicoService;
import com.arquiweb.grupo3.serana.services.UsuarioService;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfesionalMedicoServiceImpl implements ProfesionalMedicoService {
    @Autowired
    private ProfesionalMedicoRepository profesionalMedicoRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Override
    public List<ProfesionalMedico> findAll() {
        return profesionalMedicoRepository.findAll();
    }

    @Override
    public ProfesionalMedico findById(Long id) {
        return profesionalMedicoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Profesional médico no encontrado con id: " + id));
    }

    @Override
    public ProfesionalMedico findByUsuarioId(Long idUsuario) {
        return profesionalMedicoRepository.findByUsuarioId(idUsuario)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe perfil profesional para el usuario con id: " + idUsuario));
    }

    @Override
    public List<ProfesionalMedico> findByEspecialidad(String especialidad) {
        if (especialidad == null || especialidad.isBlank()) {
            throw new ValidationException("La especialidad de búsqueda no puede estar vacía.");
        }
        return profesionalMedicoRepository.findByEspecialidadIgnoreCase(especialidad.trim());
    }

    @Override
    public List<ProfesionalMedico> buscarPorNombre(String termino) {
        if (termino == null || termino.isBlank()) {
            throw new ValidationException("El término de búsqueda no puede estar vacío.");
        }
        String t = termino.trim();
        return profesionalMedicoRepository
                .findByNombresContainingIgnoreCaseOrApellidosContainingIgnoreCase(t, t);
    }

    @Override
    public List<ProfesionalMedico> findDisponiblesPorEspecialidad(String especialidad) {
        if (especialidad == null || especialidad.isBlank()) {
            throw new ValidationException("La especialidad no puede estar vacía.");
        }
        return profesionalMedicoRepository.buscarDisponiblesPorEspecialidad(especialidad.trim());
    }

    @Override
    public List<String> listarEspecialidades() {
        return profesionalMedicoRepository.listarEspecialidadesDisponibles();
    }

    @Override
    public ProfesionalMedicoDTO add(ProfesionalMedicoDTO dto) {
        validarCamposObligatorios(dto);

        Usuario usuario = usuarioService.findById(dto.getIdUsuario());

        // Evitar duplicado: un usuario solo puede tener un perfil profesional
        profesionalMedicoRepository.findByUsuarioId(dto.getIdUsuario())
                .ifPresent(p -> {
                    throw new ValidationException(
                            "El usuario con id " + dto.getIdUsuario()
                                    + " ya tiene un perfil profesional registrado (id: "
                                    + p.getId() + ").");
                });

        ProfesionalMedico profesional = new ProfesionalMedico();
        profesional.setNombres(dto.getNombres().trim());
        profesional.setApellidos(dto.getApellidos().trim());
        profesional.setEspecialidad(dto.getEspecialidad().trim());
        profesional.setLugarTrabajo(
                dto.getLugarTrabajo() != null ? dto.getLugarTrabajo().trim() : null);
        profesional.setUsuario(usuario);

        profesional = profesionalMedicoRepository.save(profesional);
        dto.setId(profesional.getId());
        return dto;
    }

    @Override
    public ProfesionalMedicoDTO update(ProfesionalMedicoDTO dto) {
        if (dto.getId() == null) {
            throw new ValidationException(
                    "El id del profesional es requerido para actualizar.");
        }

        ProfesionalMedico found = findById(dto.getId());

        if (dto.getNombres() != null && !dto.getNombres().isBlank()) {
            found.setNombres(dto.getNombres().trim());
        }
        if (dto.getApellidos() != null && !dto.getApellidos().isBlank()) {
            found.setApellidos(dto.getApellidos().trim());
        }
        if (dto.getEspecialidad() != null && !dto.getEspecialidad().isBlank()) {
            found.setEspecialidad(dto.getEspecialidad().trim());
        }
        if (dto.getLugarTrabajo() != null && !dto.getLugarTrabajo().isBlank()) {
            found.setLugarTrabajo(dto.getLugarTrabajo().trim());
        }

        profesionalMedicoRepository.save(found);
        return dto;
    }

    @Override
    public void delete(Long id) {
        ProfesionalMedico found = findById(id);

        // No eliminar si tiene sesiones en estado "Programada"
        boolean tieneSesionesPendientes = found.getSesiones() != null
                && found.getSesiones().stream()
                .map(Sesion::getEstadoSesion)
                .anyMatch("Programada"::equalsIgnoreCase);

        if (tieneSesionesPendientes) {
            throw new ValidationException(
                    "No se puede eliminar el profesional con id " + id
                            + " porque tiene sesiones programadas pendientes.");
        }

        profesionalMedicoRepository.delete(found);
    }

    private void validarCamposObligatorios(ProfesionalMedicoDTO dto) {
        if (dto == null) {
            throw new ValidationException("El cuerpo del profesional no puede ser nulo.");
        }
        if (dto.getNombres() == null || dto.getNombres().isBlank()) {
            throw new ValidationException("El nombre del profesional es obligatorio.");
        }
        if (dto.getApellidos() == null || dto.getApellidos().isBlank()) {
            throw new ValidationException("Los apellidos del profesional son obligatorios.");
        }
        if (dto.getEspecialidad() == null || dto.getEspecialidad().isBlank()) {
            throw new ValidationException("La especialidad del profesional es obligatoria.");
        }
        if (dto.getIdUsuario() == null) {
            throw new ValidationException("El id del usuario vinculado es obligatorio.");
        }
    }
}
