package com.arquiweb.grupo3.serana.controllers;

import com.arquiweb.grupo3.serana.dtos.HorarioDTO;
import com.arquiweb.grupo3.serana.entities.Horario;
import com.arquiweb.grupo3.serana.services.HorarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/arqui_serana")
// Controller encargado de gestionar los horarios disponibles de los profesionales médicos:
// consulta, creación, actualización, reserva y eliminación de franjas de tiempo.
public class HorarioController {

    @Autowired
    private HorarioService horarioService;

    @GetMapping("/horarios")
    public ResponseEntity<List<Horario>> findAll() {
        return new ResponseEntity<>(horarioService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/horarios/{id}")
    public ResponseEntity<Horario> findById(@PathVariable Long id) {
        return new ResponseEntity<>(horarioService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/horarios/profesional/{id}")
    public ResponseEntity<List<Horario>> findByProfesional(@PathVariable Long id) {
        return new ResponseEntity<>(
                horarioService.findByProfesionalMedicoId(id), HttpStatus.OK);
    }

    @GetMapping("/horarios/profesional/{id}/disponibles")
    public ResponseEntity<List<Horario>> findDisponiblesByProfesional(@PathVariable Long id) {
        return new ResponseEntity<>(
                horarioService.findDisponiblesByProfesional(id), HttpStatus.OK);
    }

    @GetMapping("/horarios/disponibles")
    public ResponseEntity<List<Horario>> findDisponiblesByEspecialidad(
            @RequestParam String especialidad) {
        return new ResponseEntity<>(
                horarioService.findDisponiblesByEspecialidad(especialidad), HttpStatus.OK);
    }

    @GetMapping("/horarios/profesional/{id}/rango")
    public ResponseEntity<List<Horario>> findByProfesionalEnRango(
            @PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        return new ResponseEntity<>(
                horarioService.findByProfesionalEnRango(id, fechaInicio, fechaFin), HttpStatus.OK);
    }

    @PostMapping("/horarios")
    public ResponseEntity<HorarioDTO> add(@RequestBody HorarioDTO horarioDTO) {
        return new ResponseEntity<>(horarioService.add(horarioDTO), HttpStatus.CREATED);
    }

    @PutMapping("/horarios")
    public ResponseEntity<HorarioDTO> update(@RequestBody HorarioDTO horarioDTO) {
        return new ResponseEntity<>(horarioService.update(horarioDTO), HttpStatus.OK);
    }

    @PatchMapping("/horarios/{id}/reservar")
    public ResponseEntity<HorarioDTO> reservar(@PathVariable Long id) {
        return new ResponseEntity<>(horarioService.reservar(id), HttpStatus.OK);
    }

    @DeleteMapping("/horarios/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        horarioService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
