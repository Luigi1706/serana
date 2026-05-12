package com.arquiweb.grupo3.serana.servicesimpl;

import com.arquiweb.grupo3.serana.exceptions.ResourceNotFoundException;
import com.arquiweb.grupo3.serana.dtos.UsuarioDTO;
import com.arquiweb.grupo3.serana.entities.Authority;
import com.arquiweb.grupo3.serana.entities.Usuario;
import com.arquiweb.grupo3.serana.repositories.UsuarioRepository;
import com.arquiweb.grupo3.serana.services.AuthorityService;
import com.arquiweb.grupo3.serana.services.UsuarioService;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AuthorityService authorityService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public Usuario findById(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Usuario no encontrado con id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Usuario findByCorreo(String correo) {
        Usuario usuario = usuarioRepository.findByCorreo(correo);
        if (usuario == null) {
            throw new ResourceNotFoundException(
                    "Usuario no encontrado con correo: " + correo);
        }
        return usuario;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByCorreo(String correo) {
        return usuarioRepository.existsByCorreo(correo);
    }

    @Override
    @Transactional
    public UsuarioDTO add(UsuarioDTO usuarioDTO) {
        // Validaciones de entrada
        if (usuarioDTO.getCorreo() == null || usuarioDTO.getCorreo().isBlank()) {
            throw new ValidationException("El correo electrónico es obligatorio.");
        }
        if (usuarioDTO.getContrasenia() == null || usuarioDTO.getContrasenia().length() < 6) {
            throw new ValidationException(
                    "La contraseña debe tener al menos 6 caracteres.");
        }
        if (usuarioDTO.getAuthorities() == null || usuarioDTO.getAuthorities().isBlank()) {
            throw new ValidationException(
                    "Se debe especificar al menos un rol para el usuario.");
        }

        String correoNormalizado = usuarioDTO.getCorreo().trim().toLowerCase();

        // Verificar correo duplicado
        if (existsByCorreo(correoNormalizado)) {
            throw new ValidationException(
                    "Ya existe un usuario registrado con el correo: " + correoNormalizado);
        }

        List<Authority> authorityList = authoritiesFromString(usuarioDTO.getAuthorities());
        if (authorityList.isEmpty()) {
            throw new ValidationException(
                    "Ninguno de los roles indicados existe en el sistema.");
        }

        Usuario newUsuario = new Usuario(
                null,
                correoNormalizado,
                passwordEncoder.encode(usuarioDTO.getContrasenia()),
                LocalDateTime.now(),
                true,
                null, null, null, null, null,
                authorityList
        );

        newUsuario = usuarioRepository.save(newUsuario);
        usuarioDTO.setId(newUsuario.getId());
        usuarioDTO.setFechaRegistro(newUsuario.getFechaRegistro());
        // Seguridad: nunca retornar la contraseña en la respuesta
        usuarioDTO.setContrasenia(null);
        return usuarioDTO;
    }

    private List<Authority> authoritiesFromString(String authorities) {
        List<Authority> authorityList = new ArrayList<>();
        Arrays.stream(authorities.split(";"))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .forEach(rolStr -> {
                    Authority authority = authorityService.findByRol(rolStr);
                    if (authority != null) {
                        authorityList.add(authority);
                    }
                });
        return authorityList;
    }
}
