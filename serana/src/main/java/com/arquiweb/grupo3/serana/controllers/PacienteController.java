package com.arquiweb.grupo3.serana.controllers;

// Controller encargado de gestionar los perfiles de pacientes de Serana:
// registro, consulta por id/usuario/estado anímico, actualización y eliminación.

import com.arquiweb.grupo3.serana.dtos.PacienteDTO;
import com.arquiweb.grupo3.serana.entities.Paciente;
import com.arquiweb.grupo3.serana.services.PacienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/arqui_serana")
public class PacienteController {

    @Autowired
    private PacienteService pacienteService;

    @GetMapping("/pacientes")
    public ResponseEntity<List<Paciente>> findAll() {
        return new ResponseEntity<>(pacienteService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/pacientes/{id}")
    public ResponseEntity<Paciente> findById(@PathVariable Long id) {
        return new ResponseEntity<>(pacienteService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/pacientes/usuario/{idUsuario}")
    public ResponseEntity<Paciente> findByUsuarioId(@PathVariable Long idUsuario) {
        return new ResponseEntity<>(pacienteService.findByUsuarioId(idUsuario), HttpStatus.OK);
    }

    @GetMapping("/pacientes/estado")
    public ResponseEntity<List<Paciente>> findByEstadoAnimo(@RequestParam String estado) {
        return new ResponseEntity<>(pacienteService.findByEstadoAnimo(estado), HttpStatus.OK);
    }

    @GetMapping("/pacientes/buscar")
    public ResponseEntity<List<Paciente>> buscarPorNombre(@RequestParam String termino) {
        return new ResponseEntity<>(pacienteService.buscarPorNombre(termino), HttpStatus.OK);
    }

    @GetMapping("/pacientes/criticos")
    public ResponseEntity<List<Paciente>> obtenerCriticos(@RequestParam(defaultValue = "7") int nivelMinimo) {
        return new ResponseEntity<>(pacienteService.obtenerPacientesCriticos(nivelMinimo), HttpStatus.OK);
    }

    @PostMapping("/pacientes")
    public ResponseEntity<PacienteDTO> add(@RequestBody PacienteDTO dto) {
        return new ResponseEntity<>(pacienteService.add(dto), HttpStatus.CREATED);
    }

    @PutMapping("/pacientes")
    public ResponseEntity<PacienteDTO> update(@RequestBody PacienteDTO dto) {
        return new ResponseEntity<>(pacienteService.update(dto), HttpStatus.OK);
    }

    @DeleteMapping("/pacientes/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        pacienteService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
