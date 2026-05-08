package com.arquiweb.grupo3.serana.servicesimpl;

import com.arquiweb.grupo3.serana.dtos.HorarioDTO;
import com.arquiweb.grupo3.serana.entities.Horario;
import com.arquiweb.grupo3.serana.entities.ProfesionalMedico;
import com.arquiweb.grupo3.serana.repositories.HorarioRepository;
import com.arquiweb.grupo3.serana.services.HorarioService;
import com.arquiweb.grupo3.serana.services.ProfesionalMedicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HorarioServiceImpl implements HorarioService {

    @Autowired
    HorarioRepository horarioRepository;

    @Autowired
    ProfesionalMedicoService profesionalMedicoService;

    @Override
    public List<Horario> findAll() {
        return horarioRepository.findAll();
    }

    @Override
    public Horario findById(Long id) {
        return horarioRepository.findById(id).orElse(null);
    }

    @Override
    public List<Horario> findByProfesionalMedicoId(Long idProfesional) {

        ProfesionalMedico profesional =
                profesionalMedicoService.findById(idProfesional);

        if(profesional == null){
            return null;
        }

        return profesional.getHorarios();
    }

    @Override
    public HorarioDTO add(HorarioDTO horarioDTO) {

        ProfesionalMedico profesional =
                profesionalMedicoService.findById(
                        horarioDTO.getIdProfesionalMedico());

        if(profesional == null){
            return null;
        }

        Horario horario = new Horario();

        horario.setFecha(horarioDTO.getFecha());
        horario.setHoraInicio(horarioDTO.getHoraInicio());
        horario.setHoraFin(horarioDTO.getHoraFin());
        horario.setDisponible(horarioDTO.getDisponible());
        horario.setLink(horarioDTO.getLink());
        horario.setProfesionalMedico(profesional);

        horario = horarioRepository.save(horario);

        horarioDTO.setId(horario.getId());

        return horarioDTO;
    }

    @Override
    public HorarioDTO update(HorarioDTO horarioDTO) {

        Horario found = findById(horarioDTO.getId());

        if(found == null){
            return null;
        }

        if(horarioDTO.getFecha() != null){
            found.setFecha(horarioDTO.getFecha());
        }

        if(horarioDTO.getHoraInicio() != null){
            found.setHoraInicio(horarioDTO.getHoraInicio());
        }

        if(horarioDTO.getHoraFin() != null){
            found.setHoraFin(horarioDTO.getHoraFin());
        }

        if(horarioDTO.getDisponible() != null){
            found.setDisponible(horarioDTO.getDisponible());
        }

        if(horarioDTO.getLink() != null){
            found.setLink(horarioDTO.getLink());
        }

        horarioRepository.save(found);

        return horarioDTO;
    }

    @Override
    public void delete(Long id) {

        Horario found = findById(id);

        if(found != null){
            horarioRepository.delete(found);
        }
    }
}