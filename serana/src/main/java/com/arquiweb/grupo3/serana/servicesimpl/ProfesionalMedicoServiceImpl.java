package com.arquiweb.grupo3.serana.servicesimpl;

import com.arquiweb.grupo3.serana.dtos.ProfesionalMedicoDTO;
import com.arquiweb.grupo3.serana.entities.ProfesionalMedico;
import com.arquiweb.grupo3.serana.entities.Usuario;
import com.arquiweb.grupo3.serana.repositories.ProfesionalMedicoRepository;
import com.arquiweb.grupo3.serana.services.ProfesionalMedicoService;
import com.arquiweb.grupo3.serana.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfesionalMedicoServiceImpl
        implements ProfesionalMedicoService {

    @Autowired
    ProfesionalMedicoRepository profesionalMedicoRepository;

    @Autowired
    UsuarioService usuarioService;

    @Override
    public List<ProfesionalMedico> findAll() {

        return profesionalMedicoRepository.findAll();
    }

    @Override
    public ProfesionalMedico findById(Long id) {

        return profesionalMedicoRepository
                .findById(id)
                .orElse(null);
    }

    @Override
    public ProfesionalMedico findByUsuarioId(Long idUsuario) {

        return profesionalMedicoRepository
                .findByUsuarioId(idUsuario);
    }

    @Override
    public ProfesionalMedicoDTO add(
            ProfesionalMedicoDTO dto) {

        Usuario usuario =
                usuarioService.findById(dto.getIdUsuario());

        if(usuario == null){
            return null;
        }

        ProfesionalMedico profesional =
                new ProfesionalMedico();

        profesional.setNombres(dto.getNombres());
        profesional.setApellidos(dto.getApellidos());
        profesional.setEspecialidad(dto.getEspecialidad());
        profesional.setLugarTrabajo(dto.getLugarTrabajo());
        profesional.setUsuario(usuario);

        profesional = profesionalMedicoRepository
                .save(profesional);

        dto.setId(profesional.getId());

        return dto;
    }

    @Override
    public ProfesionalMedicoDTO update(
            ProfesionalMedicoDTO dto) {

        ProfesionalMedico found =
                findById(dto.getId());

        if(found == null){
            return null;
        }

        if(dto.getNombres() != null){
            found.setNombres(dto.getNombres());
        }

        if(dto.getApellidos() != null){
            found.setApellidos(dto.getApellidos());
        }

        if(dto.getEspecialidad() != null){
            found.setEspecialidad(dto.getEspecialidad());
        }

        if(dto.getLugarTrabajo() != null){
            found.setLugarTrabajo(dto.getLugarTrabajo());
        }

        profesionalMedicoRepository.save(found);

        return dto;
    }

    @Override
    public void delete(Long id) {

        ProfesionalMedico found =
                findById(id);

        if(found != null){
            profesionalMedicoRepository.delete(found);
        }
    }
}