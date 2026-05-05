package com.arquiweb.grupo3.serana.servicesimpl;

import com.arquiweb.grupo3.serana.dtos.ConfiguracionDTO;
import com.arquiweb.grupo3.serana.entities.Configuracion;
import com.arquiweb.grupo3.serana.entities.Usuario;
import com.arquiweb.grupo3.serana.repositories.ConfiguracionRepository;
import com.arquiweb.grupo3.serana.services.ConfiguracionService;
import com.arquiweb.grupo3.serana.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConfiguracionServiceImpl implements ConfiguracionService {

    @Autowired
    ConfiguracionRepository configuracionRepository;
    @Autowired
    UsuarioService usuarioService;


    @Override
    public Configuracion findById(Long id) {
        return configuracionRepository.findById(id).get();
    }

    @Override
    public Configuracion findByUsuarioId(Long usuarioId) {
        Usuario usuario = usuarioService.findById(usuarioId);
        if (usuario == null || usuario.getConfiguracion() == null) {
            return null;
        }
        return usuario.getConfiguracion();
    }

    @Override
    public ConfiguracionDTO add(ConfiguracionDTO configuracionDTO) {
        Usuario usuario = usuarioService.findById(configuracionDTO.getIdUsuario());

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
    public ConfiguracionDTO update(ConfiguracionDTO configuracionDTO) {
        Configuracion foundConfiguracion = findById(configuracionDTO.getId());
        if (foundConfiguracion == null) {
            return null;
        }

        if (configuracionDTO.getTema() != null && !configuracionDTO.getTema().isBlank()) {
            foundConfiguracion.setTema(configuracionDTO.getTema());
        }
        if (configuracionDTO.getIdioma() != null && !configuracionDTO.getIdioma().isBlank()) {
            foundConfiguracion.setIdioma(configuracionDTO.getIdioma());
        }
        if (configuracionDTO.getNotificacionHabilitada() != null) {
            foundConfiguracion.setNotificacionHabilitada(configuracionDTO.getNotificacionHabilitada());
        }

        foundConfiguracion = configuracionRepository.save(foundConfiguracion);
        configuracionDTO.setId(foundConfiguracion.getId());
        return configuracionDTO;
    }
}
