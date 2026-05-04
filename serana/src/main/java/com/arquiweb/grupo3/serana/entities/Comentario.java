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
@Table(name="comentarios")
public class Comentario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fechaPublicacion;
    private String contenido;

    @ToString.Exclude
    @JsonIgnore
    @OneToMany(mappedBy = "comentario", fetch=FetchType.EAGER)
    private List<UsuarioComentario> usuariosComentarios;

    @ToString.Exclude
    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="id_post")
    private Post post;
}
