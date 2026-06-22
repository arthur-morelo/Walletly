package com.proint.walletly.utils;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    @Autowired
    private JwtUtils jwtUtil;
    
    @Autowired
    private CustomUserDetailsService userDetailsService;
    
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        // Ignora qualquer rota que contenha "/auth/" (login, register, etc)
        return path.contains("/auth/");
    }
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                  HttpServletResponse response, 
                                  FilterChain filterChain) throws ServletException, IOException {
        
        String path = request.getServletPath();
        if (path.contains("/auth/")) {
            System.out.println("[JWT-DEBUG] Bypass absoluto ativado para rota pública: " + path);
            filterChain.doFilter(request, response);
            return;
        }

        try {
            System.out.println("========== DEBUG JWT FILTER ==========");
            System.out.println("Header Auth: " + request.getHeader("Authorization"));
            System.out.println("Content Type: " + request.getContentType());
            System.out.println("Method: " + request.getMethod());
            System.out.println("URI: " + request.getRequestURI());
            System.out.println("======================================");
            
            final String authorizationHeader = request.getHeader("Authorization");

            // Ignora requisições de preflight (CORS)
            if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
                System.out.println("[JWT-DEBUG] Ignorando requisição OPTIONS.");
                filterChain.doFilter(request, response);
                return;
            }

            String username = null;
            String jwt = null;
            
            // 1. Tenta extrair o token do cabeçalho Authorization PRIMEIRO
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ") && authorizationHeader.length() > 7) {
                jwt = authorizationHeader.substring(7).trim();
                System.out.println("[JWT-DEBUG] Token extraído do cabeçalho Authorization.");
            } else {
                System.out.println("[JWT-DEBUG] Cabeçalho Authorization ausente ou inválido. Verificando Cookies...");
            }
            
            // 2. Fallback para os Cookies caso não exista header Authorization
            if (jwt == null && request.getCookies() != null) {
                for (jakarta.servlet.http.Cookie cookie : request.getCookies()) {
                    if ("jwt".equals(cookie.getName())) {
                        jwt = cookie.getValue();
                        System.out.println("[JWT-DEBUG] Token extraído do Cookie.");
                        break;
                    }
                }
            }
            
            if (jwt != null) {
                try {
                    username = jwtUtil.extractUsername(jwt);
                } catch (io.jsonwebtoken.ExpiredJwtException e) {
                    System.err.println("[JWT-ERROR] Token expirado: " + e.getMessage());
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    response.setContentType("application/json");
                    response.setCharacterEncoding("UTF-8");
                    response.getWriter().write("{\"error\": \"Token expirado. Por favor, faça login novamente.\"}");
                    return;
                } catch (Exception e) {
                    System.err.println("[JWT-ERROR] Erro ao extrair username do token: " + e.getMessage());
                }
            }
            
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);
                
                if (jwtUtil.validateToken(jwt, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken = 
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    System.out.println("[JWT-SUCCESS] Usuário autenticado com sucesso: " + username + " na rota " + request.getRequestURI());
                } else {
                    System.err.println("[JWT-ERROR] Token inválido para o usuário: " + username);
                }
            }
        } catch (Exception ex) {
            System.err.println("[JWT-CRITICAL] Exceção na extração do token, prosseguindo sem autenticação: " + ex.getMessage());
        }
        
        filterChain.doFilter(request, response);
    }
}