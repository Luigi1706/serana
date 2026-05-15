package com.arquiweb.grupo3.serana.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioComentarioDTO {
    private Long id;
    /** ¿El usuario dio like a este comentario? */
    private Boolean liked;
    /** ¿Este usuario es quien publicó el comentario? */
    private Boolean comentarista;
    private Long idUsuario;
    private Long idComentario;
}
