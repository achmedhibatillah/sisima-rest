package com.sisima.api.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.sisima.api.config.JwtUtil;
import com.sisima.api.domain.auth.model.AuthErrorResponse;
import com.sisima.api.enums.AuthDenyReasonEnum;
import com.sisima.api.security.AllowPermitEndpointConst;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.web.filter.OncePerRequestFilter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private static final Logger log =
        LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return Arrays.stream(AllowPermitEndpointConst.PUBLIC)
            .anyMatch(p -> new AntPathRequestMatcher(p).matches(request));
    }

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("[AUTH] MISSING_TOKEN {} {}", 
                request.getMethod(), request.getRequestURI());

            writeAuthError(response, AuthDenyReasonEnum.MISSING_TOKEN);
            return;
        }

        String token = authHeader.substring(7);

        if (!jwtUtil.validateToken(token)) {
            log.warn("[AUTH] INVALID_TOKEN {} {}", 
                request.getMethod(), request.getRequestURI());

            writeAuthError(response, AuthDenyReasonEnum.INVALID_TOKEN);
            return;
        }

        String publicId = jwtUtil.extractPublicId(token);
        String role = jwtUtil.extractRole(token);

        log.info("[AUTH] OK {} {} user={} role={}",
            request.getMethod(),
            request.getRequestURI(),
            publicId,
            role
        );

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            List<GrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority("ROLE_" + role));

            UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(
                    publicId, null, authorities
                );

            SecurityContextHolder.getContext()
                .setAuthentication(authToken);
        }

        filterChain.doFilter(request, response);
    }

    private void writeAuthError(
        HttpServletResponse response,
        AuthDenyReasonEnum reason
    ) throws IOException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");

        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(
            mapper.writeValueAsString(AuthErrorResponse.of(reason))
        );
    }

}
