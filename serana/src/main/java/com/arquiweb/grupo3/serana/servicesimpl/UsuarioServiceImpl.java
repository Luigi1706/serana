package com.arquiweb.grupo3.serana.servicesimpl;

import com.arquiweb.grupo3.serana.dtos.UsuarioDTO;
import com.arquiweb.grupo3.serana.entities.Authority;
import com.arquiweb.grupo3.serana.entities.ProfesionalMedico;
import com.arquiweb.grupo3.serana.entities.Usuario;
import com.arquiweb.grupo3.serana.exceptions.ResourceNotFoundException;
import com.arquiweb.grupo3.serana.repositories.UsuarioRepository;
import com.arquiweb.grupo3.serana.services.AuthorityService;
import com.arquiweb.grupo3.serana.services.PacienteService;
import com.arquiweb.grupo3.serana.services.ProfesionalMedicoService;
import com.arquiweb.grupo3.serana.services.UsuarioService;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private AuthorityService authorityService;

    /*
    @Autowired
    private PacienteService pacienteService;

    @Autowired
    private ProfesionalMedicoService profesionalMedicoService;
     */

    /** Bean declarado en SecurityConfiguration — no instanciar manualmente */
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Usuario findById(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado con id: " + id));
    }

    @Override
    public Usuario findByCorreo(String correo) {
        Usuario usuario = usuarioRepository.findByCorreo(correo);
        if (usuario == null)
            throw new ResourceNotFoundException("Usuario no encontrado con correo: " + correo);
        return usuario;
    }

    @Override
    public boolean existsByCorreo(String correo) {
        return usuarioRepository.existsByCorreo(correo);
    }

    @Override
    @Transactional
    public UsuarioDTO add(UsuarioDTO usuarioDTO) {
        // Validaciones básicas
        if (usuarioDTO.getCorreo() == null || usuarioDTO.getCorreo().isBlank())
            throw new ValidationException("El correo electrónico es obligatorio.");
        if (usuarioDTO.getContrasenia() == null || usuarioDTO.getContrasenia().length() < 6)
            throw new ValidationException("La contraseña debe tener al menos 6 caracteres.");
        if (usuarioDTO.getTipoPerfil() == null || usuarioDTO.getTipoPerfil().isBlank())
            throw new ValidationException("Debe indicar tipoPerfil: 'PACIENTE' o 'DOCTOR'.");

        String correoNormalizado = usuarioDTO.getCorreo().trim().toLowerCase();
        if (existsByCorreo(correoNormalizado))
            throw new ValidationException("Ya existe un usuario registrado con el correo: " + correoNormalizado);

        // Asignar rol automáticamente según tipoPerfil — ROLE_USER eliminado
        String rolNombre = switch (usuarioDTO.getTipoPerfil().trim().toUpperCase()) {
            case "PACIENTE" -> "ROLE_PACIENTE";
            case "DOCTOR" -> "ROLE_DOCTOR";
            default -> throw new ValidationException(
                    "tipoPerfil inválido. Use 'PACIENTE' o 'DOCTOR'.");
        };

        /*
        // Crear un nuevo registro para Paciente o Profesional_Medico
        if(usuarioDTO.getTipoPerfil().equals("PACIENTE")){
            pacienteService.add();
        }
        else if(usuarioDTO.getTipoPerfil().equals("DOCTOR")){
            profesionalMedicoService.add();
        }
        */

        Authority authority = authorityService.findByRol(rolNombre);
        if (authority == null)
            throw new ValidationException("El rol '" + rolNombre + "' no está configurado en la base de datos.");

        Usuario newUsuario = new Usuario(
                null,
                correoNormalizado,
                passwordEncoder.encode(usuarioDTO.getContrasenia()),
                LocalDateTime.now(),
                true,
                null, null, null, null, null, null, null,
                List.of(authority)
        );

        newUsuario = usuarioRepository.save(newUsuario);
        usuarioDTO.setId(newUsuario.getId());
        usuarioDTO.setFechaRegistro(newUsuario.getFechaRegistro());
        usuarioDTO.setAuthorities(rolNombre);
        usuarioDTO.setContrasenia(null); // seguridad: no retornar la contraseña
        return usuarioDTO;
    }
}
