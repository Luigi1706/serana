package com.arquiweb.grupo3.serana.controllers;

import com.arquiweb.grupo3.serana.dtos.HorarioDTO;
import com.arquiweb.grupo3.serana.entities.Horario;
import com.arquiweb.grupo3.serana.services.HorarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/arqui_serana")
public class HorarioController {

    @Autowired
    HorarioService horarioService;

    @GetMapping("/horarios")
    public ResponseEntity<List<Horario>> findAll(){

        List<Horario> horarios = horarioService.findAll();

        return new ResponseEntity<>(horarios, HttpStatus.OK);
    }

    @GetMapping("/horarios/{id}")
    public ResponseEntity<Horario> findById(@PathVariable Long id){

        Horario found = horarioService.findById(id);

        if(found == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(found, HttpStatus.OK);
    }

    @GetMapping("/horarios/profesional/{id}")
    public ResponseEntity<List<Horario>> findByProfesional(
            @PathVariable Long id){

        List<Horario> horarios =
                horarioService.findByProfesionalMedicoId(id);

        if(horarios == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(horarios, HttpStatus.OK);
    }

    @PostMapping("/horarios")
    public ResponseEntity<HorarioDTO> add(
            @RequestBody HorarioDTO horarioDTO){

        HorarioDTO newHorario =
                horarioService.add(horarioDTO);

        if(newHorario == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(newHorario, HttpStatus.CREATED);
    }

    @PutMapping("/horarios")
    public ResponseEntity<HorarioDTO> update(
            @RequestBody HorarioDTO horarioDTO){

        HorarioDTO updated =
                horarioService.update(horarioDTO);

        if(updated == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/horarios/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){

        horarioService.delete(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}