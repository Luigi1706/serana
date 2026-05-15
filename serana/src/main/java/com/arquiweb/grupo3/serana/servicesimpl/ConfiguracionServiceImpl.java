package com.arquiweb.grupo3.serana.servicesimpl;

import com.arquiweb.grupo3.serana.dtos.ConfiguracionDTO;
import com.arquiweb.grupo3.serana.entities.Configuracion;
import com.arquiweb.grupo3.serana.entities.Usuario;
import com.arquiweb.grupo3.serana.exceptions.ResourceNotFoundException;
import com.arquiweb.grupo3.serana.repositories.ConfiguracionRepository;
import com.arquiweb.grupo3.serana.services.ConfiguracionService;
import com.arquiweb.grupo3.serana.services.UsuarioService;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfiguracionServiceImpl implements ConfiguracionService {

    @Autowired
    private ConfiguracionRepository configuracionRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Override
    public Configuracion findById(Long id) {
        return configuracionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Configuración no encontrada con id: " + id));
    }

    @Override
    public Configuracion findByUsuarioId(Long usuarioId) {
        Usuario usuario = usuarioService.findById(usuarioId);
        if (usuario.getConfiguracion() == null)
            throw new ResourceNotFoundException("El usuario con id " + usuarioId + " no tiene configuración registrada.");
        return usuario.getConfiguracion();
    }

    @Override
    @Transactional
    public ConfiguracionDTO add(ConfiguracionDTO configuracionDTO) {
        if (configuracionDTO.getIdUsuario() == null)
            throw new ValidationException("El id del usuario es obligatorio.");

        Usuario usuario = usuarioService.findById(configuracionDTO.getIdUsuario());

        if (usuario.getConfiguracion() != null)
            throw new ValidationException("El usuario ya tiene una configuración. Use PUT /configuraciones para actualizar.");

        Configuracion newConfiguracion = new Configuracion(
                null,
                configuracionDTO.getTema() != null ? configuracionDTO.getTema() : "CLARO",
                configuracionDTO.getIdioma() != null ? configuracionDTO.getIdioma() : "ES",
                configuracionDTO.getNotificacionHabilitada() != null ? configuracionDTO.getNotificacionHabilitada() : true,
                usuario
        );

        newConfiguracion = configuracionRepository.save(newConfiguracion);
        configuracionDTO.setId(newConfiguracion.getId());
        return configuracionDTO;
    }

    @Override
    @Transactional
    public ConfiguracionDTO update(ConfiguracionDTO configuracionDTO) {
        if (configuracionDTO.getId() == null)
            throw new ValidationException("El id de la configuración es requerido para actualizar.");

        Configuracion found = findById(configuracionDTO.getId());

        if (configuracionDTO.getTema() != null && !configuracionDTO.getTema().isBlank())
            found.setTema(configuracionDTO.getTema());
        if (configuracionDTO.getIdioma() != null && !configuracionDTO.getIdioma().isBlank())
            found.setIdioma(configuracionDTO.getIdioma());
        if (configuracionDTO.getNotificacionHabilitada() != null)
            found.setNotificacionHabilitada(configuracionDTO.getNotificacionHabilitada());

        configuracionRepository.save(found);
        return configuracionDTO;
    }
}
