package com.arquiweb.grupo3.serana.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfesionalMedicoDTO {

    private Long id;

    private String nombres;

    private String apellidos;

    private String especialidad;

    private String lugarTrabajo;

    private Long idUsuario;
}