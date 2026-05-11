package com.arquiweb.grupo3.serana.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioComentarioDTO {

    private Long idUsuario;
    private Long idComentario;
    private Boolean liked;
    private Boolean comentarista;
    private String correoUsuario;
    private String contenidoComentario;
    private Long totalLikes;
}