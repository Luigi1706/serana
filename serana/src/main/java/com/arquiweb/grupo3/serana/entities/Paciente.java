package com.arquiweb.grupo3.serana.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="pacientes")
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombres;
    private String apellidos;
    private char genero;
    private String estadoAnimo;
    private int nivelAnsiedad;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "paciente", fetch = FetchType.EAGER)
    private List<Sesion> sesiones;

    @JsonIgnore
    @ToString.Exclude
    @OneToOne
    @JoinColumn(name="id_usuario")
    private Usuario usuario;

}
