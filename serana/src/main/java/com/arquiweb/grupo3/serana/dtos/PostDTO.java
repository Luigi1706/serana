package com.arquiweb.grupo3.serana.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PostDTO {
    private Long id;
    private String tema;
    private String contenido;
    private Boolean modoAnonimo;
    private Integer cantidadParticipantes;
    private Long usuarioId;
}
