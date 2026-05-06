package com.arquiweb.grupo3.serana.security;

import com.arquiweb.grupo3.serana.entities.Authority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthoritySecurity implements GrantedAuthority{

    private Authority authority;

    @Override
    public @Nullable String getAuthority() {
        return authority.getRol();
    }
}
