package com.arquiweb.grupo3.serana.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="sesiones")
public class Sesion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate fecha;
    private LocalTime hora;
    private String tipoSesion;
    private String estadoSesion;
    private String comentario;

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name="id_paciente")
    private Paciente paciente;

    @JsonIgnore
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name="id_profesional_medico")
    private ProfesionalMedico profesionalMedico;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "sesion", fetch = FetchType.EAGER)
    private List<SesionRecursoEducativo> sesionesRecursosEducativos;
}
