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
// Controller encargado de gestionar los perfiles de profesionales médicos de Serana:
// consulta por especialidad, nombre, disponibilidad y CRUD completo.
public class ProfesionalMedicoController {
    @Autowired
    private ProfesionalMedicoService profesionalMedicoService;

    @GetMapping("/profesionales")
    public ResponseEntity<List<ProfesionalMedico>> findAll() {
        return new ResponseEntity<>(profesionalMedicoService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/profesionales/{id}")
    public ResponseEntity<ProfesionalMedico> findById(@PathVariable Long id) {
        return new ResponseEntity<>(profesionalMedicoService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/profesionales/usuario/{idUsuario}")
    public ResponseEntity<ProfesionalMedico> findByUsuarioId(@PathVariable Long idUsuario) {
        return new ResponseEntity<>(
                profesionalMedicoService.findByUsuarioId(idUsuario), HttpStatus.OK);
    }

    @GetMapping("/profesionales/especialidad")
    public ResponseEntity<List<ProfesionalMedico>> findByEspecialidad(
            @RequestParam String nombre) {
        return new ResponseEntity<>(
                profesionalMedicoService.findByEspecialidad(nombre), HttpStatus.OK);
    }

    @GetMapping("/profesionales/buscar")
    public ResponseEntity<List<ProfesionalMedico>> buscarPorNombre(
            @RequestParam String termino) {
        return new ResponseEntity<>(
                profesionalMedicoService.buscarPorNombre(termino), HttpStatus.OK);
    }

    @GetMapping("/profesionales/disponibles")
    public ResponseEntity<List<ProfesionalMedico>> findDisponiblesByEspecialidad(
            @RequestParam String especialidad) {
        return new ResponseEntity<>(
                profesionalMedicoService.findDisponiblesPorEspecialidad(especialidad),
                HttpStatus.OK);
    }

    @GetMapping("/profesionales/especialidades")
    public ResponseEntity<List<String>> listarEspecialidades() {
        return new ResponseEntity<>(
                profesionalMedicoService.listarEspecialidades(), HttpStatus.OK);
    }

    @PostMapping("/profesionales")
    public ResponseEntity<ProfesionalMedicoDTO> add(@RequestBody ProfesionalMedicoDTO dto) {
        return new ResponseEntity<>(profesionalMedicoService.add(dto), HttpStatus.CREATED);
    }

    @PutMapping("/profesionales")
    public ResponseEntity<ProfesionalMedicoDTO> update(@RequestBody ProfesionalMedicoDTO dto) {
        return new ResponseEntity<>(profesionalMedicoService.update(dto), HttpStatus.OK);
    }

    @DeleteMapping("/profesionales/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        profesionalMedicoService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
