package com.arquiweb.grupo3.serana.services;

import com.arquiweb.grupo3.serana.dtos.UsuarioDTO;
import com.arquiweb.grupo3.serana.entities.Usuario;

public interface UsuarioService {
    public Usuario findById(Long id);
    public Usuario findByCorreo(String correo);
    public boolean existsByCorreo(String correo);
    public UsuarioDTO add(UsuarioDTO usuarioDTO);
}
