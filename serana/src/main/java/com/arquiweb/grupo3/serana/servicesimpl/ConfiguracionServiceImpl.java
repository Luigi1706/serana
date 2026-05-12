package com.arquiweb.grupo3.serana.servicesimpl;

import com.arquiweb.grupo3.serana.exceptions.ResourceNotFoundException;
import com.arquiweb.grupo3.serana.dtos.ConfiguracionDTO;
import com.arquiweb.grupo3.serana.entities.Configuracion;
import com.arquiweb.grupo3.serana.entities.Usuario;
import com.arquiweb.grupo3.serana.repositories.ConfiguracionRepository;
import com.arquiweb.grupo3.serana.services.ConfiguracionService;
import com.arquiweb.grupo3.serana.services.UsuarioService;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConfiguracionServiceImpl implements ConfiguracionService {

    @Autowired
    private ConfiguracionRepository configuracionRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Override
    @Transactional(readOnly = true)
    public Configuracion findById(Long id) {
        return configuracionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Configuración no encontrada con id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Configuracion findByUsuarioId(Long usuarioId) {
        Usuario usuario = usuarioService.findById(usuarioId);
        if (usuario.getConfiguracion() == null) {
            throw new ResourceNotFoundException(
                    "El usuario con id " + usuarioId + " no tiene configuración registrada.");
        }
        return usuario.getConfiguracion();
    }

    @Override
    @Transactional
    public ConfiguracionDTO add(ConfiguracionDTO configuracionDTO) {
        if (configuracionDTO.getIdUsuario() == null) {
            throw new ValidationException("El id del usuario es obligatorio.");
        }

        Usuario usuario = usuarioService.findById(configuracionDTO.getIdUsuario());

        // Evitar duplicado: un usuario solo puede tener una configuración
        if (usuario.getConfiguracion() != null) {
            throw new ValidationException(
                    "El usuario ya tiene una configuración registrada. "
                            + "Use el endpoint de actualización (PUT /configuraciones).");
        }

        Configuracion newConfiguracion = new Configuracion(
                null,
                configuracionDTO.getTema() != null
                        ? configuracionDTO.getTema() : "CLARO",
                configuracionDTO.getIdioma() != null
                        ? configuracionDTO.getIdioma() : "ES",
                configuracionDTO.getNotificacionHabilitada() != null
                        ? configuracionDTO.getNotificacionHabilitada() : Boolean.TRUE,
                usuario
        );

        newConfiguracion = configuracionRepository.save(newConfiguracion);
        configuracionDTO.setId(newConfiguracion.getId());
        return configuracionDTO;
    }

    @Override
    @Transactional
    public ConfiguracionDTO update(ConfiguracionDTO configuracionDTO) {
        if (configuracionDTO.getId() == null) {
            throw new ValidationException(
                    "El id de la configuración es requerido para actualizar.");
        }

        Configuracion foundConfiguracion = findById(configuracionDTO.getId());

        if (configuracionDTO.getTema() != null && !configuracionDTO.getTema().isBlank()) {
            foundConfiguracion.setTema(configuracionDTO.getTema());
        }
        if (configuracionDTO.getIdioma() != null && !configuracionDTO.getIdioma().isBlank()) {
            foundConfiguracion.setIdioma(configuracionDTO.getIdioma());
        }
        if (configuracionDTO.getNotificacionHabilitada() != null) {
            foundConfiguracion.setNotificacionHabilitada(
                    configuracionDTO.getNotificacionHabilitada());
        }

        foundConfiguracion = configuracionRepository.save(foundConfiguracion);
        configuracionDTO.setId(foundConfiguracion.getId());
        return configuracionDTO;
    }
}
