package com.arquiweb.grupo3.serana.servicesimpl;

import com.arquiweb.grupo3.serana.security.UsuarioSecurity;
import com.arquiweb.grupo3.serana.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UsuarioDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UsuarioService usuarioService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            return new UsuarioSecurity(usuarioService.findByCorreo(username));
        } catch (Exception e) {
            throw new UsernameNotFoundException(
                    "No se encontró un usuario con el correo: " + username);
        }
    }
}
