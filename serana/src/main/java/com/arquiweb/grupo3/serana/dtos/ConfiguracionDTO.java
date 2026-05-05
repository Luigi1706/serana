package com.arquiweb.grupo3.serana.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfiguracionDTO {
    private Long id;
    private String tema;
    private String idioma;
    private Boolean notificacionHabilitada;
    private Long idUsuario;
}
