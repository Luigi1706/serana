package com.arquiweb.grupo3.serana.services;

import com.arquiweb.grupo3.serana.dtos.ConfiguracionDTO;
import com.arquiweb.grupo3.serana.entities.Configuracion;

public interface ConfiguracionService {
    public Configuracion findById(Long id);
    public Configuracion findByUsuarioId(Long usuarioId);
    public ConfiguracionDTO add(ConfiguracionDTO configuracionDTO);
    public ConfiguracionDTO update(ConfiguracionDTO configuracionDTO);


}
