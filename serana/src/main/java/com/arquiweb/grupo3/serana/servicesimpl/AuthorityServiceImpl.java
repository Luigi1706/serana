package com.arquiweb.grupo3.serana.servicesimpl;

import com.arquiweb.grupo3.serana.entities.Authority;
import com.arquiweb.grupo3.serana.exceptions.ResourceNotFoundException;
import com.arquiweb.grupo3.serana.repositories.AuthorityRepository;
import com.arquiweb.grupo3.serana.services.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorityServiceImpl implements AuthorityService {

    @Autowired
    private AuthorityRepository authorityRepository;

    @Override
    public Authority findById(Long id) {
        return authorityRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado con id: " + id));
    }

    @Override
    public Authority findByRol(String rol) {
        return authorityRepository.findByRol(rol); // null si no existe (controlado en UsuarioServiceImpl)
    }

    @Override
    public Authority add(Authority authority) {
        if (authority == null || authority.getRol() == null || authority.getRol().isBlank())
            return null;
        return authorityRepository.save(authority);
    }
}
