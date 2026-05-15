package com.arquiweb.grupo3.serana.controllers;

import com.arquiweb.grupo3.serana.entities.RecursoEducativo;
import com.arquiweb.grupo3.serana.services.RecursoEducativoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/arqui_serana")
// Controller encargado de gestionar los recursos educativos de Serana:
// videos, artículos y audios de salud mental publicados por profesionales.
public class RecursoEducativoController {

    @Autowired
    RecursoEducativoService recursosEducativosService;

    //Anadir nuevo recurso educativo
    @PostMapping("/recursos_educativos") //  http://localhost:8080/arqui_serana/recursos_educativos
    public ResponseEntity<RecursoEducativo> add(@RequestBody RecursoEducativo recursosEducativos){
        RecursoEducativo newRecursosEducativosService= recursosEducativosService.add(recursosEducativos);
        return new ResponseEntity<>(newRecursosEducativosService, HttpStatus.CREATED);
    }

    //Listar todos los recursos educativos
    @GetMapping("/recursos_educativos")   //  http://localhost:8080/arqui_serana/recursos_educativos
    public ResponseEntity<List<RecursoEducativo>> listAll() {
        return new ResponseEntity<>(recursosEducativosService.listAll(), HttpStatus.OK);
    }

    //Buscar una recurso educativo por su id
    @GetMapping("/recursos_educativos/{recursos_educativosId}")   //  http://localhost:8080/arqui_serana/recursos_educativos/2  <- un id en particular
    public ResponseEntity<RecursoEducativo> findById(@PathVariable("recursos_educativosId") Long id) {
        RecursoEducativo foundRecursosEducativos = recursosEducativosService.findById(id);
        if (foundRecursosEducativos==null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(foundRecursosEducativos, HttpStatus.OK);
    }

    //Editar un recurso educativo
    @PutMapping("/recursos_educativos") //  http://localhost:8080/arqui_serana/recursos_educativos
    public ResponseEntity<RecursoEducativo> update(@RequestBody RecursoEducativo recursosEducativos){
        RecursoEducativo updatedRecursosEducativos = recursosEducativosService.update(recursosEducativos);
        return new ResponseEntity<>(updatedRecursosEducativos, HttpStatus.OK);
    }

    //Eliminar un sesión
    @DeleteMapping("/recursos_educativos/{recursos_educativosId}") //  http://localhost:8080/arqui_serana/recursos_educativos/2
    public ResponseEntity<HttpStatus> delete(@PathVariable("recursos_educativosId") Long id) {
        recursosEducativosService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
