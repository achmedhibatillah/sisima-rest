package com.sisima.api.security;

import java.util.Arrays;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import com.sisima.api.enums.AuthDenyReasonEnum;
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

    // VERSION 2

    private String user(Authentication auth) {
        return auth == null
            ? "anonymous"
            : auth.getName();
    }

    public AuthDecision rolesCanAccess_v2(
        Authentication auth,
        String[] allowedRoles
    ) {
        String user = user(auth);

        if (auth == null || !auth.isAuthenticated()) {
            log.warn(
                "[AUTHZ][ROLE] DENY user={} reason=MISSING_TOKEN allowedRoles={}",
                user, Arrays.toString(allowedRoles)
            );
            return new AuthDecision(false, AuthDenyReasonEnum.MISSING_TOKEN);
        }

        log.debug(
            "[AUTHZ][ROLE] CHECK user={} authorities={} allowedRoles={}",
            user,
            auth.getAuthorities(),
            Arrays.toString(allowedRoles)
        );

        boolean hasRole = auth.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .anyMatch(role ->
                Arrays.stream(allowedRoles)
                    .anyMatch(allowed ->
                        role.equals("ROLE_" + allowed))
            );

        if (hasRole) {
            log.info(
                "[AUTHZ][ROLE] ALLOW user={} matchedRole",
                user
            );
            return new AuthDecision(true, null);
        }

        log.warn(
            "[AUTHZ][ROLE] DENY user={} reason=NO_ACCESS",
            user
        );

        return new AuthDecision(false, AuthDenyReasonEnum.NO_ACCESS);
    }

    public AuthDecision ownerCanAccess_v2(
        Authentication auth,
        String resourceOwnerPublicId
    ) {
        String user = user(auth);

        if (auth == null || !auth.isAuthenticated()) {
            log.warn(
                "[AUTHZ][OWNER] DENY user={} reason=MISSING_TOKEN owner={}",
                user, resourceOwnerPublicId
            );
            return new AuthDecision(false, AuthDenyReasonEnum.MISSING_TOKEN);
        }

        log.debug(
            "[AUTHZ][OWNER] CHECK user={} owner={}",
            user, resourceOwnerPublicId
        );

        if (auth.getName().equals(resourceOwnerPublicId)) {
            log.info(
                "[AUTHZ][OWNER] ALLOW user={}",
                user
            );
            return new AuthDecision(true, null);
        }

        log.warn(
            "[AUTHZ][OWNER] DENY user={} reason=NO_ACCESS owner={}",
            user, resourceOwnerPublicId
        );

        return new AuthDecision(false, AuthDenyReasonEnum.NO_ACCESS);
    }

    public AuthDecision roleOrOwnerCanAccess_v2(
        Authentication auth,
        String[] roles,
        String ownerPublicId
    ) {
        String user = user(auth);

        log.debug(
            "[AUTHZ][ROLE_OR_OWNER] START user={} roles={} owner={}",
            user,
            Arrays.toString(roles),
            ownerPublicId
        );

        AuthDecision role = rolesCanAccess_v2(auth, roles);
        if (role.allowed()) {
            log.info(
                "[AUTHZ][ROLE_OR_OWNER] ALLOW user={} via=ROLE",
                user
            );
            return role;
        }

        AuthDecision owner = ownerCanAccess_v2(auth, ownerPublicId);
        if (owner.allowed()) {
            log.info(
                "[AUTHZ][ROLE_OR_OWNER] ALLOW user={} via=OWNER",
                user
            );
            return owner;
        }

        AuthDecision finalDecision =
            role.reason() == AuthDenyReasonEnum.MISSING_TOKEN
                ? role
                : owner;

        log.warn(
            "[AUTHZ][ROLE_OR_OWNER] DENY user={} reason={}",
            user,
            finalDecision.reason()
        );

        return finalDecision;
    }

}
