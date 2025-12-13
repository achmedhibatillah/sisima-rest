package com.sisima.api.security;

import java.util.Arrays;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

@Service
public class AccessControlService {

    public boolean rolesCanAccess(Authentication auth, String[] allowedRoles) {
        if (auth == null || !auth.isAuthenticated()) return false;

        String currentRole = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse("");

        return Arrays.stream(allowedRoles)
                .anyMatch(r -> currentRole.equals("ROLE_" + r.toUpperCase()));
    }

    public boolean ownerCanAccess(Authentication auth, String resourceOwnerPublicId) {
        if (auth == null || !auth.isAuthenticated()) return false;

        String currentUserPublicId = auth.getName();
        return currentUserPublicId.equals(resourceOwnerPublicId);
    }
}
