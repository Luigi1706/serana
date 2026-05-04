package com.arquiweb.grupo3.serana.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="recursos_educativos")
public class RecursosEducativos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String tipoContenido;
    private String link;
    private LocalDateTime fechaPublicacion;

    @JsonIgnore
    @ToString.Exclude
    @OneToMany(mappedBy = "recursoEducativo", fetch = FetchType.EAGER)
    private List<SesionRecursoEducativo> sesionesRecursosEducativos;
}
