package com.arquiweb.grupo3.serana.controllers;

import com.arquiweb.grupo3.serana.dtos.SesionDTO;
import com.arquiweb.grupo3.serana.entities.Sesion;
import com.arquiweb.grupo3.serana.services.SesionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/arqui_serana") // http://localhost:8080/arqui_serana
// Controller encargado de gestionar las sesiones terapéuticas de Serana:
// crear, listar, buscar, actualizar y eliminar sesiones entre paciente y profesional.
public class SesionController {
    @Autowired
    SesionService sesionService;

    //Anadir nueva sesion
    @PostMapping("/sesiones") //  http://localhost:8080/sesiones
    public ResponseEntity<SesionDTO> add(@RequestBody SesionDTO sesionDTO){
        SesionDTO newSesionDTO = sesionService.addDTO(sesionDTO);
        return new ResponseEntity<>(newSesionDTO, HttpStatus.CREATED);
    }

    //Listar todas la sesiones
    @GetMapping("/sesiones")   //  http://localhost:8080/sesiones
    public ResponseEntity<List<Sesion>> listAll() {
        return new ResponseEntity<>(sesionService.listAll(), HttpStatus.OK);
    }

    //Buscar una sesion por su id
    @GetMapping("/sesiones/{sesionId}")   //  http://localhost:8080/sesiones/2  <- un id en particular
    public ResponseEntity<Sesion> findById(@PathVariable("sesionId") Long id) {
        Sesion foundMovie = sesionService.findById(id);
        if (foundMovie==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(foundMovie, HttpStatus.OK);
    }

    //Editar una sesion
    @PutMapping("/sesiones") //  http://localhost:8080/sesiones
    public ResponseEntity<Sesion> update(@RequestBody Sesion sesion){
        Sesion updatedSesion = sesionService.update(sesion);
        return new ResponseEntity<>(updatedSesion, HttpStatus.OK);
    }

    //Eliminar un sesión
    @DeleteMapping("/sesiones/{sesionId}")
    public ResponseEntity<HttpStatus> delete(@PathVariable("sesionId") Long id) {
        sesionService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
