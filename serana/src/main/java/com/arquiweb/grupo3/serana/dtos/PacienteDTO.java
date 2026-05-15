package com.arquiweb.grupo3.serana.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PacienteDTO {
    private Long id;
    private String nombres;
    private String apellidos;
    private char genero;
    private String estadoAnimo;
    private int nivelAnsiedad;
    /** Id del usuario de la plataforma vinculado a este perfil de paciente. */
    private Long idUsuario;
}
