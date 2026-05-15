package com.arquiweb.grupo3.serana.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SesionDTO {
    private Long id;

    private LocalDate fecha;
    private LocalTime hora;
    private String tipoSesion;
    private String estadoSesion;
    private String comentario;

    private Long pacienteId ;
    private Long profesionalMedicoId;

    private String pacienteNombres;
    private String pacienteApellidos;

    private String profesionalMedicoNombres;
    private String profesionalMedicoApellidos;
}
