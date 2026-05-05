package com.arquiweb.grupo3.serana.servicesimpl;

import com.arquiweb.grupo3.serana.dtos.UsuarioDTO;
import com.arquiweb.grupo3.serana.entities.Authority;
import com.arquiweb.grupo3.serana.entities.Usuario;
import com.arquiweb.grupo3.serana.repositories.UsuarioRepository;
import com.arquiweb.grupo3.serana.services.AuthorityService;
import com.arquiweb.grupo3.serana.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class UsuarioServiceImpl implements UsuarioService {
    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    AuthorityService authorityService;

    @Override
    public Usuario findById(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    @Override
    public Usuario findByCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }

    @Override
    public boolean existsByCorreo(String correo) {
        return usuarioRepository.existsByCorreo(correo);
    }


    private List<Authority> authoritiesFromString(String authorities) {

        List<Authority> authorityList = new ArrayList<>();
        List<String> authorityStringList = Arrays.stream(authorities.split(";")).toList();
        for (String authorityString : authorityStringList) {
            Authority authority = authorityService.findByRol(authorityString);
            if (authority != null) {
                authorityList.add(authority);
            }
        }
        return authorityList;
    }
    @Override
    public UsuarioDTO add(UsuarioDTO usuarioDTO) {
        List<Authority> authorityList = authoritiesFromString(usuarioDTO.getAuthorities());

        Usuario newUsuario = new Usuario(
                null,
                usuarioDTO.getCorreo(),
                new BCryptPasswordEncoder().encode(usuarioDTO.getContrasenia()),
                true,
                null,
                null,
                null,
                null,
                null,
                authorityList);

        newUsuario = usuarioRepository.save(newUsuario);
        usuarioDTO.setId(newUsuario.getId());
        usuarioDTO.setFechaRegistro(newUsuario.getFechaRegistro());
        return usuarioDTO;
    }
}

