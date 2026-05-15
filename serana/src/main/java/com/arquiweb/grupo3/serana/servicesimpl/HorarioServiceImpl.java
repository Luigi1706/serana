package com.arquiweb.grupo3.serana.servicesimpl;

import com.arquiweb.grupo3.serana.dtos.HorarioDTO;
import com.arquiweb.grupo3.serana.entities.Horario;
import com.arquiweb.grupo3.serana.entities.ProfesionalMedico;
import com.arquiweb.grupo3.serana.exceptions.ResourceNotFoundException;
import com.arquiweb.grupo3.serana.repositories.HorarioRepository;
import com.arquiweb.grupo3.serana.services.HorarioService;
import com.arquiweb.grupo3.serana.services.ProfesionalMedicoService;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class HorarioServiceImpl implements HorarioService {
    @Autowired
    private HorarioRepository horarioRepository;

    @Autowired
    private ProfesionalMedicoService profesionalMedicoService;

    @Override
    public List<Horario> findAll() {
        return horarioRepository.findAll();
    }

    @Override
    public Horario findById(Long id) {
        return horarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Horario no encontrado con id: " + id));
    }

    @Override
    public List<Horario> findByProfesionalMedicoId(Long idProfesional) {
        // Verificar existencia del profesional antes de consultar sus horarios
        profesionalMedicoService.findById(idProfesional);
        return horarioRepository.findByProfesionalMedicoId(idProfesional);
    }

    @Override
    public List<Horario> findDisponiblesByProfesional(Long idProfesional) {
        profesionalMedicoService.findById(idProfesional);
        return horarioRepository.obtenerHorariosDisponiblesPorProfesional(idProfesional);
    }

    @Override
    public List<Horario> findDisponiblesByEspecialidad(String especialidad) {
        if (especialidad == null || especialidad.isBlank()) {
            throw new ValidationException("La especialidad no puede estar vacía.");
        }
        return horarioRepository.buscarDisponibilidadPorEspecialidad(especialidad.trim());
    }

    @Override
    public List<Horario> findByProfesionalEnRango(Long idProfesional,
                                                  LocalDate fechaInicio,
                                                  LocalDate fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            throw new ValidationException("Las fechas de inicio y fin son obligatorias.");
        }
        if (fechaFin.isBefore(fechaInicio)) {
            throw new ValidationException(
                    "La fecha de fin no puede ser anterior a la fecha de inicio.");
        }
        profesionalMedicoService.findById(idProfesional);
        return horarioRepository.obtenerHorariosPorProfesionalEnRango(
                idProfesional, fechaInicio, fechaFin);
    }

    @Override
    @Transactional
    public HorarioDTO add(HorarioDTO horarioDTO) {
        validarCamposObligatorios(horarioDTO);

        if (horarioDTO.getFecha().isBefore(LocalDate.now())) {
            throw new ValidationException(
                    "No se puede crear un horario en una fecha pasada.");
        }
        if (!horarioDTO.getHoraFin().isAfter(horarioDTO.getHoraInicio())) {
            throw new ValidationException(
                    "La hora de fin debe ser posterior a la hora de inicio.");
        }

        ProfesionalMedico profesional =
                profesionalMedicoService.findById(horarioDTO.getIdProfesionalMedico());

        Horario horario = new Horario();
        horario.setFecha(horarioDTO.getFecha());
        horario.setHoraInicio(horarioDTO.getHoraInicio());
        horario.setHoraFin(horarioDTO.getHoraFin());
        // Si no se envía, el horario se crea como disponible por defecto
        horario.setDisponible(
                horarioDTO.getDisponible() != null ? horarioDTO.getDisponible() : Boolean.TRUE);
        horario.setLink(horarioDTO.getLink());
        horario.setProfesionalMedico(profesional);

        horario = horarioRepository.save(horario);
        horarioDTO.setId(horario.getId());
        return horarioDTO;
    }

    @Override
    @Transactional
    public HorarioDTO update(HorarioDTO horarioDTO) {
        if (horarioDTO.getId() == null) {
            throw new ValidationException(
                    "El id del horario es requerido para actualizar.");
        }

        Horario found = findById(horarioDTO.getId());

        if (horarioDTO.getFecha() != null) {
            if (horarioDTO.getFecha().isBefore(LocalDate.now())) {
                throw new ValidationException(
                        "No se puede modificar un horario a una fecha pasada.");
            }
            found.setFecha(horarioDTO.getFecha());
        }
        if (horarioDTO.getHoraInicio() != null) {
            found.setHoraInicio(horarioDTO.getHoraInicio());
        }
        if (horarioDTO.getHoraFin() != null) {
            found.setHoraFin(horarioDTO.getHoraFin());
        }
        // Revalidar coherencia de horas tras la actualización parcial
        if (!found.getHoraFin().isAfter(found.getHoraInicio())) {
            throw new ValidationException(
                    "La hora de fin debe ser posterior a la hora de inicio.");
        }
        if (horarioDTO.getDisponible() != null) {
            found.setDisponible(horarioDTO.getDisponible());
        }
        if (horarioDTO.getLink() != null) {
            found.setLink(horarioDTO.getLink());
        }

        horarioRepository.save(found);
        return horarioDTO;
    }

    @Override
    @Transactional
    public HorarioDTO reservar(Long id) {
        Horario found = findById(id);

        if (Boolean.FALSE.equals(found.getDisponible())) {
            throw new ValidationException(
                    "El horario con id " + id + " ya se encuentra reservado.");
        }

        found.setDisponible(false);
        horarioRepository.save(found);

        return entityToDTO(found);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Horario found = findById(id);

        if (Boolean.FALSE.equals(found.getDisponible())) {
            throw new ValidationException(
                    "No se puede eliminar el horario con id " + id
                            + " porque ya fue reservado por un paciente.");
        }

        horarioRepository.delete(found);
    }

    private void validarCamposObligatorios(HorarioDTO dto) {
        if (dto == null) {
            throw new ValidationException("El cuerpo del horario no puede ser nulo.");
        }
        if (dto.getFecha() == null) {
            throw new ValidationException("La fecha del horario es obligatoria.");
        }
        if (dto.getHoraInicio() == null) {
            throw new ValidationException("La hora de inicio del horario es obligatoria.");
        }
        if (dto.getHoraFin() == null) {
            throw new ValidationException("La hora de fin del horario es obligatoria.");
        }
        if (dto.getIdProfesionalMedico() == null) {
            throw new ValidationException("El id del profesional médico es obligatorio.");
        }
    }

    /** Convierte entidad Horario al DTO correspondiente. */
    private HorarioDTO entityToDTO(Horario horario) {
        HorarioDTO dto = new HorarioDTO();
        dto.setId(horario.getId());
        dto.setFecha(horario.getFecha());
        dto.setHoraInicio(horario.getHoraInicio());
        dto.setHoraFin(horario.getHoraFin());
        dto.setDisponible(horario.getDisponible());
        dto.setLink(horario.getLink());
        if (horario.getProfesionalMedico() != null) {
            dto.setIdProfesionalMedico(horario.getProfesionalMedico().getId());
        }
        return dto;
    }
}
