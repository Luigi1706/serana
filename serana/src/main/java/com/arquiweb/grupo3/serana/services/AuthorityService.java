package com.arquiweb.grupo3.serana.services;

import com.arquiweb.grupo3.serana.entities.Authority;

public interface AuthorityService {
    public Authority findById(Long id);
    public Authority findByRol(String rol);

    public Authority add(Authority authority);
}
