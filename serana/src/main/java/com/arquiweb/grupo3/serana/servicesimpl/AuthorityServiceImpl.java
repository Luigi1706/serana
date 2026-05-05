package com.arquiweb.grupo3.serana.servicesimpl;

import com.arquiweb.grupo3.serana.entities.Authority;
import com.arquiweb.grupo3.serana.repositories.AuthorityRepository;
import com.arquiweb.grupo3.serana.services.AuthorityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthorityServiceImpl implements AuthorityService {
    @Autowired
    AuthorityRepository authorityRepository;

    @Override
    public Authority findById(Long id) {
        return authorityRepository.findById(id).orElse(null);
    }

    @Override
    public Authority findByRol(String rol) {
        return authorityRepository.findByRol(rol);
    }

    @Override
    public Authority add(Authority authority) {
        return null;
    }
}
