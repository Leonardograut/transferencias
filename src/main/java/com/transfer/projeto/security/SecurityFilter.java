package com.transfer.projeto.security;

import com.transfer.projeto.model.Usuario;
import com.transfer.projeto.repository.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        final String token = this.recordToken(request);

        if (token != null && !token.isBlank()) {
            Optional<String> subject = tokenService.validateToken(token);

            subject.ifPresent(username -> {
                Usuario user = usuarioRepository.findByUsername(username).orElse(null);

                if (user != null) {
                    final UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                    request.setAttribute("authenticatedUsername", username);
                }
            });
        }

        filterChain.doFilter(request, response);
    }

    private String recordToken(HttpServletRequest request) {
        final String authorization = request.getHeader("Authorization");

        return Optional.ofNullable(authorization)
                .map(auth -> auth.replace("Bearer ", "").trim())
                .orElse(null);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();

        return path.startsWith("/auth/")
                || path.startsWith("/swagger-ui/")
                || path.equals("/swagger-ui.html")
                || path.startsWith("/v3/api-docs/");
    }
}
