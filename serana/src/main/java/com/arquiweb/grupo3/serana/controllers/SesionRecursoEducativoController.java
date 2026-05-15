package com.arquiweb.grupo3.serana.controllers;

import com.arquiweb.grupo3.serana.dtos.SesionRecursoEducativoDTO;
import com.arquiweb.grupo3.serana.entities.SesionRecursoEducativo;
import com.arquiweb.grupo3.serana.services.SesionRecursoEducativoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/arqui_serana")
// Controller encargado de gestionar los vínculos entre sesiones y recursos educativos:
// asociar, consultar y eliminar recursos educativos utilizados en una sesión.
public class SesionRecursoEducativoController {
    @Autowired
    SesionRecursoEducativoService sesionRecursoEducativoService;

    // GET /arqui_serana/sesiones-recursos/{id}
    @GetMapping("/sesiones-recursos/{id}")
    public ResponseEntity<SesionRecursoEducativo> findById(@PathVariable Long id) {
        return new ResponseEntity<>(sesionRecursoEducativoService.findById(id), HttpStatus.OK);
    }

    // GET /arqui_serana/sesiones-recursos/sesion/{sesionId}
    // Recursos educativos vinculados a una sesión concreta
    @GetMapping("/sesiones-recursos/sesion/{sesionId}")
    public ResponseEntity<List<SesionRecursoEducativoDTO>> findBySesionId(@PathVariable Long sesionId) {
        return new ResponseEntity<>(sesionRecursoEducativoService.findBySesionId(sesionId), HttpStatus.OK);
    }

    // GET /arqui_serana/sesiones-recursos/recurso/{recursoId}
    // Sesiones en que se usó un recurso educativo concreto
    @GetMapping("/sesiones-recursos/recurso/{recursoId}")
    public ResponseEntity<List<SesionRecursoEducativoDTO>> findByRecursoEducativoId(@PathVariable Long recursoId) {
        return new ResponseEntity<>(sesionRecursoEducativoService.findByRecursoEducativoId(recursoId), HttpStatus.OK);
    }

    // GET /arqui_serana/sesiones-recursos/sesion/{sesionId}/total
    // Cuántos recursos tiene una sesión
    @GetMapping("/sesiones-recursos/sesion/{sesionId}/total")
    public ResponseEntity<Long> contarRecursosPorSesion(@PathVariable Long sesionId) {
        return new ResponseEntity<>(sesionRecursoEducativoService.contarRecursosPorSesion(sesionId), HttpStatus.OK);
    }

    // POST /arqui_serana/sesiones-recursos
    // Vincular un recurso educativo a una sesión
    @PostMapping("/sesiones-recursos")
    public ResponseEntity<SesionRecursoEducativoDTO> add(@RequestBody SesionRecursoEducativoDTO dto) {
        return new ResponseEntity<>(sesionRecursoEducativoService.add(dto), HttpStatus.CREATED);
    }

    // DELETE /arqui_serana/sesiones-recursos/{id}
    // Desvincular recurso de sesión
    @DeleteMapping("/sesiones-recursos/{id}")
    public ResponseEntity<HttpStatus> delete(@PathVariable Long id) {
        sesionRecursoEducativoService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
