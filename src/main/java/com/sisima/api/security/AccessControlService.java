package com.sisima.api.security;

import java.util.Arrays;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import com.sisima.api.filter.JwtAuthenticationFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AccessControlService {

    private static final Logger log =
        LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    public boolean rolesCanAccess(Authentication auth, String[] allowedRoles) {
        if (auth == null || !auth.isAuthenticated()) return false;

        log.info(
            "[DEBUG] user={} authorities={} allowed={}",
            auth.getName(),
            auth.getAuthorities(),
            Arrays.toString(allowedRoles)
        );

        return auth.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .anyMatch(userRole ->
                Arrays.stream(allowedRoles)
                    .anyMatch(allowed ->
                        userRole.equals("ROLE_" + allowed.toUpperCase())
                    )
            );
    }

    public boolean ownerCanAccess(Authentication auth, String resourceOwnerPublicId) {
        if (auth == null || !auth.isAuthenticated()) return false;

        String currentUserPublicId = auth.getName();
        return currentUserPublicId.equals(resourceOwnerPublicId);
    }
}
