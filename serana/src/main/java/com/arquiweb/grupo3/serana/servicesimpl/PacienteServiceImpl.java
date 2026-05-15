package com.arquiweb.grupo3.serana.servicesimpl;

import com.arquiweb.grupo3.serana.dtos.PacienteDTO;
import com.arquiweb.grupo3.serana.entities.Paciente;
import com.arquiweb.grupo3.serana.entities.Sesion;
import com.arquiweb.grupo3.serana.entities.Usuario;
import com.arquiweb.grupo3.serana.exceptions.ResourceNotFoundException;
import com.arquiweb.grupo3.serana.repositories.PacienteRepository;
import com.arquiweb.grupo3.serana.services.PacienteService;
import com.arquiweb.grupo3.serana.services.UsuarioService;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PacienteServiceImpl implements PacienteService {

    @Autowired
    private PacienteRepository pacienteRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Override
    public List<Paciente> findAll() {
        return pacienteRepository.findAll();
    }

    @Override
    public Paciente findById(Long id) {
        return pacienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Paciente no encontrado con id: " + id));
    }

    @Override
    public Paciente findByUsuarioId(Long usuarioId) {
        return pacienteRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No existe perfil de paciente para el usuario con id: " + usuarioId));
    }

    @Override
    public List<Paciente> findByEstadoAnimo(String estadoAnimo) {
        if (estadoAnimo == null || estadoAnimo.isBlank())
            throw new ValidationException("El estado de ánimo no puede estar vacío.");
        return pacienteRepository.findByEstadoAnimoIgnoreCase(estadoAnimo.trim());
    }

    @Override
    public List<Paciente> buscarPorNombre(String termino) {
        if (termino == null || termino.isBlank())
            throw new ValidationException("El término de búsqueda no puede estar vacío.");
        String t = termino.trim();
        return pacienteRepository.findByNombresContainingIgnoreCaseOrApellidosContainingIgnoreCase(t, t);
    }

    @Override
    public List<Paciente> obtenerPacientesCriticos(int nivelMinimo) {
        if (nivelMinimo < 0 || nivelMinimo > 10)
            throw new ValidationException("El nivel de ansiedad debe estar entre 0 y 10.");
        return pacienteRepository.obtenerPacientesCriticos(nivelMinimo);
    }

    @Override
    @Transactional
    public PacienteDTO add(PacienteDTO dto) {
        validarCamposObligatorios(dto);

        Usuario usuario = usuarioService.findById(dto.getIdUsuario());

        // Evitar duplicado: un usuario solo puede tener un perfil de paciente
        pacienteRepository.findByUsuarioId(dto.getIdUsuario()).ifPresent(p -> {
            throw new ValidationException(
                    "El usuario con id " + dto.getIdUsuario() +
                            " ya tiene un perfil de paciente registrado (id: " + p.getId() + ").");
        });

        Paciente paciente = new Paciente();
        paciente.setNombres(dto.getNombres().trim());
        paciente.setApellidos(dto.getApellidos().trim());
        paciente.setGenero(dto.getGenero());
        paciente.setAnonimato(dto.getAnonimato() != null ? dto.getAnonimato() : false);
        paciente.setEstadoAnimo(dto.getEstadoAnimo());
        paciente.setNivelAnsiedad(dto.getNivelAnsiedad());
        paciente.setUsuario(usuario);

        paciente = pacienteRepository.save(paciente);
        dto.setId(paciente.getId());
        return dto;
    }

    @Override
    @Transactional
    public PacienteDTO update(PacienteDTO dto) {
        if (dto.getId() == null)
            throw new ValidationException("El id del paciente es requerido para actualizar.");

        Paciente found = findById(dto.getId());

        if (dto.getNombres() != null && !dto.getNombres().isBlank())
            found.setNombres(dto.getNombres().trim());
        if (dto.getApellidos() != null && !dto.getApellidos().isBlank())
            found.setApellidos(dto.getApellidos().trim());
        if (dto.getEstadoAnimo() != null && !dto.getEstadoAnimo().isBlank())
            found.setEstadoAnimo(dto.getEstadoAnimo().trim());
        if (dto.getAnonimato() != null)
            found.setAnonimato(dto.getAnonimato());
        if (dto.getNivelAnsiedad() >= 0)
            found.setNivelAnsiedad(dto.getNivelAnsiedad());

        pacienteRepository.save(found);
        return dto;
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Paciente found = findById(id);

        boolean tieneSesionesPendientes = found.getSesiones() != null &&
                found.getSesiones().stream()
                        .map(Sesion::getEstadoSesion)
                        .anyMatch("Programada"::equalsIgnoreCase);

        if (tieneSesionesPendientes)
            throw new ValidationException(
                    "No se puede eliminar el paciente con id " + id +
                            " porque tiene sesiones programadas pendientes.");

        pacienteRepository.delete(found);
    }

    private void validarCamposObligatorios(PacienteDTO dto) {
        if (dto == null) throw new ValidationException("El cuerpo del paciente no puede ser nulo.");
        if (dto.getNombres() == null || dto.getNombres().isBlank())
            throw new ValidationException("El nombre del paciente es obligatorio.");
        if (dto.getApellidos() == null || dto.getApellidos().isBlank())
            throw new ValidationException("Los apellidos del paciente son obligatorios.");
        if (dto.getIdUsuario() == null)
            throw new ValidationException("El id del usuario vinculado es obligatorio.");
    }
}
