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
    private ConfiguracionService configuracionService;

    @GetMapping("/configuraciones/{id}")
    public ResponseEntity<Configuracion> findById(@PathVariable Long id) {
        return new ResponseEntity<>(configuracionService.findById(id), HttpStatus.OK);
    }

    @GetMapping("/configuraciones/usuario/{usuarioId}")
    public ResponseEntity<Configuracion> findByUsuarioId(@PathVariable Long usuarioId) {
        return new ResponseEntity<>(
                configuracionService.findByUsuarioId(usuarioId), HttpStatus.OK);
    }

    @PostMapping("/configuraciones")
    public ResponseEntity<ConfiguracionDTO> add(@RequestBody ConfiguracionDTO configuracionDTO) {
        return new ResponseEntity<>(configuracionService.add(configuracionDTO), HttpStatus.CREATED);
    }

    @PutMapping("/configuraciones")
    public ResponseEntity<ConfiguracionDTO> update(@RequestBody ConfiguracionDTO configuracionDTO) {
        return new ResponseEntity<>(configuracionService.update(configuracionDTO), HttpStatus.OK);
    }
}
