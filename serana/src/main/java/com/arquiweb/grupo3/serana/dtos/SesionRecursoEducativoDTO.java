package com.arquiweb.grupo3.serana.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SesionRecursoEducativoDTO {
    private Long id;
    private Long idSesion;
    private Long idRecursoEducativo;

    // Campos informativos en respuesta
    private String tipoSesion;
    private String tituloRecurso;
    private String tipoContenido;
    private String linkRecurso;
}
