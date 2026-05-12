package com.arquiweb.grupo3.serana.servicesimpl;

import com.arquiweb.grupo3.serana.exceptions.ResourceNotFoundException;
import com.arquiweb.grupo3.serana.entities.Authority;
import com.arquiweb.grupo3.serana.repositories.AuthorityRepository;
import com.arquiweb.grupo3.serana.services.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthorityServiceImpl implements AuthorityService {

    @Autowired
    private AuthorityRepository authorityRepository;

    @Override
    @Transactional(readOnly = true)
    public Authority findById(Long id) {
        return authorityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Rol no encontrado con id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Authority findByRol(String rol) {
        // Retorna null intencionalmente: UsuarioServiceImpl filtra roles inexistentes.
        return authorityRepository.findByRol(rol);
    }

    @Override
    @Transactional
    public Authority add(Authority authority) {
        if (authority == null || authority.getRol() == null || authority.getRol().isBlank()) {
            return null;
        }
        return authorityRepository.save(authority);
    }
}
