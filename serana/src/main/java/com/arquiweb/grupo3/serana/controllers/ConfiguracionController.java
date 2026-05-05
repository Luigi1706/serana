package com.arquiweb.grupo3.serana.controllers;

import com.arquiweb.grupo3.serana.dtos.ConfiguracionDTO;
import com.arquiweb.grupo3.serana.entities.Configuracion;
import com.arquiweb.grupo3.serana.services.ConfiguracionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/arqui_serana")
public class ConfiguracionController {
    @Autowired
    ConfiguracionService configuracionService;

    @GetMapping("/configuraciones/{id}")
    public ResponseEntity<Configuracion> findById(@PathVariable Long id) {
        Configuracion found = configuracionService.findById(id);
        if (found == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(found, HttpStatus.OK);
    }

    @GetMapping("/configuraciones/usuario/{usuarioId}")
    public ResponseEntity<Configuracion> findByUsuarioId(@PathVariable Long usuarioId) {
        Configuracion found = configuracionService.findByUsuarioId(usuarioId);
        if (found == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(found, HttpStatus.OK);
    }

    @PostMapping("/configuraciones")
    public ResponseEntity<ConfiguracionDTO> add(@RequestBody ConfiguracionDTO configuracionDTO) {
        ConfiguracionDTO newConfig = configuracionService.add(configuracionDTO);
        return new ResponseEntity<>(newConfig, HttpStatus.CREATED);
    }

    @PutMapping("/configuraciones")
    public ResponseEntity<ConfiguracionDTO> update(@RequestBody ConfiguracionDTO configuracionDTO) {
        ConfiguracionDTO updated = configuracionService.update(configuracionDTO);
        if (updated == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }
}
