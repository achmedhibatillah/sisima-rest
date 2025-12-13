package com.sisima.api.security;

import java.util.Arrays;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

@Service("access")
public class AccessControlService {

    public boolean hasAnyRole(Authentication auth, String... roles) {
        if (auth == null || !auth.isAuthenticated()) return false;

        return auth.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .anyMatch(a ->
                Arrays.stream(roles)
                    .anyMatch(r -> a.equals("ROLE_" + r.toUpperCase()))
            );
    }

    public boolean isOwner(Authentication auth, String resourceOwnerPublicId) {
        if (auth == null || !auth.isAuthenticated()) return false;

        Object principal = auth.getPrincipal();
        if (!(principal instanceof JwtUserPrincipal user)) return false;

        return user.getPublicId().equals(resourceOwnerPublicId);
    }

    public boolean ownerOrRole(
        Authentication auth,
        String resourceOwnerPublicId,
        String... roles
    ) {
        return isOwner(auth, resourceOwnerPublicId)
            || hasAnyRole(auth, roles);
    }
}
