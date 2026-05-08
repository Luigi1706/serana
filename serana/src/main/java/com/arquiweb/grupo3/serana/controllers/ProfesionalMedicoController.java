package com.arquiweb.grupo3.serana.controllers;

import com.arquiweb.grupo3.serana.dtos.ProfesionalMedicoDTO;
import com.arquiweb.grupo3.serana.entities.ProfesionalMedico;
import com.arquiweb.grupo3.serana.services.ProfesionalMedicoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/arqui_serana")
public class ProfesionalMedicoController {

    @Autowired
    ProfesionalMedicoService profesionalMedicoService;

    @GetMapping("/profesionales")
    public ResponseEntity<List<ProfesionalMedico>> findAll(){

        List<ProfesionalMedico> profesionales =
                profesionalMedicoService.findAll();

        return new ResponseEntity<>(
                profesionales,
                HttpStatus.OK);
    }

    @GetMapping("/profesionales/{id}")
    public ResponseEntity<ProfesionalMedico> findById(
            @PathVariable Long id){

        ProfesionalMedico found =
                profesionalMedicoService.findById(id);

        if(found == null){
            return new ResponseEntity<>(
                    HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(
                found,
                HttpStatus.OK);
    }

    @PostMapping("/profesionales")
    public ResponseEntity<ProfesionalMedicoDTO> add(
            @RequestBody ProfesionalMedicoDTO dto){

        ProfesionalMedicoDTO created =
                profesionalMedicoService.add(dto);

        if(created == null){
            return new ResponseEntity<>(
                    HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(
                created,
                HttpStatus.CREATED);
    }

    @PutMapping("/profesionales")
    public ResponseEntity<ProfesionalMedicoDTO> update(
            @RequestBody ProfesionalMedicoDTO dto){

        ProfesionalMedicoDTO updated =
                profesionalMedicoService.update(dto);

        if(updated == null){
            return new ResponseEntity<>(
                    HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(
                updated,
                HttpStatus.OK);
    }

    @DeleteMapping("/profesionales/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id){

        profesionalMedicoService.delete(id);

        return new ResponseEntity<>(
                HttpStatus.NO_CONTENT);
    }
}